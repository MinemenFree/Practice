package rip.crystal.practice.player.party;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.duel.DuelRequest;
import rip.crystal.practice.player.party.enums.PartyPrivacy;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.visibility.VisibilityLogic;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.chat.ChatHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Getter
public class Party {

	@Getter private final static List<Party> parties = new ArrayList<>();

	private final Player leader;
	@Getter private final List<UUID> players;
	private final List<PartyInvite> invites;
	private PartyPrivacy privacy;
	private final List<UUID> bards = Lists.newArrayList();
	private final List<UUID> diamonds = Lists.newArrayList();
	private final List<UUID> archers = Lists.newArrayList();
	private final List<UUID> rogues = Lists.newArrayList();
	private final int limit;
	private final List<UUID> bannedPlayers = Lists.newArrayList();

	public Party(Player player) {
		this.leader = player;
		this.players = new ArrayList<>();
		this.invites = new ArrayList<>();
		this.privacy = PartyPrivacy.CLOSED;

		this.players.add(player.getUniqueId());

		if (leader.hasPermission("party.vip")){
			limit = cPractice.get().getMainConfig().getInteger("PARTY.VIP_LIMIT");
		} else {
			limit = cPractice.get().getMainConfig().getInteger("PARTY.DEFAULT_LIMIT");
		}

		parties.add(this);
	}

	public void setPrivacy(PartyPrivacy privacy) {
		this.privacy = privacy;

		getListOfPlayers().forEach(player ->
			new MessageFormat(Locale.PARTY_PRIVACY_CHANGE.format(Profile.get(player.getUniqueId()).getLocale()))
				.add("{new_privacy}", privacy.getReadable())
				.send(player));

		//sendMessage(Locale.PARTY_PRIVACY_CHANGE.format(privacy.getReadable()));
	}

	public boolean containsPlayer(UUID uuid) {
		return players.contains(uuid);
	}

	public PartyInvite getInvite(UUID uuid) {
		Iterator<PartyInvite> iterator = invites.iterator();

		while (iterator.hasNext()) {
			PartyInvite invite = iterator.next();

			if (invite.getUuid().equals(uuid)) {
				if (invite.hasExpired()) {
					iterator.remove();
					return null;
				} else return invite;
			}
		}

		return null;
	}

	public void invite(Player target) {
		Profile profile = Profile.get(target.getUniqueId());
		invites.add(new PartyInvite(target.getUniqueId()));

		for (String msg : new MessageFormat(Locale.PARTY_INVITE.format(profile.getLocale()))
			.add("{leader_name}", Profile.get(leader.getUniqueId()).getColor() + leader.getName())
			.toList()) {
			if (msg.contains("%CLICKABLE%")) {
				msg = msg.replace("%CLICKABLE%", "");

				target.spigot().sendMessage(new ChatComponentBuilder("")
						.parse(msg)
						.attachToEachPart(ChatHelper.click("/party join " + leader.getName()))
						.attachToEachPart(ChatHelper.hover(Locale.PARTY_INVITE_HOVER.getString(profile.getLocale())))
						.create());
			} else {
				target.sendMessage(msg);
			}
		}

		getListOfPlayers().forEach(player ->
			new MessageFormat(Locale.PARTY_INVITE_BROADCAST.format(Profile.get(player.getUniqueId()).getLocale()))
			.add("{player_name}", profile.getColor() + target.getName())
			.send(player));

	}

	public void join(Player player) {
		invites.removeIf(invite -> invite.getUuid().equals(player.getUniqueId()));
		players.add(player.getUniqueId());

		//sendMessage(Locale.PARTY_JOIN.format(GxAPI.getColoredName(player)));

		Profile profile = Profile.get(player.getUniqueId());
		getListOfPlayers().forEach(member ->
			new MessageFormat(Locale.PARTY_JOIN.format(Profile.get(member.getUniqueId()).getLocale()))
				.add("{player_name}", profile.getColor() + player.getName())
				.send(member));

		profile.setParty(this);

		if (Profile.get(getLeader().getUniqueId()).getState() == ProfileState.LOBBY) {
			Hotbar.giveHotbarItems(this.getLeader());
		}

		if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
			Hotbar.giveHotbarItems(player);

			for (Player otherPlayer : getListOfPlayers()) {
				TaskUtil.run(() -> VisibilityLogic.handle(player, otherPlayer));
			}
		}

		TaskUtil.run(() -> VisibilityLogic.handle(player));

