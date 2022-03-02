package rip.crystal.practice.ffa;
/* 
   Made by Hysteria Development Team
   Created on 27.11.2021
*/

import rip.crystal.practice.Locale;
import rip.crystal.practice.arena.Arena;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.TimeUtil;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class FFAListener implements Listener {

    //private FFAManager ffaManager;
    //private int[] broadcastKills = new int[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50};

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
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        Action ac = event.getAction();
        Block bl = event.getClickedBlock();

        // Ender pearl listener (hover on block)
        Profile profile2 = Profile.get(player.getUniqueId());
        if (event.getItem() != null && event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (profile2.getState() == ProfileState.FFA) {
                if (event.getItem().getType() == Material.ENDER_PEARL) {
                    if (!profile2.getEnderpearlCooldown().hasExpired()) {
                        String time = TimeUtil.millisToSeconds(profile2.getEnderpearlCooldown().getRemaining());
                        new MessageFormat(Locale.MATCH_ENDERPEARL_COOLDOWN.format(profile2.getLocale()))
                                .add("{context}", (time.equalsIgnoreCase("1.0") ? "" : "s"))
                                .add("{time}", time)
                                .send(player);
                        event.setCancelled(true);
                        return;
                    } else {
                        if (ac == Action.RIGHT_CLICK_BLOCK && (bl.getType() == Material.FENCE_GATE) && !player.isSneaking()) {
                            event.setCancelled(true);
                            return;
                        }
                        profile2.setEnderpearlCooldown(new Cooldown(16_000));
                    }
                    event.setCancelled(false);
                }
            }
            return;
        }

        if (itemStack != null && event.getAction() == Action.RIGHT_CLICK_AIR || itemStack != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Profile profile = Profile.get(player.getUniqueId());

            if (profile.getState() == ProfileState.FFA) {
                // Ender pearl listener (hover over air)
                if (itemStack.getType() == Material.ENDER_PEARL && event.getClickedBlock() == null) {
                    if (!profile.getEnderpearlCooldown().hasExpired()) {
                        String time = TimeUtil.millisToSeconds(profile.getEnderpearlCooldown().getRemaining());
                        //player.sendMessage(Locale.MATCH_ENDERPEARL_COOLDOWN.format(time,
                        //	(time.equalsIgnoreCase("1.0") ? "" : "s")));
                        new MessageFormat(Locale.MATCH_ENDERPEARL_COOLDOWN.format(profile.getLocale()))
                                .add("{context}", (time.equalsIgnoreCase("1.0") ? "" : "s"))
                                .add("{time}", time)
                                .send(player);
                        event.setCancelled(true);
                    } else {
                        profile.setEnderpearlCooldown(new Cooldown(16_000));
                    }
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
