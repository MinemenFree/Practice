package rip.crystal.practice.match;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.knockback.Knockback;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.match.events.MatchEndEvent;
import rip.crystal.practice.match.events.MatchStartEvent;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.impl.BasicTeamRoundMatch;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.match.task.*;
import rip.crystal.practice.player.cosmetics.impl.killeffects.KillEffectType;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.meta.ProfileKitData;
import rip.crystal.practice.player.profile.participant.GamePlayer;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.profile.visibility.VisibilityLogic;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.utilities.*;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.chat.ChatHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Getter
public abstract class Match {

	@Getter protected static List<Match> matches = new ArrayList<>();

	private final UUID matchId = UUID.randomUUID();
	Map<String, Integer> killstreak;
	/*private final Queue queue;
	protected final Kit kit;
	protected final Arena arena;
	protected final boolean ranked;
	@Setter protected MatchState state = MatchState.STARTING_ROUND;
	protected final List<MatchSnapshot> snapshots;
	protected final List<UUID> spectators;
	protected final List<Item> droppedItems;
	private final List<Location> placedBlocks;
	private final List<BlockState> changedBlocks;*/

	private Queue queue;
	protected Kit kit;
	protected Arena arena;
	protected boolean ranked;
	@Setter protected MatchState state = MatchState.STARTING_ROUND;
	protected List<MatchSnapshot> snapshots;
	protected List<UUID> spectators;
	protected List<Item> droppedItems;
	private List<Location> placedBlocks;
	private List<BlockState> changedBlocks;

	protected long timeData;
	protected MatchLogicTask logicTask;

	private boolean hasBed = true;

	public Match(Queue queue, Kit kit, Arena arena, boolean ranked) {
		this.queue = queue;
		this.kit = kit;
		this.arena = arena;
		this.ranked = ranked;
		this.snapshots = new ArrayList<>();
		this.spectators = new ArrayList<>();
		this.droppedItems = new ArrayList<>();
		this.placedBlocks = new ArrayList<>();
		this.changedBlocks = new ArrayList<>();

		matches.add(this);
	}

	public void destroyBed() {
		this.hasBed = false;
	}

	public boolean HasBed() {
		return this.hasBed;
	}

