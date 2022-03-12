package rip.crystal.practice.match;

import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.material.TrapDoor;
import rip.crystal.practice.Locale;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.KitLoadout;
import rip.crystal.practice.game.knockback.Knockback;
import rip.crystal.practice.match.events.MatchEndEvent;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.impl.BasicTeamRoundMatch;
import rip.crystal.practice.match.menu.ViewInventoryMenu;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.hotbar.HotbarItem;
import rip.crystal.practice.player.profile.participant.GameParticipant;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.utilities.*;
import rip.crystal.practice.utilities.chat.CC;
import me.scalebound.pspigot.KnockbackProfile;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderPearl;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.SpigotConfig;

import java.util.*;
import java.util.regex.Matcher;

public class MatchListener implements Listener {

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		if (profile.getState() == ProfileState.FIGHTING) {
			if (profile.getMatch().getKit().getGameRules().isBridge()) {
				if (player.getLocation().getBlock().getType() == Material.ENDER_PORTAL ||
					player.getLocation().getBlock().getType() == Material.ENDER_PORTAL_FRAME) {
					if (LocationUtil.isTeamPortal(player)) return;
					BasicTeamRoundMatch match = (BasicTeamRoundMatch) profile.getMatch();
					if (match.getState() == MatchState.ENDING_ROUND || match.getState() == MatchState.ENDING_MATCH) return;
					match.getParticipants().forEach(gamePlayerGameParticipant ->
						gamePlayerGameParticipant.getPlayers().forEach(gamePlayer -> {
							Player other = gamePlayer.getPlayer();

							new MessageFormat(Locale.MATCH_BRIDGE_SCORED
									.format(Profile.get(other.getUniqueId()).getLocale()))
									.add("{color}", match.getRelationColor(other, player).toString())
									.add("{player}", player.getName())
									.send(other);
					}));

					GameParticipant<MatchGamePlayer> otherTeam = match.getParticipantA()
						.containsPlayer(player.getUniqueId()) ?
						match.getParticipantB() :
						match.getParticipantA();

					otherTeam.getPlayers().forEach(matchGamePlayer -> matchGamePlayer.setDead(true));
					otherTeam.setEliminated(true);

					if (match.canEndRound()) {
						match.setState(MatchState.ENDING_ROUND);
						match.getLogicTask().setNextAction(2);
						match.onRoundEnd();

						if (match.canEndMatch()) match.setState(MatchState.ENDING_MATCH);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockFromEvent(BlockFromToEvent event) {
		Match.getMatches().forEach(match -> {
			Arena arena = match.getArena();
			int x = (int) event.getToBlock().getLocation().getX();
			int y = (int) event.getToBlock().getLocation().getY();
			int z = (int) event.getToBlock().getLocation().getZ();

			if (y > arena.getMaxBuildHeight()) {
				event.setCancelled(true);
				return;
			}

			if (arena instanceof StandaloneArena) {
				StandaloneArena standaloneArena = (StandaloneArena) arena;
				if (standaloneArena.getSpawnBlue() != null && standaloneArena.getSpawnBlue().contains(event.getToBlock())) {
					event.setCancelled(true);
					return;
				}
				if (standaloneArena.getSpawnRed() != null && standaloneArena.getSpawnRed().contains(event.getToBlock())) {
					event.setCancelled(true);
					return;
				}
			}

			if (x >= arena.getX1() && x <= arena.getX2() && y >= arena.getY1() && y <= arena.getY2() &&
					z >= arena.getZ1() && z <= arena.getZ2()) {
				match.getPlacedBlocks().add(event.getBlock().getLocation());
				Location down = event.getBlock().getLocation().subtract(0, 1, 0);
				if (down.getBlock().getType() == Material.GRASS) {
					match.getChangedBlocks().add(down.getBlock().getState());
				}
			} else {
				event.setCancelled(true);
			}
		});
	}

	@EventHandler(ignoreCancelled=true)
	public void onBlockPlaceEvent(BlockPlaceEvent blockPlaceEvent) {
		Player player = blockPlaceEvent.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		if (profile.getState() == ProfileState.FIGHTING) {
			Match match = profile.getMatch();
			if ((match.getKit().getGameRules().isBuild() || match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())) && match.getState() == MatchState.PLAYING_ROUND || match.getState() == MatchState.STARTING_ROUND) {
				if (match.getKit().getGameRules().isSpleef()) {
					blockPlaceEvent.setCancelled(true);
					return;
				}
				Arena arena = match.getArena();
				int n = (int)blockPlaceEvent.getBlockPlaced().getLocation().getX();
				int n2 = (int)blockPlaceEvent.getBlockPlaced().getLocation().getY();
				int n3 = (int)blockPlaceEvent.getBlockPlaced().getLocation().getZ();
				if (n2 > arena.getMaxBuildHeight()) {
					new MessageFormat(Locale.ARENA_REACHED_MAXIMUM.format(profile.getLocale())).send(player);
					blockPlaceEvent.setCancelled(true);
					return;
				}
				if (arena instanceof StandaloneArena) {
					StandaloneArena standaloneArena = (StandaloneArena)arena;
					if (standaloneArena.getSpawnBlue() != null && standaloneArena.getSpawnBlue().contains(blockPlaceEvent.getBlockPlaced())) {
						blockPlaceEvent.setCancelled(true);
						return;
					}
					if (standaloneArena.getSpawnRed() != null && standaloneArena.getSpawnRed().contains(blockPlaceEvent.getBlockPlaced())) {
						blockPlaceEvent.setCancelled(true);
						return;
					}
				}
				if (arena.contains(n, n2, n3)) {
					if (blockPlaceEvent.getBlockReplacedState() == null || blockPlaceEvent.getBlockReplacedState().getType() == Material.AIR) {
						match.getPlacedBlocks().add(blockPlaceEvent.getBlock().getLocation());
					} else {
						match.getChangedBlocks().add(blockPlaceEvent.getBlockReplacedState());
					}
				} else {
					new MessageFormat(Locale.ARENA_BUILD_OUTSIDE.format(profile.getLocale())).send(player);
					blockPlaceEvent.setCancelled(true);
				}
			} else {
				blockPlaceEvent.setCancelled(true);
			}
		} else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
			blockPlaceEvent.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onBlockBreakEvent(BlockBreakEvent blockBreakEvent) {
		Player player = blockBreakEvent.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		if (profile.getState() == ProfileState.FIGHTING) {
			Match match = profile.getMatch();
			if ((match.getKit().getGameRules().isBuild() || match.getKit().getGameRules().isSpleef() || match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())) && match.getState() == MatchState.PLAYING_ROUND) {
				if (match.getKit().getGameRules().isSpleef()) {
					if (blockBreakEvent.getBlock().getType() == Material.SNOW_BLOCK || blockBreakEvent.getBlock().getType() == Material.SNOW) {
						match.getChangedBlocks().add(blockBreakEvent.getBlock().getState());
						blockBreakEvent.getBlock().setType(Material.AIR);
						player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 4));
						player.updateInventory();
					} else {
						blockBreakEvent.setCancelled(true);
					}
				} else if (match.getKit().getGameRules().isHcftrap()) {
					if (match.getPlacedBlocks().contains(blockBreakEvent.getBlock().getLocation())) {
						match.getPlacedBlocks().remove(blockBreakEvent.getBlock().getLocation());
					} else if (match.getChangedBlocks().stream().noneMatch(blockState -> blockState.getLocation().equals(blockBreakEvent.getBlock().getLocation()))) {
						match.getChangedBlocks().add(blockBreakEvent.getBlock().getState());
					}
					if (!blockBreakEvent.getBlock().getDrops().isEmpty()) {
						match.getDroppedItems().add(blockBreakEvent.getBlock().getLocation().getWorld().dropItemNaturally(blockBreakEvent.getBlock().getLocation(), blockBreakEvent.getBlock().getDrops().stream().findFirst().orElse(null)));
					}
					blockBreakEvent.getBlock().setType(Material.AIR);
					blockBreakEvent.setCancelled(true);
				} else if (!match.getPlacedBlocks().remove(blockBreakEvent.getBlock().getLocation())) {
					blockBreakEvent.setCancelled(true);
				}
			} else {
				blockBreakEvent.setCancelled(true);
			}
		} else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
			blockBreakEvent.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
	public void onBucketEmptyEvent(PlayerBucketEmptyEvent playerBucketEmptyEvent) {
		Player player = playerBucketEmptyEvent.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		if (profile.getState() == ProfileState.FIGHTING) {
			Match match = profile.getMatch();
			if ((match.getKit().getGameRules().isBuild() || match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())) && match.getState() == MatchState.PLAYING_ROUND) {
				Arena arena = match.getArena();
				Block block = playerBucketEmptyEvent.getBlockClicked().getRelative(playerBucketEmptyEvent.getBlockFace());
				int n = (int)block.getLocation().getX();
				int n2 = (int)block.getLocation().getY();
				int n3 = (int)block.getLocation().getZ();
				if (n2 > arena.getMaxBuildHeight()) {
					new MessageFormat(Locale.ARENA_REACHED_MAXIMUM.format(profile.getLocale())).send(player);
					playerBucketEmptyEvent.setCancelled(true);
					return;
				}
				if (n >= arena.getX1() && n <= arena.getX2() && n2 >= arena.getY1() && n2 <= arena.getY2() && n3 >= arena.getZ1() && n3 <= arena.getZ2()) {
					match.getPlacedBlocks().add(block.getLocation());
				} else {
					playerBucketEmptyEvent.setCancelled(true);
				}
			} else {
				playerBucketEmptyEvent.setCancelled(true);
			}
		} else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
			playerBucketEmptyEvent.setCancelled(true);
		}
	}


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
	@EventHandler
	public void onSign(SignChangeEvent e) {
		int currentLine = 0;
		for (String line : e.getLines()) {
			if (line.equalsIgnoreCase("[Elevator]")) {
				if (currentLine < 3) {
					if (e.getLine(currentLine + 1).equalsIgnoreCase("Up")) {
						e.setLine(0, "");
						e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&c[Elevator]"));
						e.setLine(2, ChatColor.translateAlternateColorCodes('&', "Up"));
						e.setLine(3, "");
					} else if (e.getLine(currentLine + 1).equalsIgnoreCase("Down")) {
						e.setLine(0, "");
						e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&c[Elevator]"));
						e.setLine(2, ChatColor.translateAlternateColorCodes('&', "Down"));
						e.setLine(3, "");
					} else {
						//Bukkit.broadcastMessage(e.getLine(currentLine + 1));
						e.getBlock().breakNaturally();
						e.getPlayer().sendMessage(CC.translate("&cInvalid direction."));
					}
				}
			}

			currentLine++;
		}
	}

