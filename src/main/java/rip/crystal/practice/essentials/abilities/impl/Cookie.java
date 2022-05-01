package rip.crystal.practice.essentials.abilities.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.essentials.abilities.utils.DurationFormatter;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;

public class Cookie extends Ability {

    private final cPractice plugin = cPractice.get();

    public Cookie() {
        super("COOKIE");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            Profile profile = Profile.get(player.getUniqueId());

            if (profile.getCookie().onCooldown(player)) {
                player.sendMessage(CC.translate("&bYou are on &6&lCookie &bcooldown for &3" + DurationFormatter.getRemaining(profile.getCookie().getRemainingMilis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getPartneritem().onCooldown(player)){
                player.sendMessage(CC.translate("&bYou are on &d&lPartner Item &bcooldown for &3" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getCookie().applyCooldown(player, 60 * 1000);
            profile.getPartneritem().applyCooldown(player,  10 * 1000);

            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 7, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 7, 4));

            plugin.getAbilityManager().cooldownExpired(player, this.getName(), this.getAbility());
            plugin.getAbilityManager().playerMessage(player, this.getAbility());
        }
    }
}
