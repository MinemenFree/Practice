package rip.crystal.practice.scoreboard;

import com.google.common.collect.Lists;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.event.game.EventGame;
import rip.crystal.practice.kit.Kit;
import rip.crystal.practice.party.Party;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileListener;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.profile.match.ScoreboardAdapter;
import rip.crystal.practice.profile.meta.ProfileKitData;
import rip.crystal.practice.profile.modmode.Modmode;
import rip.crystal.practice.queue.QueueProfile;
import rip.crystal.practice.scoreboard.impl.Assemble;
import rip.crystal.practice.scoreboard.impl.AssembleAdapter;
import rip.crystal.practice.tournament.Tournament;
import rip.crystal.practice.utilities.Animation;
import rip.crystal.practice.utilities.TimeUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.elo.EloUtil;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Stream;

public class BoardAdapter implements AssembleAdapter {

	@Override
	public String getTitle(Player player) {
		return CC.translate(Animation.getScoreboardTitle());
	}

	@Override
	public List<String> getLines(Player player) {
		Profile profile = Profile.get(player.getUniqueId());
		if (!profile.getOptions().showScoreboard()) return Lists.newArrayList();
		List<String> lines = Lists.newArrayList();
		BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
		String bars = config.getString("LINES.BARS");

		if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
			config.getStringList("LINES.LOBBY").forEach(line -> {
				lines.add(line
						.replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
						.replace("{in-fights}", String.valueOf(cPractice.get().getInFights()))
						.replace("{in-queues}", String.valueOf(cPractice.get().getInQueues()))
						.replace("{division}", String.valueOf(getDivision(player)))
						.replace("{coins}", String.valueOf(profile.getCoins()))
						.replace("{elo}", String.valueOf(EloUtil.getGlobalElo(profile))));
			});
		}