		for (Player otherPlayer : getListOfPlayers()) {
			TaskUtil.run(() -> VisibilityLogic.handle(otherPlayer, player));
		}
	}

	public void leave(Player player, boolean kick) {
		getListOfPlayers().forEach(member -> {
			Profile profile = Profile.get(member.getUniqueId());
			new MessageFormat(Locale.PARTY_LEAVE.format(profile.getLocale()))
					.add("{player_name}", Profile.get(player.getUniqueId()).getColor() + player.getName())
					.add("{context}", (kick ?
							Locale.PARTY_CONTEXT_KICK.getString(profile.getLocale()) :
							Locale.PARTY_CONTEXT_QUIT.getString(profile.getLocale())))
					.send(member);
		});

		players.removeIf(uuid -> uuid.equals(player.getUniqueId()));

		Profile profile = Profile.get(player.getUniqueId());
		profile.setParty(null);

		if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
			Hotbar.giveHotbarItems(player);
		}

		TaskUtil.run(() -> VisibilityLogic.handle(player));

		for (Player otherPlayer : getListOfPlayers()) {
			TaskUtil.run(() -> VisibilityLogic.handle(otherPlayer, player));
		}
	}

	public void disband() {
		parties.remove(this);

		sendMessage(Locale.PARTY_DISBAND);

		// Remove any party duel requests
		Profile leaderProfile = Profile.get(leader.getUniqueId());

		leaderProfile.getDuelRequests().removeIf(DuelRequest::isParty);

		// Reset player profiles
		for (Player player : getListOfPlayers()) {
			Profile profile = Profile.get(player.getUniqueId());
			profile.setParty(null);

			if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
				Hotbar.giveHotbarItems(player);
			}
		}

		for (Player player : getListOfPlayers()) {
			TaskUtil.run(() -> VisibilityLogic.handle(player));
		}
	}

	public void sendInformation(Player sendTo) {
		Profile profile = Profile.get(sendTo.getUniqueId());
		StringBuilder builder = new StringBuilder();

		for (Player player : getListOfPlayers()) {
			if (leader != player) {
				builder.append(CC.RESET)
						.append(Profile.get(player.getUniqueId()).getColor()).append(player.getName())
						.append(CC.GRAY)
						.append(", ");
			}
		}

		new MessageFormat(Locale.PARTY_INFORMATION.format(profile.getLocale()))
				.add("{status}", privacy.getReadable())
				.add("{leader}", leader.getName()) //
				.add("{members-size}", String.valueOf((getPlayers().size() - 1)))
				.add("{members}", CC.translate(getListOfPlayers().size() > 1 ? builder.substring(0, builder.length() - 2) : ""))
				.send(sendTo);

		/*new MessageFormat(Locale.PARTY_INFORMATION.format(profile.getLocale()))
				.add("{status}", privacy.getReadable())
				.add("{leader}", CC.translate(Profile.get(leader.getUniqueId()).getColor() + leader.getName()))
				.add("{members-size}", String.valueOf((getPlayers().size() - 1)))
				.add("{members}", CC.translate(getListOfPlayers().size() > 1 ? builder.substring(0, builder.length() - 2) : ""))
				.send(sendTo);*/
	}

	public void sendChat(Player player, String message) {
		sendMessage(Locale.PARTY_CHAT_PREFIX.format(Profile.get(player.getUniqueId()).getLocale()) +
		            player.getDisplayName() + ChatColor.RESET + ": " + message);
	}

	public void sendMessage(String message) {
		for (Player player : getListOfPlayers()) {
			player.sendMessage(message);
		}
	}

	public void sendMessage(Locale message) {
		for (Player player : getListOfPlayers()) {
			new MessageFormat(message
					.format(Profile.get(player.getUniqueId()).getLocale()))
					.send(player);
		}
	}

	public List<Player> getListOfPlayers() {
		List<Player> players = new ArrayList<>();

		for (UUID uuid : this.players) {
			Player player = Bukkit.getPlayer(uuid);

			if (player != null) {
				players.add(player);
			}
		}

		return players;
	}

	public void addBard(Player player){
		this.archers.remove(player.getUniqueId());
		this.diamonds.remove(player.getUniqueId());
		this.rogues.remove(player.getUniqueId());
		if(bards.isEmpty()){
			bards.add(player.getUniqueId());
		}else{
			addRogue(player);
		}
	}
	public void addRogue(Player player){
		this.archers.remove(player.getUniqueId());
		this.diamonds.remove(player.getUniqueId());
		this.bards.remove(player.getUniqueId());
		if(rogues.isEmpty()){
			rogues.add(player.getUniqueId());
		}else{
			addDiamond(player);
		}
	}
	public void addArcher(Player player){
		this.bards.remove(player.getUniqueId());
		this.diamonds.remove(player.getUniqueId());
		this.rogues.remove(player.getUniqueId());
		if(archers.isEmpty()){
			archers.add(player.getUniqueId());
		}else{
			addBard(player);
		}
	}
	public void addDiamond(Player player){
		this.archers.remove(player.getUniqueId());
		this.bards.remove(player.getUniqueId());
		this.rogues.remove(player.getUniqueId());
		diamonds.add(player.getUniqueId());
	}

	public static void init() {
		// Remove expired invites from each party every 2 seconds
		new BukkitRunnable() {
			@Override
			public void run() {
				Party.getParties().forEach(party -> party.getInvites().removeIf(PartyInvite::hasExpired));
			}
		}.runTaskTimerAsynchronously(cPractice.get(), 20L * 2, 20L * 2);
	}

}
