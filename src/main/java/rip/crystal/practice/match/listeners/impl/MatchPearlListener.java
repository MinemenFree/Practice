package rip.crystal.practice.match.listeners.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchState;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.chat.CC;

public class MatchPearlListener implements Listener {
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

    @EventHandler
    public void onPearl(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        ItemStack itemStack = event.getItem();

        /*if(profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.FFA) {

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
        }*/
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

        if (match.getState() != MatchState.PLAYING_ROUND/* && profile.getState() == ProfileState.FIGHTING*/) {
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
}