		if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.TOURNAMENT) {
			if (Tournament.getTournament() != null) {
				lines.addAll(Tournament.getTournament().getTournamentScoreboard());
			} else if (profile.getParty() != null) {
				int added = 0;
				Party party = profile.getParty();
				config.getStringList("LINES.PARTY.LINES").forEach(line -> {
					lines.add(line.replace("{bars}", bars));
				});

				if (party.getListOfPlayers().size() <= 4) {
					for (Player otherPlayer : party.getListOfPlayers()) {
						added++;

						lines.add(config.getString("LINES.PARTY.MEMBERS-FORMAT")
								.replace("{color}", Profile.get(otherPlayer.getUniqueId()).getColor())
								.replace("{player}", otherPlayer.getName()));

						if (added >= 4) break;
					}
				} else {
					config.getStringList("LINES.PARTY.INFO").forEach(line -> {
						lines.add(line.replace("{leader-color}", Profile.get(party.getLeader().getPlayer().getUniqueId()).getColor())
								.replace("{leader}", party.getLeader().getName())
								.replace("{size}", String.valueOf(party.getListOfPlayers().size())));
					});
				}
			} else if (profile.getClan() != null) {
				lines.addAll(profile.getClan().getClanScoreboard());
			}
		} else if (profile.getState() == ProfileState.QUEUEING) {
			QueueProfile queueProfile = profile.getQueueProfile();

			config.getStringList("LINES.IN-QUEUE").forEach(line -> {
				if (line.contains("{ranked}")) {
					if (queueProfile.getQueue().isRanked()) {
						lines.add(config.getString("LINES.RANKED-QUEUE")
								.replace("{min-range}", String.valueOf(queueProfile.getMinRange()))
								.replace("{max-range}", String.valueOf(queueProfile.getMaxRange())));
					}
					return;
				}
				if (line.contains("{pingrange}")) {
					//if(profile.getOptions().isUsingPingFactor()) {
						lines.add(config.getString("LINES.PINGRANGE-QUEUE").replace("{range}", "" + profile.getPingRange()));
					//}
					return;
				}
				lines.add(line.replace("{queue}", queueProfile.getQueue().getQueueName())
						.replace("{elapsed}", TimeUtil.millisToTimer(queueProfile.getPassed()))
						.replace("{bars}", bars));
			});
		}
		else if (profile.getState() == ProfileState.FIGHTING) {
			lines.addAll(profile.getMatch().getScoreboardLines(player));
		}
		else if(profile.getState() == ProfileState.SPECTATING) {
            //lines.addAll(profile.getBasicTeamMatch().applySpectatorScoreboard(player));
			lines.addAll(ScoreboardAdapter.getScoreboardLinesSpecator(player));
		}
		else if (profile.getState() == ProfileState.FFA) {
			lines.addAll(ScoreboardAdapter.getScoreboardLinesFFA(player));
		}
		if (profile.getState() == ProfileState.EVENT) {
			if (EventGame.getActiveGame() != null) {
				lines.addAll(EventGame.getActiveGame().getGameLogic().getScoreboardEntries());
			}
		}
		else if (profile.getState() == ProfileState.STAFF_MODE) {
			lines.addAll(Modmode.getScoreboardLines(player));
		}

		lines.add(0, "&0" + bars);
		if (config.getBoolean("FOOTER-ENABLED")) {
			lines.add("");
			lines.add(Animation.getScoreboardFooter());
		}
		lines.add("&7" + bars);

		return cPractice.get().isPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(player, lines) : lines;
	}

	public static void hook() {
		Assemble assemble = new Assemble(cPractice.get(), new BoardAdapter());
		assemble.setTicks(2);
	}

	public String getDivision(final Player player) {
		Profile profile = Profile.get(player.getUniqueId());
		int elo = EloUtil.getGlobalElo(profile);

		String division = "";

		if (elo <= 1000) {
			division = CC.GRAY + "Silver V";
		}
		if (elo >= 1000) {
			division = CC.GRAY + "Silver IV";
		}
		if (elo >= 1050) {
			division = CC.GRAY + "Silver III";
		}
		if (elo >= 1075) {
			division = CC.GRAY + "Silver II";
		}
		if (elo >= 1100) {
			division = CC.GRAY + "Silver I";
		}
		if (elo >= 1150) {
			division = CC.YELLOW + "Gold V";
		}
		if (elo >= 1300) {
			division = CC.YELLOW + "Gold IV";
		}
		if (elo >= 1350) {
			division = CC.YELLOW + "Gold III";
		}
		if (elo >= 1400) {
			division = CC.YELLOW + "Gold II";
		}
		if (elo >= 1450) {
			division = CC.YELLOW + "Gold I";
		}
		if (elo >= 1500) {
			division = CC.AQUA + "Platinum V";
		}
		if (elo >= 1600) {
			division = CC.AQUA + "Platinum IV";
		}
		if (elo >= 1700) {
			division = CC.AQUA + "Platinum III";
		}
		if (elo >= 1800) {
			division = CC.AQUA + "Platinum II";
		}
		if (elo >= 1900) {
			division = CC.AQUA + "Platinum I";
		}
		if (elo >= 2000) {
			division = CC.GREEN + "Emerald V";
		}
		if (elo >= 2100) {
			division = CC.GREEN + "Emerald IV";
		}
		if (elo >= 2200) {
			division = CC.GREEN + "Emerald III";
		}
		if (elo >= 2300) {
			division = CC.GREEN + "Emerald II";
		}
		if (elo >= 2400) {
			division = CC.GREEN + "Emerald I";
		}
		if (elo >= 2500) {
			division = CC.BLUE + "Sapphire V";
		}
		if (elo >= 2600) {
			division = CC.BLUE + "Sapphire IV";
		}
		if (elo >= 2700) {
			division = CC.BLUE + "Sapphire III";
		}
		if (elo >= 2800) {
			division = CC.BLUE + "Sapphire II";
		}
		if (elo >= 2900) {
			division = CC.BLUE + "Sapphire I";
		}
		if (elo >= 3000) {
			division = CC.GOLD + "Champion";
		}
		return division;
	}
}
