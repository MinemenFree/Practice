package rip.crystal.practice.player.profile.meta;

import lombok.Getter;
import lombok.var;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.impl.BasicTeamRoundMatch;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.chat.ChatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ProfileRematchData {

	private final UUID key;
	private final UUID sender;
	private final UUID target;
	private final Kit kit;
	private final Arena arena;
	private boolean sent;
	private boolean receive;
	private boolean cancelled;
	private final long timestamp;

	public ProfileRematchData(UUID key, UUID sender, UUID target, Kit kit, Arena arena) {
		this.key = key;
		this.sender = sender;
		this.target = target;
		this.kit = kit;
		this.arena = arena;
		this.timestamp = System.currentTimeMillis();
	}

	public void request() {
		this.validate();

		if (cancelled) {
			return;
		}

		Player sender = Bukkit.getPlayer(this.sender);
		Player target = Bukkit.getPlayer(this.target);

		if (sender == null || target == null) {
			return;
		}

		Profile senderProfile = Profile.get(sender.getUniqueId());
		Profile targetProfile = Profile.get(target.getUniqueId());

		if (senderProfile.isBusy()) {
			sender.sendMessage(CC.RED + "You cannot duel right now.");
			return;
		}

		if (targetProfile.isBusy()) {
			sender.sendMessage(CC.RED + "You cannot duel right now.");
			return;
		}



		for (String line : new MessageFormat(Locale.REMATCH_SENT_REQUEST.format(Profile.get(sender.getUniqueId()).getLocale()))
							.add("{target_name}", target.getName())
							.add("{arena_name}", arena.getName())
							.toList()) {
			sender.sendMessage(line);
		}

		List<BaseComponent[]> components = new ArrayList<>();



		for (String line : new MessageFormat(Locale.REMATCH_RECEIVED_REQUEST.format(Profile.get(target.getUniqueId()).getLocale()))
							.add("{sender_name}", sender.getName())
							.add("{arena_name}", arena.getName())
							.toList()) {
			BaseComponent[] lineComponents = new ChatComponentBuilder("")
					.parse(line)
					.attachToEachPart(ChatHelper.hover(Locale.REMATCH_RECEIVED_REQUEST_HOVER.format(Profile.get(target.getUniqueId()).getLocale()).toString()))
					.attachToEachPart(ChatHelper.click("/rematch"))
					.create();

			components.add(lineComponents);
		}

		for (BaseComponent[] line : components) {
			target.spigot().sendMessage(line);
		}

		this.sent = true;

		targetProfile.getRematchData().receive = true;
	}

	public void accept() {
		this.validate();

		Player sender = cPractice.get().getServer().getPlayer(this.sender);
		Player target = cPractice.get().getServer().getPlayer(this.target);

		if (sender == null || target == null || !sender.isOnline() || !target.isOnline()) {
			return;
		}

		Profile senderProfile = Profile.get(sender.getUniqueId());
		Profile targetProfile = Profile.get(target.getUniqueId());

		if (senderProfile.isBusy()) {
			sender.sendMessage(CC.RED + "You cannot duel right now.");
			return;
		}

		if (targetProfile.isBusy()) {
			sender.sendMessage(target.getDisplayName() + CC.RED + " is currently busy.");
			return;
		}

		Arena arena = this.arena;

		if (arena == null || arena.isActive()) {
			arena = Arena.getRandomArena(kit);
		}

		if (arena == null) {
			sender.sendMessage(CC.RED + "Tried to start a match but there are no available arenas.");
			return;
		}

		arena.setActive(true);

		MatchGamePlayer playerA = new MatchGamePlayer(sender.getUniqueId(), sender.getName());
		MatchGamePlayer playerB = new MatchGamePlayer(target.getUniqueId(), target.getName());

		GameParticipant<MatchGamePlayer> participantA = new GameParticipant<>(playerA);
		GameParticipant<MatchGamePlayer> participantB = new GameParticipant<>(playerB);

		var match = new BasicTeamMatch(null, kit, arena, false, participantA, participantB);
		if(match.getKit().getGameRules().isBridge()){
			match = new BasicTeamRoundMatch(null, kit, arena, false, participantA, participantB, cPractice.get().getBridgeRounds());
		}
		match.start();
	}

	public void validate() {
		for (UUID uuid : new UUID[]{ sender, target }) {
			Player player = Bukkit.getPlayer(uuid);

			if (player != null) {
				Profile profile = Profile.get(player.getUniqueId());

				if (profile.getRematchData() == null) {
					this.cancel();
					return;
				}

				if (!profile.getRematchData().getKey().equals(this.key)) {
					this.cancel();
					return;
				}

				if (!(profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING)) {
					this.cancel();
					return;
				}

				if (System.currentTimeMillis() >= timestamp + 30_000L) {
					this.cancel();
					return;
				}
			}
		}
	}

	public void cancel() {
		this.cancelled = true;

		for (UUID uuid : new UUID[]{ sender, target }) {
			Player player = Bukkit.getPlayer(uuid);

			if (player != null) {
				Profile profile = Profile.get(player.getUniqueId());
				profile.setRematchData(null);

				if (profile.getState() == ProfileState.LOBBY) {
					Hotbar.giveHotbarItems(player);
				}
			}
		}
	}

}
