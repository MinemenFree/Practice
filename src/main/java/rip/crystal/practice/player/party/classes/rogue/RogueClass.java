package rip.crystal.practice.player.party.classes.rogue;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import rip.crystal.practice.Locale;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.player.party.classes.HCFClass;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.TimeUtils;

import java.util.HashMap;
import java.util.Map;

public class RogueClass implements Listener {

    @Getter private static final Map<String, Long> lastSpeedUsage = new HashMap<>();
    @Getter private static final Map<String, Long> lastJumpUsage = new HashMap<>();
    @Getter private static final Map<String, Long> backstabCooldown = new HashMap<>();


    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Profile profile = Profile.get(event.getPlayer().getUniqueId());
        Match match = profile.getMatch();
        if(match == null || profile.getParty() == null){
            return;
        }
        if (!event.getAction().name().contains("RIGHT_") ||
            !event.hasItem() || !HCFClass.ROGUE.isApply(event.getPlayer())) {
            return;
        }
        Player player = event.getPlayer();
        if(event.getItem().getType() != Material.SUGAR || event.getItem().getType() != Material.FEATHER){
            return;
        }
        itemConsumed(player, event.getItem().getType());
    }

    public boolean itemConsumed(Player player, Material material) {
        if (material == Material.SUGAR) {
            if (lastSpeedUsage.containsKey(player.getName()) && lastSpeedUsage.get(player.getName()) > System.currentTimeMillis()) {
                Long millisLeft = ((lastSpeedUsage.get(player.getName()) - System.currentTimeMillis()) / 1000L) * 1000L;
                String msg = TimeUtils.formatIntoDetailedString((int) (millisLeft / 1000));

                player.sendMessage(ChatColor.RED + "You cannot use this for another \u00A7c\u00A7l" + msg + "\u00A7c.");
                return (false);
            }

            lastSpeedUsage.put(player.getName(), System.currentTimeMillis() + (1000L * 60 * 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 4), true);
        } else {
            if (lastJumpUsage.containsKey(player.getName()) && lastJumpUsage.get(player.getName()) > System.currentTimeMillis()) {
                Long millisLeft = ((lastJumpUsage.get(player.getName()) - System.currentTimeMillis()) / 1000L) * 1000L;
                String msg = TimeUtils.formatIntoDetailedString((int) (millisLeft / 1000));

                player.sendMessage(ChatColor.RED + "You cannot use this for another \u00A7c\u00A7l" + msg + "\u00A7c.");
                return (false);
            }

            lastJumpUsage.put(player.getName(), System.currentTimeMillis() + (1000L * 60 * 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 6), true);
        }

        return (true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Profile damagerProfile = Profile.get(damager.getUniqueId());
            Player victim = (Player) event.getEntity();

            if (damager.getItemInHand() != null && damager.getItemInHand().getType() == Material.GOLD_SWORD &&
                HCFClass.ROGUE.isApply(damager)) {
                if (backstabCooldown.containsKey(damager.getName())
                    && backstabCooldown.get(damager.getName()) > System.currentTimeMillis()) return;

                backstabCooldown.put(damager.getName(), System.currentTimeMillis() + 1500L);

                Vector playerVector = damager.getLocation().getDirection();
                Vector entityVector = victim.getLocation().getDirection();

                playerVector.setY(0F);
                entityVector.setY(0F);

                double degrees = playerVector.angle(entityVector);

                if (Math.abs(degrees) < 1.4) {
                    damager.setItemInHand(new ItemStack(Material.AIR));

                    damager.playSound(damager.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
                    damager.getWorld().playEffect(victim.getEyeLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);

                    if (victim.getHealth() - 7D <= 0) {
                        event.setCancelled(true);
                    } else {
                        event.setDamage(0D);
                    }

                    //DeathMessageHandler.addDamage(victim, new BackstabDamage(victim.getName(), 7D, damager.getName()));
                    victim.setHealth(Math.max(0D, victim.getHealth() - 7D));
                    //if (victim.getHealth() <= 0.0D) new BackstabKillEvent(damager, victim).call();

                    damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 2));
                    new MessageFormat(Locale.CLASS_USE_BACKSTAB.format(damagerProfile.getLocale()))
                            .add("{victim}", victim.getName())
                            .send(damager);
                } else {
                    new MessageFormat(Locale.CLASS_FAILED_BACKSTAB.format(damagerProfile.getLocale()))
                            .send(damager);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        lastJumpUsage.remove(event.getPlayer().getName());
        lastSpeedUsage.remove(event.getPlayer().getName());
        backstabCooldown.remove(event.getPlayer().getName());
    }

}
