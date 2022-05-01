package rip.crystal.practice.match.impl;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchState;
import rip.crystal.practice.match.mongo.MatchInfo;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.party.classes.rogue.RogueClass;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.meta.ProfileKitData;
import rip.crystal.practice.player.profile.meta.ProfileRematchData;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.utilities.*;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.elo.EloUtil;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
public class BasicTeamMatch extends Match {

	private final GameParticipant<MatchGamePlayer> participantA;
	private final GameParticipant<MatchGamePlayer> participantB;
	private @Setter GameParticipant<MatchGamePlayer> winningParticipant;
	private @Setter GameParticipant<MatchGamePlayer> losingParticipant;

	private boolean hasBed = true;

	public BasicTeamMatch(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayer> participantA,
                          GameParticipant<MatchGamePlayer> participantB) {
		super(queue, kit, arena, ranked);

		this.participantA = participantA;
		this.participantB = participantB;
	}

	@Override
	public void setupPlayer(Player player) {
		super.setupPlayer(player);
		Profile profile = Profile.get(player.getUniqueId());

		Party party = profile.getParty();
		if (party != null) {
			if (getKit().getGameRules().isHcf()) {
				Kit kit;
				if (party.getArchers().contains(player.getUniqueId())) {
					kit = Kit.getByName("Archer");
					player.getInventory().setArmorContents(Objects.requireNonNull(kit).getKitLoadout().getArmor());
					player.getInventory().setContents(kit.getKitLoadout().getContents());
				} else if (party.getBards().contains(player.getUniqueId())) {
					kit = Kit.getByName("Bard");
					player.getInventory().setArmorContents(Objects.requireNonNull(kit).getKitLoadout().getArmor());
					player.getInventory().setContents(kit.getKitLoadout().getContents());
				} else if (party.getRogues().contains(player.getUniqueId())) {
					kit = Kit.getByName("Rogue");
					player.getInventory().setArmorContents(Objects.requireNonNull(kit).getKitLoadout().getArmor());
					player.getInventory().setContents(kit.getKitLoadout().getContents());
				}
			}
		}

		if (getKit().getGameRules().isBridge()){
			ProfileKitData kitData = profile.getKitData().get(getKit());
			if (kitData.getKitCount() == 0) {
				player.getInventory().setContents(getKit().getKitLoadout().getContents());
				KitUtils.giveBridgeKit(player);
			}
		}

		if (getKit().getGameRules().isBedFight()){
			ProfileKitData kitData = profile.getKitData().get(getKit());
			if (kitData.getKitCount() == 0) {
				player.getInventory().setContents(getKit().getKitLoadout().getContents());
				KitUtils.giveBedFightKit(player);
			}
		}


		if (getKit().getGameRules().isHcftrap()) {
			ProfileKitData kitData = profile.getKitData().get(getKit());
			if (kitData.getKitCount() == 0) {
				player.getInventory().setContents(getKit().getKitLoadout().getContents());
				KitUtils.giveBaseRaidingKit(player);
			}
		}

		player.updateInventory();

		// Teleport the player to their spawn point
		Location spawn = participantA.containsPlayer(player.getUniqueId()) ?
				getArena().getSpawnA() : getArena().getSpawnB();

		if (spawn.getBlock().getType() == Material.AIR) player.teleport(spawn);
		else player.teleport(spawn.add(0, 2, 0));
	}

	public void destroyBed() {
		this.hasBed = false;
	}

	public boolean isHasBed() {
		return this.hasBed;
	}

