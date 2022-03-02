package rip.crystal.practice.abilities.impl;

import rip.crystal.practice.abilities.Ability;
import rip.crystal.practice.abilities.utils.DurationFormatter;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TankIngot extends Ability {

    private final cPractice plugin = cPractice.get();

    public TankIngot() {
        super("TANK_INGOT");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            Profile profile = Profile.get(player.getUniqueId());

            if (profile.getTankingot().onCooldown(player)) {
                player.sendMessage(CC.translate("&bYou are on &6&lTank Ingot &bcooldown for &3" + DurationFormatter.getRemaining(profile.getTankingot().getRemainingMilis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getPartneritem().onCooldown(player)){
                player.sendMessage(CC.translate("&bYou are on &d&lPartner Item &bcooldown for &3" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getTankingot().applyCooldown(player, 60 * 1000);
            profile.getPartneritem().applyCooldown(player,  10 * 1000);

            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 6, 2));

            plugin.getAbilityManager().cooldownExpired(player, this.getName(), this.getAbility());
            plugin.getAbilityManager().playerMessage(player, this.getAbility());
        }
    }
}