	public void setupPlayer(Player player) {
		// Set the player as alive
		MatchGamePlayer gamePlayer = getGamePlayer(player);
		gamePlayer.setDead(false);

		// If the player disconnected, skip any operations for them
		if (gamePlayer.isDisconnected()) return;

		Profile profile = Profile.get(player.getUniqueId());

		// Reset the player's inventory
		PlayerUtil.reset(player);

		// Deny movement if the kit is sumo , bridge or bed fight
		if (getKit().getGameRules().isSumo() || getKit().getGameRules().isBridge() || getKit().getGameRules().isBedFight()) {
			PlayerUtil.denyMovement(player);
		}

		// Set the player's max damage ticks and knockback
		player.setMaximumNoDamageTicks(getKit().getGameRules().getHitDelay());

		Knockback.getKnockbackProfiler().setKnockback(player.getPlayer(), getKit().getGameRules().getKbProfile());

		// If the player has no kits, apply the default kit, otherwise
		// give the player a list of kit books to choose from
		if (!getKit().getGameRules().isSumo()) {
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
	}

	public void start() {
		// Set state
		state = MatchState.STARTING_ROUND;

		// Start logic task
		logicTask = new MatchLogicTask(this);
		logicTask.runTaskTimer(cPractice.get(), 0L, 20L);

		// Set arena as active
		arena.setActive(true);

		// Send arena message
		if (getArena().getAuthor() != null && !getArena().getAuthor().isEmpty()) {
			sendMessage(Locale.MATCH_PLAYING_ARENA_AUTHOR, new MessageFormat()
					.add("{arena_name}", arena.getName())
					.add("{author}", arena.getAuthor()));
		} else
			sendMessage(Locale.MATCH_PLAYING_ARENA_NO_AUTHOR, new MessageFormat().add("{arena_name}", arena.getName()));

		// Setup players
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					Profile profile = Profile.get(player.getUniqueId());
					profile.setState(ProfileState.FIGHTING);
					profile.setMatch(this);

					TaskUtil.run(() -> setupPlayer(player));
					if (getKit().getGameRules().isShowHealth()) {
						for (GameParticipant<MatchGamePlayer> gameParticipantOther : getParticipants()) {
							for (MatchGamePlayer gamePlayerOther : gameParticipantOther.getPlayers()) {
								Player other = gamePlayerOther.getPlayer();
								Scoreboard scoreboard = player.getScoreboard();
								Objective objective = scoreboard.getObjective(DisplaySlot.BELOW_NAME);

								if (objective == null) {
									objective = scoreboard.registerNewObjective("showhealth", "health");
								}

								objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
								objective.setDisplayName(ChatColor.RED + StringEscapeUtils.unescapeJava("\u2764"));
								objective.getScore(other.getName()).setScore((int) Math.floor(other.getHealth() / 2));
							}
						}
					}

				}
			}
		}

		// Handle player visibility
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					VisibilityLogic.handle(player);
				}
			}
		}

		timeData = System.currentTimeMillis();
		new MatchStartEvent(this).call();
	}


	public void end() {
		new MatchEndEvent(this).call();

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						player.setFireTicks(0);
						player.updateInventory();

						Profile profile = Profile.get(player.getUniqueId());
						profile.setFishHit(0);
						profile.setState(ProfileState.LOBBY);
						profile.setMatch(null);
						profile.setEnderpearlCooldown(new Cooldown(0));
						profile.setSelectedKit(null);

						/*
							Partner Items
						 */

						profile.getPartneritem().applyCooldown(player, 0);
						profile.getAntitrapper().cooldownRemove(player);
						profile.getBeacom().cooldownRemove(player);
						profile.getCookie().cooldownRemove(player);
						profile.getEffectdisabler().cooldownRemove(player);
						profile.getGuardianangel().cooldownRemove(player);
						profile.getNinjastar().cooldownRemove(player);
						profile.getPocketbard().cooldownRemove(player);
						profile.getRocket().cooldownRemove(player);
						profile.getScrammbler().cooldownRemove(player);
						profile.getStrength().cooldownRemove(player);
						profile.getSwapperaxe().cooldownRemove(player);
						profile.getSwitcher().cooldownRemove(player);
						profile.getTankingot().cooldownRemove(player);
						profile.getTimewarp().cooldownRemove(player);

						if (getKit().getGameRules().isShowHealth()) {
							Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

							if (objective != null) objective.unregister();
						}
					}
				}
			}
		}

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						VisibilityLogic.handle(player);
						Hotbar.giveHotbarItems(player);
						cPractice.get().getEssentials().teleportToSpawn(player);
					}
				}
			}
		}

		for (Player player : getSpectatorsAsPlayers()) {
			removeSpectator(player);
		}

		droppedItems.forEach(Entity::remove);
		new MatchResetTask(this).runTask(cPractice.get());
		matches.remove(this);
		logicTask.cancel();
	}

	public abstract boolean canEndMatch();

	public void setEffects() {
		TaskUtil.run(() ->
				getParticipants().forEach(gameParticipant ->
						gameParticipant.getPlayers().forEach(gamePlayer -> {
							Player player = gamePlayer.getPlayer();
							if (player != null) {
								for (PotionEffect effect : kit.getGameRules().getEffects()) {
									player.addPotionEffect(effect);
								}
							}
						})));
	}

	public void onRoundStart() {
		// Reset snapshots
		snapshots.clear();

		// Reset each game participant
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			gameParticipant.reset();
			gameParticipant.getPlayers().forEach(gamePlayer -> {
				// Allow movement if the kit is sumo , bridge or bed fight
				if (getKit().getGameRules().isSumo() || getKit().getGameRules().isBridge() || getKit().getGameRules().isBedFight())
					PlayerUtil.allowMovement(gamePlayer.getPlayer());
			});
		}

		TaskUtil.run(() ->
				getParticipants().forEach(gameParticipant ->
						gameParticipant.getPlayers().forEach(gamePlayer -> {
							Player player = gamePlayer.getPlayer();
							if (player != null) {
								for (PotionEffect effect : kit.getGameRules().getEffects()) {
									player.addPotionEffect(effect);
								}
							}
						})));

		// Set time data
		timeData = System.currentTimeMillis();
	}

	public abstract boolean canStartRound();

	public void onRoundEnd() {
		timeData = System.currentTimeMillis() - timeData;
		// Snapshot alive players' inventories
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						if (!gamePlayer.isDead()) {
							MatchSnapshot snapshot = new MatchSnapshot(player, false);
							snapshot.setPotionsThrown(gamePlayer.getPotionsThrown());
							snapshot.setPotionsMissed(gamePlayer.getPotionsMissed());
							snapshot.setLongestCombo(gamePlayer.getLongestCombo());
							snapshot.setTotalHits(gamePlayer.getHits());

							snapshots.add(snapshot);
						}
					}
				}
			}
		}

		if (this instanceof BasicTeamMatch) {
			BasicTeamMatch match = (BasicTeamMatch) this;
			// Set opponents in snapshots if solo
			if (match.getParticipantA().getPlayers().size() == 1 && match.getParticipantB().getPlayers().size() == 1) {
				for (MatchSnapshot snapshot : snapshots) {
					if (snapshot.getUuid().equals(match.getParticipantA().getLeader().getUuid())) {
						snapshot.setOpponent(match.getParticipantB().getLeader().getUuid());
					} else if (snapshot.getUuid().equals(match.getParticipantB().getLeader().getUuid())) {
						snapshot.setOpponent(match.getParticipantA().getLeader().getUuid());
					}
				}
			}
		}

		// Make all snapshots available
		for (MatchSnapshot snapshot : snapshots) {
			snapshot.setCreatedAt(System.currentTimeMillis());
			MatchSnapshot.getSnapshots().put(snapshot.getUuid(), snapshot);
		}


		// Send ending messages to game participants
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						for (BaseComponent[] components : generateEndComponents(player)) {
							player.spigot().sendMessage(components);
						}
					}
				}
			}
		}

		// Send ending messages to spectators
		for (Player player : getSpectatorsAsPlayers()) {
			for (BaseComponent[] components : generateEndComponents(player)) {
				player.spigot().sendMessage(components);
			}

			removeSpectator(player);
		}
	}

	public abstract boolean canEndRound();

	public void onDisconnect(Player dead) {
		if (getKit().getGameRules().isBridge()) {
			BasicTeamRoundMatch match = (BasicTeamRoundMatch) this;
			if (match.getParticipantA().containsPlayer(dead.getUniqueId())) match.setWinningParticipant(match.getParticipantB());
			else match.setWinningParticipant(match.getParticipantA());
			end();
			return;
		}
		if (getKit().getGameRules().isBedFight()) {
			BasicTeamRoundMatch match = (BasicTeamRoundMatch) this;
			if (match.getParticipantA().containsPlayer(dead.getUniqueId())) match.setWinningParticipant(match.getParticipantB());
			else match.setWinningParticipant(match.getParticipantA());
			end();
			return;
		}

		// Don't continue if the match is already ending
		if (!(state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND)) return;

		MatchGamePlayer deadGamePlayer = getGamePlayer(dead);

		if (deadGamePlayer != null) {
			deadGamePlayer.setDisconnected(true);

			if (!deadGamePlayer.isDead()) onDeath(dead);
		}
	}

	public void onDeath(Player dead) {
		// Don't continue if the match is already ending
		if (!(state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND)) return;

		MatchGamePlayer deadGamePlayer = getGamePlayer(dead);

		// Don't continue if the player is already dead
		if (deadGamePlayer.isDead()) return;

		// Get killer
		Player killer = PlayerUtil.getLastAttacker(dead);

		// Set player as dead
		if (getKit().getGameRules().isBridge()) {
			getParticipant(dead).getPlayers().forEach(gamePlayer -> gamePlayer.setDead(false));
		} else if (getKit().getGameRules().isBedFight()) {
			getParticipant(dead).getPlayers().forEach(gamePlayer -> gamePlayer.setDead(false));
		} else deadGamePlayer.setDead(true);

		Profile profile = Profile.get(dead.getUniqueId());

		if(killer != null) {
			Profile winner = Profile.get(killer.getUniqueId());

			/*
				Death Animations
		 	*/

			KillEffectType effect = winner.getKillEffectType();
			if (effect != null && effect.getCallable() != null) {
				for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
					for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
						effect.getCallable().call(dead.getLocation().clone().add(0.0, 1.0, 0.0));
					}
				}
			}

			// Add coins to winner
			winner.addCoins(10);
		}

		if (killer != null) { // If killer isn't null then add a kill to the player.
			MatchGamePlayer matchGamePlayer = getGamePlayer(killer);
			matchGamePlayer.incrementKills();
		}

		dead.setVelocity(new Vector());

		MatchSnapshot snapshot = new MatchSnapshot(dead, true); // Save snapshot of player. (Match information)
		snapshot.setPotionsMissed(deadGamePlayer.getPotionsMissed());
		snapshot.setPotionsThrown(deadGamePlayer.getPotionsThrown());
		snapshot.setLongestCombo(deadGamePlayer.getLongestCombo());
		snapshot.setTotalHits(deadGamePlayer.getHits());

		snapshots.add(snapshot);

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						if (!getKit().getGameRules().isSumo() && !getKit().getGameRules().isBridge() && !getKit().getGameRules().isBedFight()) {
							VisibilityLogic.handle(player, dead);
						}
						if (!getKit().getGameRules().isBridge() || !getKit().getGameRules().isBedFight()) {
							sendDeathMessage(player, dead, killer);
						}
					}
				}
			}
		}

		// Handle visibility for spectators
		// Send death message
		for (Player player : getSpectatorsAsPlayers()) {
			if (!getKit().getGameRules().isSumo() && !getKit().getGameRules().isBridge() && !getKit().getGameRules().isBedFight()) {
				VisibilityLogic.handle(player, dead);
			}
			if (!getKit().getGameRules().isBridge() || !getKit().getGameRules().isBedFight()) {
				sendDeathMessage(player, dead, killer);
			}
		}

		if (canEndRound()) {
			state = MatchState.ENDING_ROUND;
			onRoundEnd();

			if (canEndMatch()) {
				state = MatchState.ENDING_MATCH;
				//add shit here
			}
			logicTask.setNextAction(4);
		} else {
			if (!(this instanceof BasicTeamRoundMatch)) {
				TaskUtil.runLater(() -> {
					PlayerUtil.reset(dead);
					addSpectator(dead, killer);
				}, 10L);
			} else {
				if (getKit().getGameRules().isBridge()) {
					BasicTeamRoundMatch teamRoundMatch = (BasicTeamRoundMatch) this;

					Location spawn = teamRoundMatch.getParticipantA().containsPlayer(dead.getUniqueId()) ?
							teamRoundMatch.getArena().getSpawnA() : teamRoundMatch.getArena().getSpawnB();
					dead.teleport(spawn.add(0, 2, 0));
					TaskUtil.runLater(() -> {
						PlayerUtil.reset(dead);
						if (profile.getSelectedKit() == null) {
							dead.getInventory().setContents(getKit().getKitLoadout().getContents());
						} else {
							dead.getInventory().setContents(profile.getSelectedKit().getContents());
						}
						KitUtils.giveBridgeKit(dead);
					}, 5L);
				}
				if (getKit().getGameRules().isBedFight()) {
					BasicTeamRoundMatch teamRoundMatch = (BasicTeamRoundMatch) this;

					Location spawn = teamRoundMatch.getParticipantA().containsPlayer(dead.getUniqueId()) ?
							teamRoundMatch.getArena().getSpawnA() : teamRoundMatch.getArena().getSpawnB();
					dead.teleport(spawn.add(0, 2, 0));
					TaskUtil.runLater(() -> {
						PlayerUtil.reset(dead);
						if (profile.getSelectedKit() == null) {
							dead.getInventory().setContents(getKit().getKitLoadout().getContents());
						} else {
							dead.getInventory().setContents(profile.getSelectedKit().getContents());
						}
						KitUtils.giveBedFightKit(dead);
					}, 5L);
				}
			}
		}

		if (Tournament.getTournament() != null) {
			if (Tournament.getTournament().getPlayers().contains(dead.getUniqueId())) {
				profile.setInTournament(false);
			}
		}
	}

	public abstract boolean isOnSameTeam(Player first, Player second);

	public abstract List<GameParticipant<MatchGamePlayer>> getParticipants();

	public GameParticipant<MatchGamePlayer> getParticipant(Player player) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			if (gameParticipant.containsPlayer(player.getUniqueId())) {
				return gameParticipant;
			}
		}

		return null;
	}

	public MatchGamePlayer getGamePlayer(Player player) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (gamePlayer.getUuid().equals(player.getUniqueId())) {
					return gamePlayer;
				}
			}
		}

		return null;
	}

	public abstract ChatColor getRelationColor(Player viewer, Player target);

	public abstract List<String> getScoreboardLines(Player player);

	public void addSpectator(Player spectator, Player target) {
		Profile profile = Profile.get(spectator.getUniqueId());

		profile.setMatch(this);

		if (profile.getParty() == null) spectator.teleport(target.getLocation().clone().add(0, 2, 0));

		if (profile.getState() != ProfileState.STAFF_MODE) {
			spectators.add(spectator.getUniqueId());
			profile.setState(ProfileState.SPECTATING);
			Hotbar.giveHotbarItems(spectator);
			spectator.updateInventory();
		}

		VisibilityLogic.handle(spectator);

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (GamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player bukkitPlayer = gamePlayer.getPlayer();

					if (bukkitPlayer != null && !(profile.getState() == ProfileState.STAFF_MODE)) {
						VisibilityLogic.handle(bukkitPlayer);
						new MessageFormat(Locale.MATCH_NOW_SPECTATING.format(Profile.get(bukkitPlayer.getUniqueId()).getLocale()))
								.add("{spectator_name}", spectator.getName())
								.send(bukkitPlayer);
					}
				}
			}
		}
		spectator.spigot().setCollidesWithEntities(false);
		TaskUtil.runLater(() -> {
			spectator.setGameMode(GameMode.CREATIVE);
			spectator.setAllowFlight(true);
			spectator.setFlying(true);
		}, 5L);
	}

	public void removeSpectator(Player spectator) {
		spectators.remove(spectator.getUniqueId());

		Profile profile = Profile.get(spectator.getUniqueId());
		profile.setState(ProfileState.LOBBY);
		profile.setMatch(null);

		PlayerUtil.reset(spectator);
		Hotbar.giveHotbarItems(spectator);
		cPractice.get().getEssentials().teleportToSpawn(spectator);
		VisibilityLogic.handle(spectator);

		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player bukkitPlayer = gamePlayer.getPlayer();

					if (bukkitPlayer != null && !(profile.getState() == ProfileState.STAFF_MODE)) {
						VisibilityLogic.handle(bukkitPlayer);

						if (state != MatchState.ENDING_MATCH) {
							new MessageFormat(Locale.MATCH_NO_LONGER_SPECTATING.format(Profile.get(bukkitPlayer.getUniqueId()).getLocale()))
									.add("{spectator_name}", spectator.getName())
									.send(bukkitPlayer);
						}
					}
				}
			}
		}
	}

	public String getDuration() {
		if (state == MatchState.STARTING_ROUND) return "Starting";
		if (state == MatchState.ENDING_MATCH) return "Ending";
		else return TimeUtil.millisToTimer(System.currentTimeMillis() - timeData);
	}

	public void sendMessage(String message) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			gameParticipant.sendMessage(CC.translate(message));
		}

		for (Player player : getSpectatorsAsPlayers()) {
			player.sendMessage(CC.translate(message));
		}
	}

	public void sendMessage(Locale lang, MessageFormat messageFormat) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			gameParticipant.sendMessage(lang, messageFormat);
		}

		for (Player player : getSpectatorsAsPlayers()) {
			messageFormat.setMessage(lang.format(Profile.get(player.getUniqueId()).getLocale()));
			messageFormat.send(player);
		}
	}

	public void sendSound(Sound sound, float volume, float pitch) {
		for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
			gameParticipant.sendSound(sound, volume, pitch);
		}

		for (Player player : getSpectatorsAsPlayers()) {
			player.playSound(player.getLocation(), sound, volume, pitch);
		}
	}

	protected List<Player> getSpectatorsAsPlayers() {
		List<Player> players = new ArrayList<>();

		for (UUID uuid : spectators) {
			Player player = Bukkit.getPlayer(uuid);

			if (player != null) players.add(player);
		}

		return players;
	}

	public abstract List<BaseComponent[]> generateEndComponents(Player player);

	public void sendDeathMessage(Player player, Player dead, Player killer) {
		String deathMessage;
		Profile profile = Profile.get(player.getUniqueId());

		if (killer == null) {
			deathMessage = new MessageFormat(Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
					.add("{dead_name}", getRelationColor(player, dead) + dead.getName())
					.toString();
		} else {
			deathMessage = new MessageFormat(Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
					.add("{dead_name}", getRelationColor(player, dead) + dead.getName())
					.add("{killer_name}", getRelationColor(player, killer) + killer.getName())
					.toString();
		}

		player.sendMessage(deathMessage);
	}

	public void sendDeathMessage(Player dead, Player killer) {
		String deathMessage;
		// Send death message
		for (GameParticipant<MatchGamePlayer> gameParticipant : this.getParticipants()) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player other = gamePlayer.getPlayer();
					Profile profile = Profile.get(other.getUniqueId());

					if (killer == null) {
						deathMessage = new MessageFormat(Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
								.add("{dead_name}", getRelationColor(other, dead) + dead.getName())
								.toString();
					} else {
						deathMessage = new MessageFormat(Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
								.add("{dead_name}", getRelationColor(other, dead) + dead.getName())
								.add("{killer_name}", getRelationColor(other, killer) + killer.getName())
								.toString();
					}
					other.sendMessage(deathMessage);
				}
			}
		}

		// Handle visibility for spectators
		// Send death message
		for (Player other : this.getSpectatorsAsPlayers()) {
			Profile profile = Profile.get(other.getUniqueId());
			if (killer == null) {
				deathMessage = new MessageFormat(Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
						.add("{dead_name}", getRelationColor(other, dead) + dead.getName())
						.toString();
			} else {
				deathMessage = new MessageFormat(Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
						.add("{dead_name}", getRelationColor(other, dead) + dead.getName())
						.add("{killer_name}", getRelationColor(other, killer) + killer.getName())
						.toString();
			}
			other.sendMessage(deathMessage);
		}
	}

	public static void init() {
		new MatchPearlCooldownTask().runTaskTimerAsynchronously(cPractice.get(), 2L, 2L);
		new MatchSnapshotCleanupTask().runTaskTimerAsynchronously(cPractice.get(), 20L * 5, 20L * 5);
		cPractice.get().getServer().getScheduler().runTaskTimer(cPractice.get(), new MatchLiquidTask(), 20L, 8L);
	}

	public static void cleanup() {
		for (Match match : matches) {
			match.getPlacedBlocks().forEach(location -> location.getBlock().setType(Material.AIR));
			match.getChangedBlocks().forEach((blockState) -> blockState.getLocation().getBlock().setType(blockState.getType()));
			match.getDroppedItems().forEach(Entity::remove);
		}
	}

	public static int getInFightsCount(Queue queue) {
		int i = 0;

		for (Match match : matches) {
			if (match.getQueue() != null &&
					(match.getState() == MatchState.STARTING_ROUND || match.getState() == MatchState.PLAYING_ROUND)) {
				if (match.getQueue().equals(queue)) {
					for (GameParticipant<? extends GamePlayer> gameParticipant : match.getParticipants()) {
						i += gameParticipant.getPlayers().size();
					}
				}
			}
		}

		return i;
	}

	public static BaseComponent[] generateInventoriesComponents(String prefix, GameParticipant<MatchGamePlayer> participant) {
		return generateInventoriesComponents(prefix, Collections.singletonList(participant));
	}

	public static BaseComponent[] generateInventoriesComponents(String prefix, List<GameParticipant<MatchGamePlayer>> participants) {
		ChatComponentBuilder builder = new ChatComponentBuilder(prefix);

		int totalPlayers = 0;
		int processedPlayers = 0;

		for (GameParticipant<MatchGamePlayer> gameParticipant : participants) {
			totalPlayers += gameParticipant.getPlayers().size();
		}

		for (GameParticipant<MatchGamePlayer> gameParticipant : participants) {
			for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
				processedPlayers++;

				ChatComponentBuilder current = new ChatComponentBuilder(
						CC.translate(new MessageFormat(Locale.MATCH_CLICK_TO_VIEW_NAME
								.format(Profile.get(gamePlayer.getUuid()).getLocale()))
								.add("{name}", gamePlayer.getUsername()).toString()))
						.attachToEachPart(ChatHelper.hover(CC.translate(
								new MessageFormat(Locale.MATCH_CLICK_TO_VIEW_HOVER
										.format(Profile.get(gamePlayer.getUuid()).getLocale()))
										.add("{name}", gamePlayer.getUsername()).toString())))
						.attachToEachPart(ChatHelper.click("/viewinv " + gamePlayer.getUuid().toString()));

				builder.append(current.create());

				if (processedPlayers != totalPlayers) {
					builder.append(", ");
					builder.getCurrent().setClickEvent(null);
					builder.getCurrent().setHoverEvent(null);
				}
			}
		}

		return builder.create();
	}


	private PacketContainer createLightningPacket(Location location) {
		final PacketContainer lightningPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_WEATHER);
		lightningPacket.getModifier().writeDefaults();
		lightningPacket.getIntegers().write(0, 128);
		lightningPacket.getIntegers().write(4, 1);
		lightningPacket.getIntegers().write(1, (int)(location.getX() * 32.0));
		lightningPacket.getIntegers().write(2, ((int)(location.getY() * 32.0)));
		lightningPacket.getIntegers().write(3, ((int)(location.getZ() * 32.0)));
		return lightningPacket;
	}

	private void sendLightningPacket(final Player target, final PacketContainer packet) {
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(target, packet);
		}
		catch (InvocationTargetException ex) {
			//empty catch
		}
	}

}
