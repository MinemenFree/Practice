package rip.crystal.practice.essentials.abilities.impl;

import com.google.common.collect.Maps;
import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.essentials.abilities.cooldown.AbilityCooldowns;
import rip.crystal.practice.essentials.abilities.utils.DurationFormatter;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Beacon extends Ability {

    private final Map<Location, Set<Player>> affectedPlayers;

    public Beacon() {
        super("BEACON");

        this.affectedPlayers = Maps.newLinkedHashMap();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (AbilityCooldowns.isOnCooldown("DENY-BLOCK", player)) {
            event.setCancelled(true);
            player.sendMessage(
                    CC.translate("&cYou can't place blocks due to a &6&lBeacon &cability was activated nearest you!"));
            return;
        }

        if (!isAbility(player.getItemInHand())) {
            return;
        }

        Block block = event.getBlock();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getPartneritem().onCooldown(player)) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&bYou are on &d&lPartner Item &bcooldown for &3" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(player), true, true)));
            return;
        }

        if (profile.getBeacom().onCooldown(player)) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&bYou are on &6&lBeacon &bcooldown for &3"
                    + DurationFormatter.getRemaining(profile.getBeacom().getRemainingMilis(player), true, true)));
            return;
        }

        PlayerUtil.decrement(player);

        profile.getBeacom().applyCooldown(player, 60 * 1000);
        profile.getPartneritem().applyCooldown(player, 10 * 1000);

        this.setAntiBuild(player, block.getLocation().clone());

        cPractice.get().getAbilityManager().cooldownExpired(player, this.getName(), this.getAbility());
        cPractice.get().getAbilityManager().playerMessage(player, this.getAbility());
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() == Material.BEACON) {
            if (this.affectedPlayers.containsKey(block.getLocation())) {
                Set<Player> affectedPlayers = this.affectedPlayers.get(block.getLocation());

                affectedPlayers.forEach(target -> {
                    AbilityCooldowns.removeCooldown("DENY-BLOCK", target);
                });

                this.affectedPlayers.remove(block.getLocation());

                player.sendMessage(
                        CC.translate("&aYou have broken the &6&lBeacon &aability block! Now you can build again."));
                return;
            }
        }

        if (AbilityCooldowns.isOnCooldown("DENY-BLOCK", player)) {
            event.setCancelled(true);
            player.sendMessage(
                    CC.translate("&cYou can't place blocks due to a &6&lBeacon &cability was activated nearest you!"));
        }
    }

    private void setAntiBuild(Player player, Location blockLocation) {
        Set<Player> nearbyPlayers = player.getNearbyEntities(10.0D, 10.0D, 10.0D).stream()
                .filter(entity -> entity instanceof Player).filter(nearbyPlayer -> nearbyPlayer != player)
                .map(entity -> (Player) entity).collect(Collectors.toSet());

        nearbyPlayers.forEach(nearbyPlayer -> AbilityCooldowns.addCooldown("DENY-BLOCK", nearbyPlayer, 15));

        this.affectedPlayers.put(blockLocation, nearbyPlayers);
    }
}