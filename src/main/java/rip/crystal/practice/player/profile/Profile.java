package rip.crystal.practice.player.profile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.abilities.cooldown.AbilityCooldown;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.kit.KitLoadout;
import rip.crystal.practice.game.knockback.impl.Default;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.duel.DuelProcedure;
import rip.crystal.practice.match.duel.DuelRequest;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.mongo.MatchInfo;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.clan.ClanInvite;
import rip.crystal.practice.player.cosmetics.impl.killeffects.KillEffectType;
import rip.crystal.practice.player.cosmetics.impl.trails.TrailsEffectType;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.profile.conversation.ProfileConversations;
import rip.crystal.practice.player.profile.file.IProfile;
import rip.crystal.practice.player.profile.file.impl.FlatFileIProfile;
import rip.crystal.practice.player.profile.file.impl.MongoDBIProfile;
import rip.crystal.practice.player.profile.follow.Follow;
import rip.crystal.practice.player.profile.meta.ProfileKitData;
import rip.crystal.practice.player.profile.meta.ProfileKitEditorData;
import rip.crystal.practice.player.profile.meta.ProfileRematchData;
import rip.crystal.practice.player.profile.meta.option.ProfileOptions;
import rip.crystal.practice.player.profile.weight.Weight;
import rip.crystal.practice.player.queue.QueueProfile;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.language.Lang;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import rip.crystal.practice.visual.tablist.TabType;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class Profile {

	@Getter private static Map<UUID, Profile> profiles = new HashMap<>();
	@Getter public static MongoCollection<Document> collection;
	@Getter public static IProfile iProfile;

	private UUID uuid;
	private ProfileState state;
	private final ProfileOptions options;
	private final ProfileKitEditorData kitEditorData;
	private final Map<Kit, ProfileKitData> kitData;
	private final List<DuelRequest> duelRequests;
	private DuelProcedure duelProcedure;
	private ProfileRematchData rematchData;
	private Party party;
	private Match match;
	private BasicTeamMatch basicTeamMatch;
	private QueueProfile queueProfile;
	private Cooldown enderpearlCooldown, voteCooldown, chatCooldown, partyAnnounceCooldown;
	private AbilityCooldown partneritem, beacom, combo, effectdisabler, guardianangel, ninjastar, pocketbard, scrammbler,
			strength, swapperaxe, timewarp, switcher, tankingot, cookie, fakelogger, rocket, antitrapper;

	private ProfileConversations conversations;
	private Clan clan;
	private Map<String, ClanInvite> invites;
	private boolean inTournament, online, isFrozen;
	private int fishHit;
	private int pingRange = -1;
	private List<MatchInfo> matches;
	private Lang locale = Lang.getByAbbreviation(cPractice.get().getMainConfig().getString("DEFAULT_LANG"));
	private KitLoadout selectedKit;
	private String name, color;
	private Weight weight;
	private KillEffectType killEffectType;
	private TrailsEffectType trailsEffectType;
	private TabType tabType = TabType.DEFAULT;
	private Follow follow;
	private int coins;

	public Profile(UUID uuid) {
		this.uuid = uuid;
		this.state = ProfileState.LOBBY;
		this.options = new ProfileOptions();
		this.kitEditorData = new ProfileKitEditorData();
		this.kitData = Maps.newHashMap();
		this.duelRequests = Lists.newArrayList();
		this.enderpearlCooldown = new Cooldown(0);
		this.partyAnnounceCooldown = new Cooldown(0);
		this.voteCooldown = new Cooldown(0);
		this.matches = Lists.newArrayList();
		this.conversations = new ProfileConversations(this);
		this.chatCooldown = new Cooldown(0);
		this.partneritem = new AbilityCooldown();
		this.antitrapper = new AbilityCooldown();
		this.beacom = new AbilityCooldown();
		this.combo = new AbilityCooldown();
		this.cookie = new AbilityCooldown();
		this.effectdisabler = new AbilityCooldown();
		this.guardianangel = new AbilityCooldown();
		this.ninjastar = new AbilityCooldown();
		this.pocketbard = new AbilityCooldown();
		this.rocket = new AbilityCooldown();
		this.scrammbler = new AbilityCooldown();
		this.strength = new AbilityCooldown();
		this.swapperaxe = new AbilityCooldown();
		this.switcher = new AbilityCooldown();
		this.tankingot = new AbilityCooldown();
		this.timewarp = new AbilityCooldown();
		this.color = "&r";
		this.invites = Maps.newHashMap();
		this.name = Bukkit.getOfflinePlayer(this.uuid).getName();

		Kit.getKits().forEach(kit -> this.kitData.put(kit, new ProfileKitData()));
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public DuelRequest getDuelRequest(Player sender) {
		for (DuelRequest duelRequest : duelRequests) {
			if (duelRequest.getSender().equals(sender.getUniqueId())) {
				return duelRequest;
			}
		}
		return null;
	}

	public boolean isDuelRequestExpired(DuelRequest duelRequest) {
		if (duelRequest != null) {
			if (duelRequest.isExpired()) {
				duelRequests.remove(duelRequest);
				return true;
			}
		}

		return false;
	}

	public void msg(String msg) {
		if (this.getPlayer() == null) {
			return;
		}
		this.getPlayer().sendMessage(CC.translate(msg));
	}

	public boolean isBusy() {
		return state != ProfileState.LOBBY;
	}

	public void load() {
		iProfile.load(this);
	}

	public void save() {
		iProfile.save(this);
	}

	public static void init() {
		if (cPractice.get().getRankManager().getRank() instanceof Default) {
			for (int i = 0; i < 3; i++) System.out.println("cPractice needs a permission system to function properly");
			Bukkit.getServer().getPluginManager().disablePlugin(cPractice.get());
		}

		// Players might have joined before the plugin finished loading
		TaskUtil.runLater(() -> {
			if (iProfile instanceof MongoDBIProfile) {
				collection = cPractice.get().getMongoDatabase().getCollection("profiles");
				for (Document document : cPractice.get().getMongoDatabase().getCollection("profiles").find()) {
					UUID uuid = UUID.fromString(document.getString("uuid"));
					Profile profile = new Profile(uuid);

					try {
						TaskUtil.runAsync(profile::load);
					} catch (Exception e) {
						if (profile.isOnline()) {
							profile.getPlayer().kickPlayer(CC.RED + "The server is loading...");
						}
						throw new IllegalArgumentException("The profile of uuid " + uuid + " could not be loaded, check the database to see what is wrong");
					}

					profiles.put(uuid, profile);
				}
			}
			else if (iProfile instanceof FlatFileIProfile) {
				for (String players : cPractice.get().getPlayersConfig().getConfiguration().getConfigurationSection("players").getKeys(false)) {
					UUID uuid = UUID.fromString(players);
					Profile profile = new Profile(uuid);

					try {
						TaskUtil.runAsync(profile::load);
					} catch (Exception e) {
						if (profile.isOnline()) {
							profile.getPlayer().kickPlayer(CC.RED + "The server is loading...");
						}
						continue;
					}

					profiles.put(uuid, profile);
				}
			}
		}, 40L);


		// Expire duel requests
		TaskUtil.runTimerAsync(() -> {
			for (Profile profile : Profile.getProfiles().values().stream()
					.filter(profile -> profile.getPlayer() != null && profile.getPlayer().isOnline())
					.collect(Collectors.toList())) {
				Iterator<DuelRequest> iterator = profile.duelRequests.iterator();

				while (iterator.hasNext()) {
					DuelRequest duelRequest = iterator.next();

					if (duelRequest.isExpired()) {
						duelRequest.expire();
						iterator.remove();
					}
				}
			}
		}, 60L, 60L);

		// Save every 5 minutes to prevent data loss
		TaskUtil.runTimerAsync(() -> {
			for (Player players : Bukkit.getOnlinePlayers()) {
				Profile.get(players.getUniqueId()).save();
			}
		}, 6000L, 6000L);
	}

	public static Profile get(UUID uuid) {
		Profile profile = profiles.get(uuid);
		if (profile == null) profile = new Profile(uuid);
		return profile;
	}

	public void follow(Player player) {
		this.follow = new Follow(this.uuid, player.getUniqueId(), player);
		Follow.getFollows().put(this.uuid, this.follow);
		this.follow.follow();
	}

	public static int getHostSlots(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		BasicConfigurationFile config = cPractice.get().getEventsConfig();
		int slots = config.getInteger("DEFAULT_HOST_SLOTS");

		for (String key : config.getConfiguration().getConfigurationSection("HOST_SLOTS").getKeys(false)) {
			if (player.hasPermission(config.getString("HOST_SLOTS." + key + ".PERMISSION"))) {
				if (config.getInteger("HOST_SLOTS." + key + ".SLOTS") > slots) {
					slots = config.getInteger("HOST_SLOTS." + key + ".SLOTS");
				}
			}
		}

		return slots;
	}

	public void addCoins(int coins) {
		this.coins += coins;
	}

	public void removeCoins(int coins) {
		this.coins -= coins;
	}

}