	@EventHandler
    public void onSignInteract(PlayerInteractEvent e) {
		boolean ret = e.getClickedBlock() == null;
        Player player = e.getPlayer();
        if (ret) {
            return;
        }
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.WALL_SIGN) || e.getClickedBlock().getType() == Material.SIGN_POST) {
            final Sign s = (Sign) e.getClickedBlock().getState();
            if (s.getLine(1).equalsIgnoreCase(ChatColor.RED + "[Elevator]")) { // If first line == "Elevator"
                if (s.getLine(2).equalsIgnoreCase("Up")) { // If second line == "Up"
                	Location loc = e.getClickedBlock().getLocation().add(0.0, 1.0, 0.0);
                    while (loc.getY() < 254.0) {
                        if (loc.getBlock().getType() != Material.AIR) {
                            while (loc.getBlockY() < 254) {
                                if (loc.getBlock().getType() == Material.AIR && loc.add(0.0, 1.0, 0.0).getBlock().getType() == Material.AIR) {
                                	Location pl = player.getLocation();
                                    player.teleport(new Location(pl.getWorld(), loc.getX() + 0.5, loc.getY() - 1.0, loc.getZ() + 0.5, pl.getYaw(), pl.getPitch()));
                                    break;
                                }
                                loc.add(0.0, 1.0, 0.0);
                            }
                            break;
                        }
                        loc.add(0.0, 1.0, 0.0);
                    }
                    if (loc.getY() == 254.0) {
						player.sendMessage(CC.translate("&7[&cHysteria&7] &cCould not teleport.")); // Send error if teleport isn't possbile.
                    }
                }
                else if (s.getLine(2).equalsIgnoreCase("Down")) { // If second line == "down"
                	Location loc = e.getClickedBlock().getLocation().subtract(0.0, 1.0, 0.0);
                    while (loc.getY() > 2.0) {
                        if (loc.getBlock().getType() != Material.AIR) {
                            while (loc.getY() > 2.0) {
                                if (loc.getBlock().getType() == Material.AIR && loc.subtract(0.0, 1.0, 0.0).getBlock().getType() == Material.AIR) {
                                    player.teleport(new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getY(), loc.getZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch()));
                                    break;
                                }
                                loc.subtract(0.0, 1.0, 0.0);
                            }
                            break;
                        }
                        loc.subtract(0.0, 1.0, 0.0);
                    }
                    if (loc.getY() == 2.0) {
						player.sendMessage(CC.translate("&7[&cHysteria&7] &cCould not teleport.")); // Send error if teleport isn't possbile.
                    }
                }
            }
        }
    }

	@EventHandler
	public void onPearlLand(PlayerTeleportEvent event) {
		Location to = event.getTo();

		if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			to.setX(to.getBlockX() + 0.5);
			to.setZ(to.getBlockZ() + 0.5);
			event.setTo(to);
			Location pearlLocation = event.getTo();
			Location playerLocation = event.getFrom();

			if (playerLocation.getBlockY() < pearlLocation.getBlockY()) {
				Block block = pearlLocation.getBlock();

				for (BlockFace face : BlockFace.values()) {
					Material type = block.getRelative(face).getType();

					if (type == Material.GLASS || type == Material.BARRIER) {
						pearlLocation.setY(pearlLocation.getBlockY() - 1.0);
						break;
					}
				}
			} else pearlLocation.setY(pearlLocation.getBlockY() + 0.0); // set to 0

			event.setTo(pearlLocation);
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
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		if (profile.getState() == ProfileState.FIGHTING &&  profile.getMatch().getKit().getGameRules().isBridge()) {
			if (event.getItem().getType() == Material.GOLDEN_APPLE) {
				if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().getDisplayName().contains("Bridge Apple")) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
					player.setHealth(20);
				}
			}
		}
		if (event.getItem().getType() == Material.GOLDEN_APPLE) {
			if (event.getItem().hasItemMeta() &&
			    event.getItem().getItemMeta().getDisplayName().contains("Golden Head")) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
				player.setFoodLevel(Math.min(player.getFoodLevel() + 6, 20));
			}
		}