	@Override
	public void end() {
		if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1) {
			UUID rematchKey = UUID.randomUUID();

			for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
				for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
					if (!gamePlayer.isDisconnected()) {
						Profile profile = Profile.get(gamePlayer.getUuid());

						if (profile.getParty() == null) {
							if (gamePlayer.getPlayer() == null) {
								super.end();
								return;
							}
							UUID opponent;

							if (gameParticipant.equals(participantA)) opponent = participantB.getLeader().getUuid();
							else opponent = participantA.getLeader().getUuid();

							if (opponent != null) {
								ProfileRematchData rematchData = new ProfileRematchData(rematchKey,
										gamePlayer.getUuid(), opponent, kit, arena);
								profile.setRematchData(rematchData);
							}

							RogueClass.getLastJumpUsage().remove(profile.getPlayer().getName());
							RogueClass.getLastSpeedUsage().remove(profile.getPlayer().getName());
							RogueClass.getBackstabCooldown().remove(profile.getPlayer().getName());
						}
					}
				}
			}
		}

		super.end();
	}

	@Override
	public boolean canEndMatch() {
		return true;
	}

	@Override
	public boolean canStartRound() {
		if (kit.getGameRules().isSumo()) {
			if (ranked) {
				return !(participantA.getRoundWins() == 3 || participantB.getRoundWins() == 3);
			}
		}
		return kit.getGameRules().isBridge();
	}

	@Override
	public void onRoundEnd() {
		// Store winning participant
		winningParticipant = participantA.isAllDead() ? participantB : participantA;
		//winningParticipant.setRoundWins(winningParticipant.getRoundWins() + 1);

		if (kit.getGameRules().isBridge()) {
			this.getParticipants().forEach(participant ->
				participant.getPlayers().forEach(gamePlayer -> {
					Player other = gamePlayer.getPlayer();
					PlayerUtil.reset(other);

					Location spawn = this.getParticipantA().containsPlayer(other.getUniqueId()) ?
						this.getArena().getSpawnA() :
						this.getArena().getSpawnB();

					PlayerUtil.denyMovement(other);
					other.teleport(spawn.add(0, 2, 0));
					Profile profile = Profile.get(other.getUniqueId());
					if (profile.getSelectedKit() == null) {
						other.getInventory().setContents(getKit().getKitLoadout().getContents());
					} else {
						other.getInventory().setContents(profile.getSelectedKit().getContents());
					}
					KitUtils.giveBridgeKit(other);

				}));
			return;
		}

		// Store losing participant
		losingParticipant = participantA.isAllDead() ? participantA : participantB;
		losingParticipant.setEliminated(true);

		if(canEndMatch()) {
			/*winningParticipant.getPlayers().forEach(matchGamePlayer -> {
				Profile profile = Profile.get(matchGamePlayer.getUuid());
				matchGamePlayer.getPlayer().sendTitle(new Title(
						new MessageFormat(Locale.MATCH_WINNER_TITLE.format(profile.getLocale())).getMessage(),
						new MessageFormat(Locale.MATCH_WINNER_SUBTITLE.format(profile.getLocale()))
								.add("{winner}", winningParticipant.getLeader().getUsername()).getMessage(),
						20, 40, 20));
					});
			/*losingParticipant.getPlayers().forEach(matchGamePlayer -> {
				Profile profile = Profile.get(matchGamePlayer.getUuid());
				matchGamePlayer.getPlayer().sendTitle(new Title(
						new MessageFormat(Locale.MATCH_LOSER_TITLE.format(profile.getLocale())).getMessage(),
						new MessageFormat(Locale.MATCH_LOSER_SUBTITLE.format(profile.getLocale()))
								.add("{winner}", winningParticipant.getLeader().getUsername()).getMessage(),
						20, 40, 20));
			});*/
		}

		if (kit.getGameRules().isSumo()) {
			if (!canEndMatch()) {
				//int roundsToWin = (ranked ? 3 : 1) - winningParticipant.getRoundWins();
				state = MatchState.ENDING_ROUND;
				logicTask.setNextAction(3);
				this.getParticipants().forEach(participant ->
					participant.getPlayers().forEach(gamePlayer -> {
						Player player = gamePlayer.getPlayer();
						// Teleport the player to their spawn point
						Location spawn = participantA.containsPlayer(player.getUniqueId()) ?
							getArena().getSpawnA() : getArena().getSpawnB();

						if (spawn.getBlock().getType() == Material.AIR) player.teleport(spawn);
						else player.teleport(spawn.add(0, 8 /*2*/, 0));
						PlayerUtil.denyMovement(player);
					}));
				return;
			}
		}

		// Set opponents in snapshots if solo
		if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1) {

			if (ranked) {
				int oldWinnerElo = winningParticipant.getLeader().getElo();
				int oldLoserElo = losingParticipant.getLeader().getElo();

				int newWinnerElo = EloUtil.getNewRating(oldWinnerElo, oldLoserElo, true);
				int newLoserElo = EloUtil.getNewRating(oldLoserElo, oldWinnerElo, false);

				winningParticipant.getLeader().setEloMod(newWinnerElo - oldWinnerElo);
				losingParticipant.getLeader().setEloMod(oldLoserElo - newLoserElo);

				Profile winningProfile = Profile.get(winningParticipant.getLeader().getUuid());
				winningProfile.getKitData().get(getKit()).setElo(newWinnerElo);
				winningProfile.getKitData().get(getKit()).incrementWon();

				Profile losingProfile = Profile.get(losingParticipant.getLeader().getUuid());
				losingProfile.getKitData().get(getKit()).setElo(newLoserElo);
				losingProfile.getKitData().get(getKit()).incrementLost();

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();

				MatchInfo matchInfo = new MatchInfo(winningParticipant.getConjoinedNames(),
					losingParticipant.getConjoinedNames(),
					getKit(),
					winningParticipant.getLeader().getEloMod(),
					losingParticipant.getLeader().getEloMod(),
					dtf.format(now),
					TimeUtil.millisToTimer(System.currentTimeMillis() - timeData));

				winningProfile.getMatches().add(matchInfo);
				losingProfile.getMatches().add(matchInfo);
			}
		}


		super.onRoundEnd();
	}

	@Override
	public boolean canEndRound() {
		return participantA.isAllDead() || participantB.isAllDead();
	}

	@Override
	public boolean isOnSameTeam(Player first, Player second) {
		boolean[] booleans = new boolean[]{
				participantA.containsPlayer(first.getUniqueId()),
				participantB.containsPlayer(first.getUniqueId()),
				participantA.containsPlayer(second.getUniqueId()),
				participantB.containsPlayer(second.getUniqueId())
		};

		return (booleans[0] && booleans[2]) || (booleans[1] && booleans[3]);
	}

	@Override
	public List<GameParticipant<MatchGamePlayer>> getParticipants() {
		return Arrays.asList(participantA, participantB);
	}

	@Override
	public ChatColor getRelationColor(Player viewer, Player target) {
		if (getKit().getGameRules().isBridge()) {
			if (participantA.containsPlayer(target.getUniqueId())) return ChatColor.RED;
			else return ChatColor.BLUE;
		}

		if (getKit().getGameRules().isBedFight()) {
			if (participantA.containsPlayer(target.getUniqueId())) return ChatColor.RED;
			else return ChatColor.BLUE;
		}

		if (viewer.equals(target)) return ChatColor.GREEN;

		boolean[] booleans = new boolean[]{
				participantA.containsPlayer(viewer.getUniqueId()),
				participantB.containsPlayer(viewer.getUniqueId()),
				participantA.containsPlayer(target.getUniqueId()),
				participantB.containsPlayer(target.getUniqueId())
		};

		if ((booleans[0] && booleans[3]) || (booleans[2] && booleans[1])) return ChatColor.RED;
		else if ((booleans[0] && booleans[2]) || (booleans[1] && booleans[3])) return ChatColor.GREEN;
		else if (spectators.contains(viewer.getUniqueId())) return participantA.containsPlayer(target.getUniqueId()) ?
					ChatColor.GREEN : ChatColor.RED;
		else return ChatColor.YELLOW;
	}

	@Override
	public List<String> getScoreboardLines(Player player) {
		List<String> lines = new ArrayList<>();
		BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
		String bars = config.getString("LINES.BARS");
		Profile profile = Profile.get(player.getUniqueId());

		if (getParticipant(player) != null) {
			if (state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND || state == MatchState.ENDING_ROUND) {
				if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1) {
					GameParticipant<MatchGamePlayer> opponent;
					GameParticipant<MatchGamePlayer> yours;

					if (participantA.containsPlayer(player.getUniqueId())) {
						opponent = participantB;
						yours = participantA;
					}
					else {
						opponent = participantA;
						yours = participantB;
					}

					if(opponent.getLeader().getPlayer() == null) {
						return null;
					}

					if(yours.getLeader().getPlayer() == null) {
						return null;
					}

					String actualHits = "0";
					if ((yours.getLeader().getHits() - opponent.getLeader().getHits()) > 0) {
						actualHits = "+" + (yours.getLeader().getHits() - opponent.getLeader().getHits());
					}
					else if ((yours.getLeader().getHits() - opponent.getLeader().getHits()) < 0) {
						actualHits = String.valueOf(yours.getLeader().getHits() - opponent.getLeader().getHits());
					}

					if (kit.getGameRules().isBoxing()) {
						String finalActualHits = actualHits;
						config.getStringList("FIGHTS.1V1.BOXING-MODE").forEach(line -> {
							lines.add(line.replace("{bars}", bars)
									.replace("{duration}", getDuration())
									.replace("{opponent-color}", Profile.get(opponent.getLeader().getPlayer().getUniqueId()).getColor())
									.replace("{opponent}", opponent.getLeader().getPlayer().getName())
									.replace("{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getLeader().getPlayer())))
									.replace("{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
									.replace("{arena-author}", getArena().getAuthor())
									.replace("{kit}", getKit().getName())
									//.replace("{hits}", finalActualHits)
									.replace("{hits}", (yours.getLeader().getHits() >= opponent.getLeader().getHits() ? CC.GREEN : CC.RED) + "(" + (yours.getLeader().getHits() >= opponent.getLeader().getHits() ? "+" : "-") + (yours.getLeader().getHits() >= opponent.getLeader().getHits() ? String.valueOf(yours.getLeader().getHits() - opponent.getLeader().getHits()) : String.valueOf(opponent.getLeader().getHits() - yours.getLeader().getHits())) + ")")
									.replace("{your-hits}", String.valueOf(yours.getLeader().getHits()))
									.replace("{opponent-hits}", String.valueOf(opponent.getLeader().getHits()))
									.replace("{combo}", String.valueOf(yours.getLeader().getCombo())));
						});
						return lines;
					}

					config.getStringList("FIGHTS.1V1.LINES").forEach(line -> {
						if (line.contains("{bridge}")) return;
						if (line.contains("{rounds}")) return;
						lines.add(line.replace("{bars}", bars)
								.replace("{duration}", getDuration())
								.replace("{opponent-color}", Profile.get(opponent.getLeader().getPlayer().getUniqueId()).getColor())
								.replace("{opponent}", opponent.getLeader().getPlayer().getName())
								.replace("{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getLeader().getPlayer())))
								.replace("{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
								.replace("{arena-author}", getArena().getAuthor())
								.replace("{kit}", getKit().getName()));
					});
				} else {
					GameParticipant<MatchGamePlayer> friendly = getParticipant(player);
					GameParticipant<MatchGamePlayer> opponent = participantA.equals(friendly) ?
							participantB : participantA;

					if (friendly.getPlayers().size() + opponent.getPlayers().size() <= 6) {
						config.getStringList("FIGHTS.SMALL-TEAM.LINES").forEach(line -> {
							if (line.contains("{bridge}")) {
								if (kit.getGameRules().isBridge()) {
									config.getStringList("FIGHTS.BRIDGE-FORMAT.LINES").forEach(line2 -> {
										if (line2.contains("{points}")) return;
										lines.add(line2.replace("{kills}", String.valueOf(getGamePlayer(player).getKills())));
									});
								}
								return;
							}
							if (line.contains("{no-bridge}")) {
								if (!kit.getGameRules().isBridge()) {
									config.getStringList("FIGHTS.SMALL-TEAM.NO-BRIDGE.LINES").forEach(line2 -> {
										if (line2.contains("{players}")) {
											for (MatchGamePlayer gamePlayer : friendly.getPlayers()) {
												lines.add(config.getString("FIGHTS.SMALL-TEAM.NO-BRIDGE.PLAYERS-FORMAT")
														.replace("{player}", (gamePlayer.isDead() || gamePlayer.isDisconnected() ? "&7&m" : "") +
																gamePlayer.getUsername()));
											}
											return;
										}
										if (line2.contains("{opponents}")) {
											for (MatchGamePlayer gamePlayer : opponent.getPlayers()) {
												lines.add(config.getString("FIGHTS.SMALL-TEAM.NO-BRIDGE.OPPONENTS-FORMAT")
														.replace("{opponent}", (gamePlayer.isDead() || gamePlayer.isDisconnected() ? "&7&m" : "") +
																gamePlayer.getUsername()));
											}
											return;
										}
										lines.add(line2.replace("{bars}", bars)
												.replace("{team-alive}", String.valueOf(friendly.getAliveCount()))
												.replace("{team-size}", String.valueOf(friendly.getPlayers().size()))
												.replace("{opponent-alive}", String.valueOf(opponent.getAliveCount()))
												.replace("{opponent-size}", String.valueOf(opponent.getPlayers().size())
												.replace("{kit}", getKit().getName())));
									});
								}
								return;
							}
							if (line.contains("{rounds}")) return;
							lines.add(line.replace("{bars}", bars)
									.replace("{duration}", getDuration())
									.replace("{arena-author}", getArena().getAuthor())
									.replace("{kit}", getKit().getName()));
						});
					} else {
						config.getStringList("FIGHTS.BIG-TEAM.LINES").forEach(line -> {
							if (line.contains("{bridge}")) {
								if (kit.getGameRules().isBridge()) {
									config.getStringList("FIGHTS.BRIDGE-FORMAT.LINES").forEach(line2 -> {
										if (line2.contains("{points}")) return;
										lines.add(line2.replace("{kills}", String.valueOf(getGamePlayer(player).getKills())));
									});
								}
								return;
							}
							if (line.contains("{rounds}")) return;
							lines.add(line.replace("{duration}", getDuration())
									.replace("{arena-author}", getArena().getAuthor())
									.replace("{team-alive}", String.valueOf(friendly.getAliveCount()))
									.replace("{team-size}", String.valueOf(friendly.getPlayers().size()))
									.replace("{opponent-alive}", String.valueOf(opponent.getAliveCount()))
									.replace("{opponent-size}", String.valueOf(opponent.getPlayers().size()))
									.replace("{kit}", getKit().getName()));
						});
					}
				}
			} else {
				config.getStringList("FIGHTS.ON-END-ROUND-FOR-NEXT").forEach(line -> {
					lines.add(line.replace("{duration}", getDuration())
							.replace("{arena-author}", getArena().getAuthor())
							.replace("{kit}", getKit().getName()));
				});
			}
		}

		return lines;
	}

	public List<String> applySpectatorScoreboard(Player spectator) {
		List<String> lines = Lists.newArrayList();
		Profile profile = Profile.get(spectator.getUniqueId());

		if (profile.getMatch() != null) {
			cPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING").forEach(s ->
					lines.add(s
							.replace("{playerA}", String.valueOf(participantA.getLeader().getUsername()))
							.replace("{playerB}", String.valueOf(participantB.getLeader().getUsername()))
							.replace("{duration}", profile.getMatch().getDuration())
							.replace("{kit}", profile.getMatch().getKit().getName())
							.replace("{spectators}", String.valueOf(profile.getMatch().getSpectators().size()))
							.replace("{arena}", profile.getMatch().getArena().getName())));
		}

		return lines;
	}

	@Override
	public void addSpectator(Player spectator, Player target) {
		super.addSpectator(spectator, target);

		ChatColor firstColor;
		ChatColor secondColor;

		if (participantA.containsPlayer(target.getUniqueId())) {
			firstColor = ChatColor.GREEN;
			secondColor = ChatColor.RED;
		} else {
			firstColor = ChatColor.RED;
			secondColor = ChatColor.GREEN;
		}

		if (ranked) {
			new MessageFormat(Locale.MATCH_START_SPECTATING_RANKED.format(Profile.get(spectator.getUniqueId()).getLocale()))
				.add("{first_color}", firstColor.toString())
				.add("{participant_a}", participantA.getConjoinedNames())
				.add("{participant_a_elo}", String.valueOf(participantA.getLeader().getElo()))
				.add("{second_color}", secondColor.toString())
				.add("{participant_b}", participantB.getConjoinedNames())
				.add("{participant_b_elo}", String.valueOf(participantB.getLeader().getElo()))
				.send(spectator);
		} else {
			new MessageFormat(Locale.MATCH_START_SPECTATING.format(Profile.get(spectator.getUniqueId()).getLocale()))
				.add("{first_color}", firstColor.toString())
				.add("{participant_a}", participantA.getConjoinedNames())
				.add("{second_color}", secondColor.toString())
				.add("{participant_b}", participantB.getConjoinedNames())
 				.send(spectator);
		}
	}

	@Override
	public List<BaseComponent[]> generateEndComponents(Player player) {
		List<BaseComponent[]> componentsList = new ArrayList<>();
		Profile profile = Profile.get(player.getUniqueId());

		for (String line : Locale.MATCH_END_DETAILS.getStringList(profile.getLocale())) {
			if (line.equalsIgnoreCase("%INVENTORIES%")) {

				BaseComponent[] winners = generateInventoriesComponents(
					new MessageFormat(Locale.MATCH_END_WINNER_INVENTORY.format(profile.getLocale()))
						.add("{context}", participantA.getPlayers().size() == 1 ? "" : "s")
						.toString(), winningParticipant);

				BaseComponent[] losers = generateInventoriesComponents(
					new MessageFormat(Locale.MATCH_END_LOSER_INVENTORY.format(profile.getLocale()))
						.add("{context}", participantB.getPlayers().size() > 1 ? "s" : "").toString(), losingParticipant);


				if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1) {
					ChatComponentBuilder builder = new ChatComponentBuilder("");

					for (BaseComponent component : winners) {
						builder.append((TextComponent) component);
					}

					builder.append(new ChatComponentBuilder("&7 - ").create());

					for (BaseComponent component : losers) {
						builder.append((TextComponent) component);
					}

					componentsList.add(builder.create());
				} else {
					componentsList.add(winners);
					componentsList.add(losers);
				}

				continue;
			}

			if (line.equalsIgnoreCase("%ELO_CHANGES%")) {
				if (participantA.getPlayers().size() == 1 && participantB.getPlayers().size() == 1 && ranked) {
					List<String> sectionLines = new MessageFormat(Locale.MATCH_ELO_CHANGES.getStringList(profile.getLocale()))
						.add("{winning_name}", winningParticipant.getConjoinedNames())
						.add("{winning_elo_mod}", String.valueOf(winningParticipant.getLeader().getEloMod()))
						.add("{winning_elo_mod_elo}",
							String.valueOf((winningParticipant.getLeader().getElo() + winningParticipant.getLeader().getEloMod())))
						.add("{losser_name}", losingParticipant.getConjoinedNames())
						.add("{losser_elo_mod}", String.valueOf(losingParticipant.getLeader().getEloMod()))
						.add("{losser_elo_mod_elo}",
							String.valueOf((losingParticipant.getLeader().getElo() - winningParticipant.getLeader().getEloMod())))
						.toList();


					for (String sectionLine : sectionLines) {
						componentsList.add(new ChatComponentBuilder("").parse(sectionLine).create());
					}
				}

				continue;
			}

			componentsList.add(new ChatComponentBuilder("").parse(line).create());
		}

		return componentsList;
	}

}
