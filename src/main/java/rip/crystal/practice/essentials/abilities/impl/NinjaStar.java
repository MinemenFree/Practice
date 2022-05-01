package rip.crystal.practice.essentials.abilities.impl;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.essentials.abilities.utils.DurationFormatter;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;

import java.util.Map;
import java.util.UUID;

public class NinjaStar extends Ability {

    private final cPractice plugin = cPractice.get();
    private final Map<UUID, UUID> TAGGED = Maps.newHashMap();

    public NinjaStar() {
        super("NINJA_STAR");
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            //AbilityCooldowns.addCooldown("TELEPORT", victim, 15);
            TAGGED.put(victim.getUniqueId(), damager.getUniqueId());
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            Profile profile = Profile.get(player.getUniqueId());

            if (profile.getNinjastar().onCooldown(player)) {
                player.sendMessage(CC.translate("&bYou are on &6&lNinja Star &bcooldown for &3" + DurationFormatter.getRemaining(profile.getNinjastar().getRemainingMilis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getPartneritem().onCooldown(player)){
                player.sendMessage(CC.translate("&bYou are on &d&lPartner Item &bcooldown for &3" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(player), true, true)));
                player.updateInventory();
                return;
            }

            //if (!AbilityCooldowns.isOnCooldown("TELEPORT", player)) return;

            PlayerUtil.decrement(player);

            Player target = Bukkit.getPlayer(TAGGED.get(player.getUniqueId()));

            profile.getEffectdisabler().applyCooldown(player, 60 * 1000);
            profile.getPartneritem().applyCooldown(player,  10 * 1000);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(target == null) {
                        return;
                    }
                    player.teleport(target.getLocation());
                    player.sendMessage(CC.translate("&3You has been successfully teleported")); // you just got teleported back
                }
            }.runTaskLaterAsynchronously(cPractice.get(), (5 * 10));

            plugin.getAbilityManager().cooldownExpired(player, this.getName(), this.getAbility());
            plugin.getAbilityManager().playerMessage(player, this.getAbility());
            plugin.getAbilityManager().targetMessage(target, player, this.getAbility());

            TAGGED.remove(player.getUniqueId());
        }
    }
}