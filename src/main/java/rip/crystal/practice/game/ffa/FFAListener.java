package rip.crystal.practice.game.ffa;
/* 
   Made by cpractice Development Team
   Created on 27.11.2021
*/

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.TimeUtil;
import rip.crystal.practice.utilities.chat.CC;


public class FFAListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FFA) {
            Player killer = PlayerUtil.getLastAttacker(event.getEntity());

            if (killer != null) {
                this.broadcastMessage("&9" + player.getName() + " &fwas slain by &9" + killer.getName() + "&f.");
            }

            event.setDeathMessage(null);
            event.setDroppedExp(0);
            player.setHealth(20.0);
            new BukkitRunnable(){

                public void run() {
                    cPractice.get().getFfaManager().joinFFA(player, Arena.getByName("FFA")); // Send player to FFA
                }
            }.runTaskLater(cPractice.get(), 5L);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onLow(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FFA) {
            if (player.getLocation().getBlockY() <= 30) {
                new BukkitRunnable(){
                    public void run() {
                        cPractice.get().getFfaManager().joinFFA(player, Arena.getByName("FFA")); // Send player to FFA
                    }
                }.runTaskLater(cPractice.get(), 5L);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Profile profile = Profile.get(event.getPlayer().getUniqueId());
        Player player = event.getPlayer();
        Item getItemDrop = event.getItemDrop();
        if(profile.getState() == ProfileState.FFA) {
            if (getItemDrop.getItemStack().getType() == Material.DIAMOND_SWORD) { // If player is holding sword, cancel event.
                player.sendMessage(CC.translate("&cYou cannot drop your sword."));
                event.setCancelled(true);
                return;
            }
            event.setCancelled(false); // If player isn't holding sword, don't cancel
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        Profile profile = Profile.get(event.getPlayer().getUniqueId());
        if (profile.getState() == ProfileState.FFA) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            Profile profile = Profile.get(player.getUniqueId());

            if (profile.getState() == ProfileState.FFA) {
                if (cPractice.get().getFfaManager().getFfaSafezone() != null && (cPractice.get().getFfaManager().getFfaSafezone().contains(victim.getLocation()) || cPractice.get().getFfaManager().getFfaSafezone().contains(player.getLocation()))) {
                    event.setCancelled(true);
                    player.sendMessage(CC.translate("&cYou can't attack players in safezone!"));
                }
            }
        }
    }

    @EventHandler
    public void onPearlLand(PlayerTeleportEvent event) {
        Location to = event.getTo();
        Profile profile = Profile.get(event.getPlayer().getUniqueId());
        if (profile.getState() == ProfileState.FFA) {
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
    }

    @EventHandler
    public void onPearl(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        ItemStack itemStack = event.getItem();

        if(profile.getState() == ProfileState.FFA) {
            if(itemStack == null) {
                return;
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
        if(player == null) {
            return;
        }
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FFA) {
            // Set pearl cooldown to player.
            if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof EnderPearl && profile.getState() == ProfileState.FFA) {
                if (profile.getEnderpearlCooldown().hasExpired()) {
                    profile.setEnderpearlCooldown(new Cooldown(16_000));
                }
            }
        }
    }

    private void broadcastMessage(String message) {
        for (Profile ffaPlayer : cPractice.get().getFfaManager().getFFAPlayers()) {
            ffaPlayer.msg(message);
        }
    }
}
