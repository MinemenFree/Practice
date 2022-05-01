package rip.crystal.practice.player.party.classes.archer;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.player.party.classes.HCFClass;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.Pair;
import rip.crystal.practice.utilities.chat.CC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ArcherClass implements Listener {
    public static final int MARK_SECONDS = 10;

    private static final Map<String, Long> lastSpeedUsage = new HashMap<>();
    private static final Map<String, Long> lastJumpUsage = new HashMap<>();
    @Getter
    private static final Map<String, Long> markedPlayers = new ConcurrentHashMap<>();

    @Getter private static final Map<String, Set<Pair<String, Long>>> markedBy = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityArrowHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            final Player player = (Player) event.getEntity();

            if (!(arrow.getShooter() instanceof Player)) {
                return;
            }

            Player shooter = (Player) arrow.getShooter();
            float pullback = arrow.getMetadata("Pullback").get(0).asFloat();

            if (!HCFClass.ARCHER.isApply(shooter)) {
                return;
            }

            // The 'ShotFromDistance' metadata is applied in the deathmessage module.
            Location shotFrom = shooter.getLocation();
            double distance = roundToHalf(shotFrom.distance(player.getLocation()));

            double damage = roundToHalf(2 * (distance / 10));

            if(damage > 12) damage = 12;

            if (player.getHealth() - damage <= 0D) {
                event.setCancelled(true);
            } else {
                event.setDamage(0D);
            }

            //DeathMessageHandler.addDamage(player, new ArrowTracker.ArrowDamageByPlayer(player.getName(), damage, ((Player) arrow.getShooter()).getName(), shotFrom, distance));
            double health = player.getHealth() - damage;
            if(health <= 0D){
                player.setHealth(0);
            }else {
                player.setHealth(health);
            }

            shooter.sendMessage(CC.translate("&6Archer hit: &6(&a" + distance + " blocks&6) &6-> &aTook &e" +
                roundToHalf(damage / 2) + " &4❤ &aof damage"));


            if (HCFClass.ARCHER.isApply(player)) {
                shooter.sendMessage(ChatColor.YELLOW + "[" + ChatColor.BLUE + "Arrow Range" + ChatColor.YELLOW + " (" + ChatColor.RED + (int) distance + ChatColor.YELLOW + ")] " + ChatColor.RED + "Cannot mark other Archers. " + ChatColor.BLUE + ChatColor.BOLD + "(" + damage / 2 + " heart" + ((damage / 2 == 1) ? "" : "s") + ")");
            } else if (pullback >= 0.5F) {
                shooter.sendMessage(ChatColor.YELLOW + "[" + ChatColor.BLUE + "Arrow Range" + ChatColor.YELLOW + " (" + ChatColor.RED + (int) distance + ChatColor.YELLOW + ")] " + ChatColor.GOLD + "Marked player for " + MARK_SECONDS + " seconds. " + ChatColor.BLUE + ChatColor.BOLD + "(" + damage / 2 + " heart" + ((damage / 2 == 1) ? "" : "s") + ")");

                // Only send the message if they're not already marked.
                if (!isMarked(player)) {
                    player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Marked! " + ChatColor.YELLOW + "An archer has shot you and marked you (+25% damage) for " + MARK_SECONDS + " seconds.");
                }

                getMarkedPlayers().put(player.getName(), System.currentTimeMillis() + (MARK_SECONDS * 1000));

                getMarkedBy().putIfAbsent(shooter.getName(), new HashSet<>());
                getMarkedBy().get(shooter.getName()).add(new Pair<>(player.getName(), System.currentTimeMillis() + (MARK_SECONDS * 1000)));
            } else {
                shooter.sendMessage(ChatColor.YELLOW + "[" + ChatColor.BLUE + "Arrow Range" + ChatColor.YELLOW + " (" + ChatColor.RED + (int) distance + ChatColor.YELLOW + ")] " + ChatColor.RED + "Bow wasn't fully drawn back. " + ChatColor.BLUE + ChatColor.BOLD + "(" + damage / 2 + " heart" + ((damage / 2 == 1) ? "" : "s") + ")");
            }
        }
    }

    public double roundToHalf(double x) {
        return (Math.ceil(x * 2) / 2);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (isMarked(player)) {
                Player damager = null;
                if (event.getDamager() instanceof Player) {
                    damager = (Player) event.getDamager();
                } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    damager = (Player) ((Projectile) event.getDamager()).getShooter();
                }

                if (damager != null && !canUseMark(damager, player)) {
                    return;
                }

                // Apply 125% damage if they're 'marked'
                event.setDamage(event.getDamage() * 1.25D);
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        event.getProjectile().setMetadata("ShotFromDistance", new FixedMetadataValue(cPractice.get(), event.getProjectile().getLocation()));
        event.getProjectile().setMetadata("Pullback", new FixedMetadataValue(cPractice.get(), event.getForce()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT_") || !event.hasItem() || !HCFClass.ARCHER.isApply(event.getPlayer()))
            return;
        Player player = event.getPlayer();
        if (event.getItem().getType()!= Material.SUGAR || event.getItem().getType() != Material.FEATHER) return;

        Profile profile = Profile.get(event.getPlayer().getUniqueId());
        Match match = profile.getMatch();
        if (match == null || profile.getParty() == null) return;

        itemConsumed(player, event.getItem().getType());
    }

    public boolean itemConsumed(Player player, Material material) {
        Profile profile = Profile.get(player.getUniqueId());
        if (material == Material.SUGAR) {
            if (lastSpeedUsage.containsKey(player.getName()) && lastSpeedUsage.get(player.getName()) > System.currentTimeMillis()) {
                long millisLeft = lastSpeedUsage.get(player.getName()) - System.currentTimeMillis();
                int time = (int) millisLeft / 1000;

                new MessageFormat(Locale.CLASS_CANNOT_USE_ITEM.format(profile.getLocale()))
                        .add("{seconds}", String.valueOf(time))
                        .send(player);
                return (false);
            }

            lastSpeedUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3), true);
            new MessageFormat(Locale.CLASS_CUSTOM_ITEM_USE.format(profile.getLocale()))
                    .add("{effect}", "speed")
                    .send(player);
            return (true);
        } else {
            if (lastJumpUsage.containsKey(player.getName()) && lastJumpUsage.get(player.getName()) > System.currentTimeMillis()) {
                long millisLeft = lastJumpUsage.get(player.getName()) - System.currentTimeMillis();
                int time = (int) millisLeft / 1000;

                new MessageFormat(Locale.CLASS_CANNOT_USE_ITEM.format(profile.getLocale()))
                        .add("{seconds}", String.valueOf(time))
                        .send(player);
                return (false);
            }

            lastJumpUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 5, 4));
            new MessageFormat(Locale.CLASS_CUSTOM_ITEM_USE.format(profile.getLocale()))
                    .add("{effect}", "jump")
                    .send(player);
            return (false);
        }
    }

    public static boolean isMarked(Player player) {
        return (getMarkedPlayers().containsKey(player.getName()) && getMarkedPlayers().get(player.getName()) > System.currentTimeMillis());
    }

    private boolean canUseMark(Player player, Player victim) {
        if (markedBy.containsKey(player.getName())) {
            for (Pair<String, Long> pair : markedBy.get(player.getName())) {
                if (victim.getName().equals(pair.getLeft()) && pair.getRight() > System.currentTimeMillis()) {
                    return false;
                }
            }
        }
        return true;
    }
}
