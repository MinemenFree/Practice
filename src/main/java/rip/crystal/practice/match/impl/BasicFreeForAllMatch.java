package rip.crystal.practice.match.impl;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchSnapshot;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.meta.ProfileKitData;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import rip.crystal.practice.utilities.lag.LagRunnable;

import java.util.ArrayList;
import java.util.List;

public class BasicFreeForAllMatch extends Match {

	private final List<GameParticipant<MatchGamePlayer>> participants;
	private GameParticipant<MatchGamePlayer> winningParticipant;

	public BasicFreeForAllMatch(Queue queue, Kit kit, Arena arena, List<GameParticipant<MatchGamePlayer>> participants) {
		super(queue, kit, arena, false);

		this.participants = participants;
	}

	@Override
	public void setupPlayer(Player player) {
		// Set the player as alive
		MatchGamePlayer gamePlayer = getGamePlayer(player);
		gamePlayer.setDead(false);

		// If the player disconnected, skip any operations for them
		if (gamePlayer.isDisconnected()) {
			return;
		}

		// Reset the player's inventory
		PlayerUtil.reset(player);

		// Deny movement if the kit is sumo
		if (getKit().getGameRules().isSumo()) {
			PlayerUtil.denyMovement(player);
		}

		// Set the player's max damage ticks
		player.setMaximumNoDamageTicks(getKit().getGameRules().getHitDelay());

		// If the player has no kits, apply the default kit, otherwise
		// give the player a list of kit books to choose from
		if (!getKit().getGameRules().isSumo()) {
			Profile profile = Profile.get(player.getUniqueId());
			ProfileKitData kitData = profile.getKitData().get(getKit());

			if (kitData.getKitCount() > 0) {
				profile.getKitData().get(getKit()).giveBooks(player);
			} else {
				player.getInventory().setArmorContents(getKit().getKitLoadout().getArmor());
				player.getInventory().setContents(getKit().getKitLoadout().getContents());
				//player.sendMessage(Locale.MATCH_GIVE_KIT.format("Default"));
				new MessageFormat(Locale.MATCH_GIVE_KIT.format(profile.getLocale()))
					.add("{kit_name}", "Default")
					.send(player);
			}
		}

		// Teleport the player to their spawn point
		Location spawn = arena.getSpawnA();

		if (spawn.getBlock().getType() == Material.AIR) {
			player.teleport(spawn);
		} else {
			player.teleport(spawn.add(0, 2, 0));
		}
	}

	@Override
	public boolean canEndMatch() {
		return getRemainingTeams() <= 1;
	}

	@Override
	public boolean canStartRound() {
		return false;
	}

	@Override
	public boolean canEndRound() {
		return getRemainingTeams() <= 1;
	}

	@Override
	public void onRoundEnd() {
		for (GameParticipant<MatchGamePlayer> gameParticipant : participants) {
			if (!gameParticipant.isAllDead()) {
				winningParticipant = gameParticipant;
				break;
			}
		}

		if (!kit.getGameRules().isSumo() && !kit.getGameRules().isBridge() && !kit.getGameRules().isBedFight()) {
			// Make all snapshots available
			for (MatchSnapshot snapshot : snapshots) {
				snapshot.setCreatedAt(System.currentTimeMillis());
				MatchSnapshot.getSnapshots().put(snapshot.getUuid(), snapshot);
			}
		}

		super.onRoundEnd();
	}

	@Override
	public boolean isOnSameTeam(Player first, Player second) {
		return first.equals(second);
	}

	@Override
	public List<GameParticipant<MatchGamePlayer>> getParticipants() {
		return new ArrayList<>(participants);
	}

	@Override
	public ChatColor getRelationColor(Player viewer, Player target) {
		if (viewer.equals(target)) {
			return ChatColor.GREEN;
		} else {
			for (GameParticipant<MatchGamePlayer> participant : participants) {
				if (participant.containsPlayer(target.getUniqueId())) {
					return ChatColor.RED;
				}
			}

			return ChatColor.YELLOW;
		}
	}

	@Override
	public List<String> getScoreboardLines(Player player) {
		List<String> lines = new ArrayList<>();
		BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
		Profile profile = Profile.get(player.getUniqueId());
		if (profile.getMatch() != null && profile.getState() == ProfileState.STAFF_MODE) { // remove / change later
			cPractice.get().getScoreboardConfig().getStringList("STAFF_MODE.SPECTATING").forEach(s ->
					lines.add(s
							.replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
							.replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
							.replace("{duration}", profile.getMatch().getDuration())
							.replace("{state}", profile.getMatch().getState().name())
							//.replace("{ranked}", (profile.getMatch().getQueue().isRanked() ? "&aTrue" : "&cFalse"))
							.replace("{tps}", format(LagRunnable.getTPS()))));
							//.replace("{tps}", format(Bukkit.spigot().getTPS()[0]))));
		}

		if (getParticipant(player) != null && !getGamePlayer(player).isDead()) {
			config.getStringList("FIGHTS.PARTY-FFA.IS-ALIVE").forEach(line -> {
				lines.add(line
						.replace("{duration}", getDuration())
						.replace("{opponents-size}", String.valueOf(getRemainingTeams() - 1)));
			});
		} else {
			config.getStringList("FIGHTS.PARTY-FFA.IS-DEAD-OR-SPECTATOR").forEach(line -> {
				lines.add(line
						.replace("{kit}", getKit().getName())
						.replace("{duration}", getDuration())
						.replace("{teams}", String.valueOf(getRemainingTeams())));
			});
		}

		return lines;
	}

	private static String format(double tps) {
		int max = 20;
		return ((tps > 18.0D) ? ChatColor.GREEN : ((tps > 16.0D) ? ChatColor.YELLOW : ChatColor.RED)) + ((tps > max) ? "*" : "") + Math.min(Math.round(tps * 100.0D) / 100.0D, max);
	}

	@Override
	public void addSpectator(Player spectator, Player target) {
		super.addSpectator(spectator, target);
	}

	@Override
	public List<BaseComponent[]> generateEndComponents(Player player) {
		List<BaseComponent[]> componentsList = new ArrayList<>();
		Profile profile = Profile.get(player.getUniqueId());

		for (String line : Locale.MATCH_END_DETAILS.getStringList(profile.getLocale())) {
			if (line.equalsIgnoreCase("%INVENTORIES%")) {
				List<GameParticipant<MatchGamePlayer>> participants = new ArrayList<>(this.participants);
				participants.remove(winningParticipant);

				BaseComponent[] winners = generateInventoriesComponents(
					new MessageFormat(Locale.MATCH_END_WINNER_INVENTORY.format(profile.getLocale()))
						.add("{context}", "").toString(), winningParticipant);

				BaseComponent[] losers = generateInventoriesComponents(
						new MessageFormat(Locale.MATCH_END_LOSER_INVENTORY.format(profile.getLocale()))
							.add("{context}", participants.size() > 1 ? "s" : "").toString(), participants);

				componentsList.add(winners);
				componentsList.add(losers);

				continue;
			}

			if (line.equalsIgnoreCase("%ELO_CHANGES%")) continue;

			componentsList.add(new ChatComponentBuilder("").parse(line).create());
		}

		return componentsList;
	}

	private int getRemainingTeams() {
		int remaining = 0;

		for (GameParticipant<MatchGamePlayer> gameParticipant : participants) {
			if (!gameParticipant.isAllDead()) {
				remaining++;
			}
		}

		return remaining;
	}

}
