package rip.crystal.practice.essentials.abilities.impl;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.essentials.abilities.utils.DurationFormatter;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class EffectDisabler extends Ability {

    private final cPractice plugin = cPractice.get();

    private final Map<UUID, Integer> HITS = Maps.newHashMap();

    public EffectDisabler() {
        super("EFFECT_DISABLER");
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            Profile profile = Profile.get(damager.getUniqueId());
            
            if (!isAbility(damager.getItemInHand())) return;

            if (isBard(victim) || isArcher(victim) || isRogue(victim) || isMiner(victim)) return;
            
            if (profile.getEffectdisabler().onCooldown(damager)) {
                damager.sendMessage(CC.translate("&bYou are on &6&lEffect Disabler &bcooldown for &3" + DurationFormatter.getRemaining(profile.getEffectdisabler().getRemainingMilis(damager), true, true)));
                damager.updateInventory();
                return;
            }

            if(profile.getPartneritem().onCooldown(damager)){
                damager.sendMessage(CC.translate("&bYou are on &d&lPartner Item &bcooldown for &3" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(damager), true, true)));
                damager.updateInventory();
                return;
            }

            if (!HITS.containsKey(victim.getUniqueId())) {
                HITS.put(victim.getUniqueId(), 0);
            }

            HITS.put(victim.getUniqueId(), HITS.get(victim.getUniqueId()) + 1);

            if (HITS.get(victim.getUniqueId()) != 5) return;

            PlayerUtil.decrement(damager);

            profile.getEffectdisabler().applyCooldown(damager, 60 * 1000);
            profile.getPartneritem().applyCooldown(damager,  10 * 1000);
            HITS.remove(victim.getUniqueId());

            victim.getActivePotionEffects().forEach(potionEffect -> victim.removePotionEffect(potionEffect.getType()));

            plugin.getAbilityManager().cooldownExpired(damager, this.getName(), this.getAbility());
            plugin.getAbilityManager().playerMessage(damager, this.getAbility());
            plugin.getAbilityManager().targetMessage(victim, damager, this.getAbility());
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        event.setCancelled(true);

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Player player = event.getPlayer();

            if (this.hasCooldown(player)) {
                event.setCancelled(true);
                plugin.getAbilityManager().cooldown(player, this.getName(), this.getCooldown(player));
                player.updateInventory();
            }
        }
    }

    private boolean isBard(Player victim) {
        return victim.getInventory().getHelmet() != null && victim.getInventory().getHelmet().getType().equals(Material.GOLD_HELMET)
                && victim.getInventory().getChestplate() != null && victim.getInventory().getChestplate().getType().equals(Material.GOLD_CHESTPLATE)
                && victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().getType().equals(Material.GOLD_LEGGINGS)
                && victim.getInventory().getBoots() != null && victim.getInventory().getBoots().getType().equals(Material.GOLD_BOOTS);
    }

    private boolean isArcher(Player victim) {
        return victim.getInventory().getHelmet() != null && victim.getInventory().getHelmet().getType().equals(Material.LEATHER_HELMET)
                && victim.getInventory().getChestplate() != null && victim.getInventory().getChestplate().getType().equals(Material.LEATHER_CHESTPLATE)
                && victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().getType().equals(Material.LEATHER_LEGGINGS)
                && victim.getInventory().getBoots() != null && victim.getInventory().getBoots().getType().equals(Material.LEATHER_BOOTS);
    }

    private boolean isRogue(Player victim) {
        return victim.getInventory().getHelmet() != null && victim.getInventory().getHelmet().getType().equals(Material.CHAINMAIL_HELMET)
                && victim.getInventory().getChestplate() != null && victim.getInventory().getChestplate().getType().equals(Material.CHAINMAIL_CHESTPLATE)
                && victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().getType().equals(Material.CHAINMAIL_LEGGINGS)
                && victim.getInventory().getBoots() != null && victim.getInventory().getBoots().getType().equals(Material.CHAINMAIL_BOOTS);
    }

    private boolean isMiner(Player victim) {
        return victim.getInventory().getHelmet() != null && victim.getInventory().getHelmet().getType().equals(Material.IRON_HELMET)
                && victim.getInventory().getChestplate() != null && victim.getInventory().getChestplate().getType().equals(Material.IRON_CHESTPLATE)
                && victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().getType().equals(Material.IRON_LEGGINGS)
                && victim.getInventory().getBoots() != null && victim.getInventory().getBoots().getType().equals(Material.IRON_BOOTS);
    }
}