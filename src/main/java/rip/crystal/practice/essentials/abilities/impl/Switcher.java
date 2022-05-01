package rip.crystal.practice.essentials.abilities.impl;

import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.essentials.abilities.utils.DurationFormatter;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;

public class Switcher extends Ability {

    private final cPractice plugin = cPractice.get();

    public Switcher() {
        super("SWITCHER");
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            if (isAbility(shooter.getItemInHand())) {
                event.getEntity().setMetadata(this.getAbility(), new FixedMetadataValue(this.plugin, true));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player shooter = event.getPlayer();
            Profile profile = Profile.get(shooter.getUniqueId());

            if (profile.getSwitcher().onCooldown(shooter)) {
                shooter.sendMessage(CC.translate("&bYou are on &6&lSwitcher &bcooldown for &3" + DurationFormatter.getRemaining(profile.getSwitcher().getRemainingMilis(shooter), true, true)));
                shooter.updateInventory();
                event.setCancelled(true);
                return;
            }

            if(profile.getPartneritem().onCooldown(shooter)){
                shooter.sendMessage(CC.translate("&bYou are on &d&lPartner Item &bcooldown for &3" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(shooter), true, true)));
                shooter.updateInventory();
                event.setCancelled(true);
                return;
            }

            profile.getSwitcher().applyCooldown(shooter, 8 * 1000);
            profile.getPartneritem().applyCooldown(shooter,  10 * 1000);

            plugin.getAbilityManager().cooldownExpired(shooter, this.getName(), this.getAbility());
            plugin.getAbilityManager().playerMessage(shooter, this.getAbility());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            if (projectile instanceof Egg && projectile.hasMetadata(this.getAbility())) {
                Player player = (Player) event.getEntity();
                Player shooter = (Player) projectile.getShooter();

                Location playerLocation = player.getLocation().clone();
                Location shooterLocation = shooter.getLocation().clone();

                player.teleport(shooterLocation);
                shooter.teleport(playerLocation);

                this.plugin.getAbilityManager().targetMessage(player, shooter, this.getAbility());
            }
            else if (projectile instanceof Snowball && projectile.hasMetadata(this.getAbility())) {
                Player player = (Player) event.getEntity();
                Player shooter = (Player) projectile.getShooter();

                Location playerLocation = player.getLocation().clone();
                Location shooterLocation = shooter.getLocation().clone();

                player.teleport(shooterLocation);
                shooter.teleport(playerLocation);

                this.plugin.getAbilityManager().targetMessage(player, shooter, this.getAbility());
            }
        }
    }
}