//		if (Practice.get().getMainConfig().getBoolean("MATCH.REMOVE_POTION_BOTTLE_ON_CONSUME")) {
//			if (event.getItem().getType() == Material.POTION) {
//				System.out.print(1);
//				player.getItemInHand().setType(Material.AIR);
//				player.updateInventory();
//			}
//		}
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
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());
		Player player = event.getPlayer();

		if (profile.getState() == ProfileState.FIGHTING) {
			Match match = profile.getMatch();

			if (match.getState() == MatchState.STARTING_ROUND || match.getState() == MatchState.PLAYING_ROUND) {
				profile.getMatch().onDisconnect(player);
			}
		}
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (profile.getState() == ProfileState.FIGHTING) {
			Match match = profile.getMatch();

			if (match.getState() == MatchState.STARTING_ROUND || match.getState() == MatchState.PLAYING_ROUND) {
				profile.getMatch().onDisconnect(event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getState() == ProfileState.SPECTATING && event.getRightClicked() instanceof Player &&
		    player.getItemInHand() != null) {
			Player target = (Player) event.getRightClicked();

			if (Hotbar.fromItemStack(player.getItemInHand()) == HotbarItem.VIEW_INVENTORY) {
				new ViewInventoryMenu(target).openMenu(player);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractSoup(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Profile profileSoup = Profile.get(player.getUniqueId());

		if (profileSoup.getState() == ProfileState.FIGHTING && profileSoup.getMatch().getKit().getGameRules().isSoup()) {
			if (event.getItem() != null && event.getItem().getType() == Material.MUSHROOM_SOUP) {
				if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					event.setCancelled(true);
					Player p = event.getPlayer();
					Damageable d = event.getPlayer();
					if (d.getHealth() < d.getMaxHealth() - 7) {
						d.setHealth(d.getHealth() + 7);
						p.setItemInHand(new ItemStack(Material.BOWL));
					} else if (d.getHealth() < d.getMaxHealth()) {
						d.setHealth(d.getMaxHealth());
						p.setItemInHand(new ItemStack(Material.BOWL));
					} else if (p.getFoodLevel() < 13) {
						p.setFoodLevel(p.getFoodLevel() + 7);
						p.setItemInHand(new ItemStack(Material.BOWL));
						if (p.getSaturation() < 13) {
							p.setSaturation(p.getSaturation() + 7);
						} else {
							p.setSaturation(20);
						}
					} else if (p.getFoodLevel() < 20) {
						p.setFoodLevel(20);
						p.setItemInHand(new ItemStack(Material.BOWL));
						if (p.getSaturation() < 13) {
							p.setSaturation(p.getSaturation() + 7);
						} else {
							p.setSaturation(20);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPearl(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		ItemStack itemStack = event.getItem();

		if(profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.FFA) {

			Match match = profile.getMatch();

			if(match == null) {
				return;
			}

			if(itemStack == null) {
				return;
			}

			if (itemStack.getType() == Material.ENDER_PEARL) {
				if (match.getState() != MatchState.PLAYING_ROUND) {
					player.sendMessage(CC.RED + "You can't throw pearls right now!");
					event.setCancelled(true);
					return;
				}
			}

			if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR || !event.hasItem()) {
				return;
			}

			if (event.getItem().getType() == Material.ENDER_PEARL) {
				if (!profile.getEnderpearlCooldown().hasExpired()) {
					event.setCancelled(true);
					String time = TimeUtil.millisToSeconds(profile.getEnderpearlCooldown().getRemaining());
					new MessageFormat(Locale.MATCH_ENDERPEARL_COOLDOWN.format(profile.getLocale())).add("{context}", (time.equalsIgnoreCase("1.0") ? "" : "s")).add("{time}", time).send(player);
				}
			}
		}
	}

	@EventHandler
	public void onPearlLaunch(ProjectileLaunchEvent event) {
		Player player = (Player) event.getEntity().getShooter();
		Profile profile = Profile.get(player.getUniqueId());
		Match match = profile.getMatch();

		// If match doesn't exist then stop.
		if(match == null) {
			return;
		}

		// Check if match isn't MatchState.PLAYING_ROUND and ProfileState.FIGHTING, if so, cancel event.
		if (match.getState() != MatchState.PLAYING_ROUND && profile.getState() == ProfileState.FIGHTING) {
			if(event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof EnderPearl) {
				player.sendMessage(CC.RED + "You can't throw pearls right now!");
				event.setCancelled(true);
				return;
			}
		}

		// Set pearl cooldown to player.
		if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof EnderPearl && profile.getState() == ProfileState.FIGHTING || event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof EnderPearl && profile.getState() == ProfileState.FFA) {
			if (profile.getEnderpearlCooldown().hasExpired()) {
				profile.setEnderpearlCooldown(new Cooldown(16_000));
			}
		}
	}


	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		ItemStack itemStack = event.getItem();

		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (profile = Profile.get(player.getUniqueId())).getState() == ProfileState.FIGHTING) {
			Match match = profile.getMatch();

			if (profile.getState() == ProfileState.FIGHTING) {

				if (itemStack != null) {
					if (Hotbar.fromItemStack(itemStack) == HotbarItem.SPECTATE_STOP) {
						match.onDisconnect(player);
						return;
					}

					// Kit Selection
					if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
						ItemStack kitItem = Hotbar.getItems().get(HotbarItem.KIT_SELECTION).getItemStack();

						if (itemStack.getType() == kitItem.getType() &&
								itemStack.getDurability() == kitItem.getDurability()) {
							Matcher matcher = HotbarItem.KIT_SELECTION.getPattern().
									matcher(itemStack.getItemMeta().getDisplayName());

							if (matcher.find()) {
								String kitName = matcher.group(2);
								KitLoadout kitLoadout = null;

								if (kitName.equals("Default")) {
									kitLoadout = match.getKit().getKitLoadout();
								} else {
									for (KitLoadout find : profile.getKitData().get(match.getKit()).getLoadouts()) {
										if (find != null && find.getCustomName().equals(kitName)) {
											kitLoadout = find;
										}
									}
								}

								if (kitLoadout != null) {
									//player.sendMessage(Locale.MATCH_GIVE_KIT.format(kitLoadout.getCustomName()));
									new MessageFormat(Locale.MATCH_GIVE_KIT.format(profile.getLocale()))
											.add("{kit_name}", kitLoadout.getCustomName())
											.send(player);
									if (match.getKit().getGameRules().isBridge()) {
										player.getInventory().setContents(kitLoadout.getContents());
										KitUtils.giveBridgeKit(player);
										profile.setSelectedKit(kitLoadout);
									} else if (match.getKit().getGameRules().isHcftrap()) {
										player.getInventory().setContents(kitLoadout.getContents());
										KitUtils.giveBaseRaidingKit(player);
										profile.setSelectedKit(kitLoadout);
									} else {
										player.getInventory().setArmorContents(kitLoadout.getArmor());
										player.getInventory().setContents(kitLoadout.getContents());
									}
									player.updateInventory();
									event.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractHCFTrap(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();

		if(teamMatch == null) {
			return;
		}

		if (profile.getState() == ProfileState.SPECTATING) event.setCancelled(true);

		if(teamMatch.getKit().getGameRules().isHcftrap()) {
			if(event.getItem() != null && event.getClickedBlock() != null && (event.getClickedBlock().getType().name().contains("FENCE") || event.getClickedBlock().getState() instanceof TrapDoor || event.getClickedBlock().getType().name().contains("TRAP") || event.getClickedBlock().getType().name().contains("CHEST") || event.getClickedBlock().getType().name().contains("DOOR") || event.getClickedBlock().getType().equals(Material.BEACON) || event.getClickedBlock().getType().equals(Material.FURNACE) || event.getClickedBlock().getType().equals(Material.WORKBENCH) || event.getClickedBlock().getType().equals(Material.NOTE_BLOCK) || event.getClickedBlock().getType().equals(Material.JUKEBOX) || event.getClickedBlock().getType().equals(Material.ANVIL) || event.getClickedBlock().getType().equals(Material.HOPPER) || event.getClickedBlock().getType().equals(Material.BED_BLOCK) || event.getClickedBlock().getType().equals(Material.DROPPER) || event.getClickedBlock().getType().equals(Material.BREWING_STAND) && event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			//if(event.getItem() != null && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.SPRUCE_FENCE_GATE && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
					if (teamMatch.getChangedBlocks().stream().noneMatch(blockState -> blockState.getBlock().getLocation().equals(event.getClickedBlock().getLocation()))) {
						teamMatch.getChangedBlocks().add(event.getClickedBlock().getState());
					}
				} else {
					player.sendMessage(CC.translate("&cYou cannot interact as a raider."));
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onMathEnd(MatchEndEvent event) {
		Match match = event.getMatch();
		match.getParticipants().forEach(matchGamePlayerGameParticipant -> {
			matchGamePlayerGameParticipant.getPlayers().forEach(player -> {
				if (player.getPlayer() == null) return;
				if(cPractice.get().getServer().getName().equalsIgnoreCase("pSpigot")) {
					KnockbackProfile knockbackProfile = SpigotConfig.getKbProfileByName("default");
					player.getPlayer().setKbProfile(knockbackProfile);
				} else {
					Knockback.getKnockbackProfiler().setKnockback(player.getPlayer(), "default");
				}
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
