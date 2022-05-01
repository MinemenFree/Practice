package rip.crystal.practice.match.listeners;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderPearl;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.knockback.Knockback;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchState;
import rip.crystal.practice.match.events.MatchEndEvent;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;

import java.util.Iterator;
import java.util.List;

public class MatchListener implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if(profile.getState() == ProfileState.SPECTATING) {
			event.setCancelled(true);
			return;
		}

		if (profile.getState() == ProfileState.FIGHTING) {
			if (profile.getMatch().getGamePlayer(event.getPlayer()).isDead()) {
				event.setCancelled(true);
				return;
			}

			if (event.getItem().getItemStack().getType().name().contains("BOOK")) {
				event.setCancelled(true);
				return;
			}

			Iterator<Item> itemIterator = profile.getMatch().getDroppedItems().iterator();

			while (itemIterator.hasNext()) {
				Item item = itemIterator.next();

				if (item.equals(event.getItem())) {
					itemIterator.remove();
					return;
				}
			}

			event.setCancelled(true);
		}
	}

	//@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (event.getItemDrop().getItemStack().getType() == Material.BOOK ||
				event.getItemDrop().getItemStack().getType() == Material.ENCHANTED_BOOK) {
			event.setCancelled(true);
			return;
		}

		if (profile.getState() == ProfileState.FIGHTING) {
			if (event.getItemDrop().getItemStack().getType() == Material.GLASS_BOTTLE) {
				//event.getPlayer().sendMessage(CC.translate("&cYou cannot drop your sword."));
				event.getItemDrop().remove();
				return;
			}

			if (event.getItemDrop().getItemStack().getType()== Material.DIAMOND_SWORD) {
				event.getPlayer().sendMessage(CC.translate("&cYou cannot drop your sword."));
				event.setCancelled(true);
				return;
			}

			if (profile.getMatch().getKit().getGameRules().isBridge()) {
				event.setCancelled(true);
				return;
			}

			//if (event.getPlayer().getItemInHand().getType().name().endsWith("_SWORD")) {

//			ProtocolManager manager = ProtocolLibrary.getProtocolManager();
//			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//				Profile onlineProfile = Profile.get(onlinePlayer.getUniqueId());
//				if (onlineProfile.getState() == ProfileState.FIGHTING) {
//					if (profile.getMatch().getParticipant(onlinePlayer) == null) {
//						System.out.println("TEST");
//						PacketContainer destroyEntity = new PacketContainer(ENTITY_DESTROY);
//						destroyEntity.getIntegerArrays().write(0, new int[] { event.getItemDrop().getEntityId() });
//						try {
//							manager.sendServerPacket(onlinePlayer, destroyEntity);
//						} catch (InvocationTargetException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}

			profile.getMatch().getDroppedItems().add(event.getItemDrop());
		}
		else if (profile.getState() == ProfileState.SPECTATING) event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		event.getEntity().spigot().respawn();
		event.setDeathMessage(null);

		Profile profile = Profile.get(event.getEntity().getUniqueId());

		if (profile.getState() == ProfileState.FIGHTING) {
			Match match = profile.getMatch();
			if (match.getKit().getGameRules().isBridge()) {
				Player killer = PlayerUtil.getLastAttacker(event.getEntity());
				match.sendDeathMessage(event.getEntity(), killer);
			}
			Player killer = PlayerUtil.getLastAttacker(event.getEntity());
			if(killer == null) {
				return;
			}
			Profile killerProfile = Profile.get(killer.getUniqueId());
			killerProfile.getKitData().get(match.getKit()).incrementStreak();

			if(profile.getKitData().get(match.getKit()).hasStreak()) {
				profile.getKitData().get(match.getKit()).resetStreak();
			}

			if (match.getKit().getGameRules().isBridge()) event.getDrops().clear();

			if (cPractice.get().getMainConfig().getBoolean("MATCH.DROP_ITEMS_ON_DEATH")) {
				TaskUtil.run(() -> {
					List<Item> entities = Lists.newArrayList();

					event.getDrops().forEach(itemStack -> {
						if (!(itemStack.getType() == Material.BOOK || itemStack.getType() == Material.ENCHANTED_BOOK)) {
							entities.add(event.getEntity().getLocation().getWorld().dropItemNaturally(event.getEntity().getLocation(), itemStack));
						}
					});

					event.getDrops().clear();

					profile.getMatch().getDroppedItems().addAll(entities);
				});
			} else
				event.getDrops().clear();

			profile.getMatch().onDeath(event.getEntity());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onLow(final PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		Match match = profile.getMatch();
		if (profile.getState() == ProfileState.FIGHTING) {
			if (match.getKit().getGameRules().isBridge()) {
				if (player.getLocation().getBlockY() <= 30) {
					Player killer = PlayerUtil.getLastAttacker(event.getPlayer());
					match.sendDeathMessage(event.getPlayer(), killer);
					profile.getMatch().onDeath(player);
				}
			}
			if (match.getKit().getGameRules().isBuild()) {
				if (player.getLocation().getBlockY() <= 30) {
					profile.getMatch().onDeath(player);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		event.setRespawnLocation(event.getPlayer().getLocation());
	}

	@EventHandler(ignoreCancelled = true)
	public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player shooter = (Player) event.getEntity().getShooter();
			Profile profile = Profile.get(shooter.getUniqueId());

			if (profile.getState() == ProfileState.FIGHTING) {
				Match match = profile.getMatch();

				if (match.getState() == MatchState.STARTING_ROUND) event.setCancelled(true);
				else if (match.getState() == MatchState.PLAYING_ROUND) {

					if (event.getEntity() instanceof Arrow) {
						TaskUtil.runLater(() -> shooter.getInventory().addItem(new ItemStack(Material.ARROW, 1)), 20 * 3);
						return;
					}

					if (event.getEntity() instanceof ThrownPotion) {
						if (cPractice.get().getMainConfig().getBoolean("MATCH.FAST_POTION")) {
							Projectile projectile = event.getEntity();

							if (shooter.isSprinting()) {
								Vector velocity = projectile.getVelocity();

								velocity.setY(-1.1);
								projectile.setVelocity(velocity);
							}
						}
						match.getGamePlayer(shooter).incrementPotionsThrown();
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Arrow) {
			if (event.getEntity().getShooter() instanceof Player) {
				Player shooter = (Player) event.getEntity().getShooter();
				Profile shooterData = Profile.get(shooter.getUniqueId());

				if (shooterData.getState() == ProfileState.FIGHTING) {
					shooterData.getMatch().getGamePlayer(shooter).handleHit();
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPotionSplashEvent(PotionSplashEvent event) {
		if (event.getPotion().getShooter() instanceof Player) {
			Player shooter = (Player) event.getPotion().getShooter();
			Profile shooterData = Profile.get(shooter.getUniqueId());

			if (shooterData.getState() == ProfileState.FIGHTING &&
					shooterData.getMatch().getState() == MatchState.PLAYING_ROUND) {

//				event.getEntity().

//				for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//					Profile profile = Profile.get(onlinePlayer.getUniqueId());
//					if (profile.getMatch() != null && profile.getMatch().getParticipant(shooter) != null) {
//						cPractice.get().getEntityHider().hideEntity(onlinePlayer, event.getEntity());
//						System.out.println("asd");
//					}
//				}

				if (event.getIntensity(shooter) <= 0.5D) {
					shooterData.getMatch().getGamePlayer(shooter).incrementPotionsMissed();
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Profile profile = Profile.get(player.getUniqueId());
		if (profile.getState() == ProfileState.SPECTATING) event.setCancelled(true);
	}

	/*@EventHandler
	public void onInventoryClick(InventoryInteractEvent event){
		Player player = (Player) event.getWhoClicked();
		Profile profile = Profile.get(player.getUniqueId());
		if (profile.getState() == ProfileState.SPECTATING) event.setCancelled(true);
	}*/

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			Player healed = (Player) event.getEntity();
			Profile healedProfile = Profile.get(healed.getUniqueId());

			if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
				if (healedProfile.getState() == ProfileState.FIGHTING && !healedProfile.getMatch().getKit().getGameRules().isHealthRegeneration()) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getState() == ProfileState.FIGHTING &&  profile.getMatch().getKit().getGameRules().isNofalldamage()) {
				if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
					event.setCancelled(true);
				}
			}

			if (profile.getState() == ProfileState.FIGHTING) {
				if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
					Match match = profile.getMatch();
					if (match.getKit().getGameRules().isBridge()) {
						Player killer = PlayerUtil.getLastAttacker(player);
						match.sendDeathMessage(player, killer);
					}

					if (match.getState() == MatchState.ENDING_MATCH) {
						event.setCancelled(true);
						return;
					}
					profile.getMatch().onDeath(player);
					return;
				}

				if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
					Match match = profile.getMatch();
					if (match.getKit().getGameRules().isBridge()) event.setCancelled(true);
				}

				if (profile.getMatch().getState() != MatchState.PLAYING_ROUND) {
					event.setCancelled(true);
					return;
				}

				if (profile.getMatch().getGamePlayer(player).isDead()) {
					event.setCancelled(true);
					return;
				}

				if (profile.getMatch().getKit().getGameRules().isSumo() || profile.getMatch().getKit().getGameRules().isSpleef()
						|| profile.getMatch().getKit().getGameRules().isBoxing()) {
					event.setDamage(0);
					player.setHealth(20.0);
					player.updateInventory();
				}
			}

		}
	}

	@EventHandler
	public void onEntityDamageByEntityLow(EntityDamageByEntityEvent event) {
		Player attacker;

		if (event.getDamager() instanceof Player) {
			attacker = (Player) event.getDamager();
		}
		else if (event.getDamager() instanceof Projectile) {
			if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
				attacker = (Player) ((Projectile) event.getDamager()).getShooter();
			}
			else {
				event.setCancelled(true);
				return;
			}
		}
		else {
			event.setCancelled(true);
			return;
		}

		if (attacker != null && event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();
			Profile damagedProfile = Profile.get(damaged.getUniqueId());
			Profile attackerProfile = Profile.get(attacker.getUniqueId());

			if (attackerProfile.getState() == ProfileState.SPECTATING || damagedProfile.getState() == ProfileState.SPECTATING) {
				event.setCancelled(true);
				return;
			}

			if (event.getDamager() instanceof CraftEnderPearl) {
				event.setCancelled(false);
				return;
			}

			if (damagedProfile.getState() == ProfileState.FIGHTING && attackerProfile.getState() == ProfileState.FIGHTING) {
				Match match = attackerProfile.getMatch();

				if (!damagedProfile.getMatch().getMatchId().equals(attackerProfile.getMatch().getMatchId())) {
					event.setCancelled(true);
					return;
				}

				if (match.getGamePlayer(damaged).isDead()) {
					event.setCancelled(true);
					return;
				}

				if (match.getGamePlayer(attacker).isDead()) {
					event.setCancelled(true);
					return;
				}

				if (match.isOnSameTeam(damaged, attacker)) {
					event.setCancelled(true);
					return;
				}

				attackerProfile.getMatch().getGamePlayer(attacker).handleHit();
				damagedProfile.getMatch().getGamePlayer(damaged).resetCombo();

				if (match.getKit().getGameRules().isBoxing()) {
					if (match.getGamePlayer(attacker).getHits() == cPractice.get().getMainConfig().getInteger("MATCH.BOXING_MAX_HITS")) {
						match.onDeath(damaged);
					}
				}

				if (event.getDamager() instanceof Arrow) {
					int range = (int) Math.ceil(event.getEntity().getLocation().distance(attacker.getLocation()));
					double health = Math.ceil(damaged.getHealth() - event.getFinalDamage()) / 2.0D;

					new MessageFormat(Locale.ARROW_DAMAGE_INDICATOR.format(attackerProfile.getLocale()))
							.add("{range}", String.valueOf(range))
							.add("{damaged_name}", damaged.getName())
							.add("{damaged_health}", String.valueOf(health))
							.add("{symbol}", StringEscapeUtils.unescapeJava("\u2764"))
							.send(attacker);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player victim = (Player) event.getEntity();
			Player attacker = null;

			if (event.getDamager() instanceof Player) {
				attacker = (Player) event.getDamager();
			} else if (event.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) event.getDamager();

				if (projectile.getShooter() instanceof Player) {
					attacker = (Player) projectile.getShooter();
				}
			}

			if (attacker != null) {
				PlayerUtil.setLastAttacker(victim, attacker);
//				Profile profile = Profile.get(victim.getUniqueId());
//				if (profile.getMatch() != null && profile.getMatch().getKit().getGameRules().isBridge()) {
//					TaskUtil.runLater(() -> victim.removeMetadata("lastAttacker", cPractice.get()), 20L * 15);
//				}
			}
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getState() == ProfileState.FIGHTING) {
				if(profile.getMatch().getKit().getGameRules().isAntiFood()) {
					if (event.getFoodLevel() < 20) {
						event.setFoodLevel(20);
						player.setSaturation(20.0f);
					}
				} else {
					event.setCancelled(false);
				}
//				else {
//					event.setCancelled(ThreadLocalRandom.current().nextInt(100) > 25);
//				}
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onMathEnd(MatchEndEvent event) {
		Match match = event.getMatch();
		match.getParticipants().forEach(matchGamePlayerGameParticipant -> {
			matchGamePlayerGameParticipant.getPlayers().forEach(player -> {
				if (player.getPlayer() == null) return;

				Knockback.getKnockbackProfiler().setKnockback(player.getPlayer(), "default");
				//Knockback.getKnockbackProfiler().setKnockback(player.getPlayer(), "default");
			});
		});

		Tournament<?> tournament = Tournament.getTournament();
		if(tournament == null) return;
		match.getParticipants().forEach(gameParticipant -> {
			if (gameParticipant.isAllDead()) {
				tournament.eliminatedTeam(gameParticipant);
			}
		});

		if (tournament.getMatches().contains(match)) {
			tournament.getMatches().remove(match);
			if (tournament.getMatches().isEmpty()) {
				if (tournament.getTeams().size() == 1) {
					tournament.end(tournament.getTeams().get(0));
				} else if(tournament.getTeams().isEmpty()) {
					tournament.end(null);
				} else {
					if (tournament.getMatches().isEmpty()) {
						tournament.nextRound();
					}
				}
			}
		}
	}
}