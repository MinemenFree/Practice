package rip.crystal.practice.player.party.classes.bard;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.party.classes.HCFClass;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.utilities.MessageFormat;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BardListener implements Listener {

    ImmutableMap<Material, BardEffect> interactItems = ImmutableMap.<Material, BardEffect>builder()
            .put(Material.BLAZE_POWDER, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 1), 45))
            .put(Material.SUGAR, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.SPEED, 20 * 6, 2), 40))
            .put(Material.FEATHER, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.JUMP, 20 * 5, 6), 40))
            .put(Material.IRON_INGOT, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 2), 40))
            .put(Material.GHAST_TEAR, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 2), 40))
            .put(Material.MAGMA_CREAM, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 6, 1), 40))
            //.put(Material.BLAZE_POWDER, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 1), 40))
            .put(Material.WHEAT, BardEffect.fromEnergy(25))
            .put(Material.SPIDER_EYE, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.WITHER, 20 * 5, 1), 35)).build();

    ImmutableMap<Material, BardEffect> passiveEffects = ImmutableMap.<Material, BardEffect>builder()
            .put(Material.BLAZE_POWDER, BardEffect.fromPotion(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 0)))
            .put(Material.SUGAR, BardEffect.fromPotion(new PotionEffect(PotionEffectType.SPEED, 20 * 6, 1)))
            .put(Material.FEATHER, BardEffect.fromPotion(new PotionEffect(PotionEffectType.JUMP, 20 * 6, 1)))
            .put(Material.IRON_INGOT, BardEffect.fromPotion(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 6, 0)))
            .put(Material.GHAST_TEAR, BardEffect.fromPotion(new PotionEffect(PotionEffectType.REGENERATION, 20 * 6, 0)))
            .put(Material.MAGMA_CREAM, BardEffect.fromPotion(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 6, 0))).build();

    @Getter
    private static final Map<String, Long> lastEffectUsage = new ConcurrentHashMap<>();
    private static final Set<PotionEffectType> DEBUFFS = ImmutableSet
            .of(PotionEffectType.POISON,
                    PotionEffectType.SLOW,
                    PotionEffectType.WEAKNESS,
                    PotionEffectType.HARM,
                    PotionEffectType.WITHER);
    private static final Table<UUID, PotionEffectType, PotionEffect> restores = HashBasedTable.create();

    public static final int BARD_RANGE = 20;
    public static final int EFFECT_COOLDOWN = 10 * 1000;


    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        if (!HCFClass.BARD.isApply(player)){
            return;
        }
        if (player.getItemInHand() != null && passiveEffects.containsKey(player.getItemInHand().getType())) {
            // CUSTOM
            if (player.getItemInHand().getType() == Material.FERMENTED_SPIDER_EYE && getLastEffectUsage().containsKey(player.getName()) && getLastEffectUsage().get(player.getName()) > System.currentTimeMillis()) {
                return;
            }

            giveBardEffect(player, passiveEffects.get(player.getItemInHand().getType()), true, false);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().name().contains("RIGHT_") || !event.hasItem() ||
                !interactItems.containsKey(event.getItem().getType()) ||
                !HCFClass.BARD.isApply(player) || !BardEnergyTask.getEnergy().containsKey(player.getName()))
            return;

        Profile profile = Profile.get(player.getUniqueId());
        Match match = profile.getMatch();
        if (match == null || profile.getParty() == null) return;

        if (getLastEffectUsage().containsKey(player.getName()) && getLastEffectUsage().get(player.getName()) > System.currentTimeMillis() &&
                player.getGameMode() != GameMode.CREATIVE) {
            long millisLeft = getLastEffectUsage().get(player.getName()) - System.currentTimeMillis();

            double value = (millisLeft / 1000D);
            double sec = Math.round(10.0 * value) / 10.0;

            new MessageFormat(Locale.CLASS_CANNOT_USE_ITEM.format(profile.getLocale()))
                    .add("{seconds}", String.valueOf(sec))
                    .send(player);
            return;
        }

        BardEffect bardEffect = interactItems.get(event.getItem().getType());

        if (bardEffect.getEnergy() > BardEnergyTask.getEnergy().get(player.getName())) {
            new MessageFormat(Locale.CLASS_BARD_ENOUGH_ENERGY.format(profile.getLocale()))
                    .add("{needed-energy}", String.valueOf(bardEffect.getEnergy()))
                    .add("{energy}", String.valueOf(BardEnergyTask.getEnergy().get(player.getName()).intValue()))
                    .send(player);
            return;
        }

        BardEnergyTask.getEnergy().put(player.getName(), BardEnergyTask.getEnergy().get(player.getName()) - bardEffect.getEnergy());

        boolean negative = bardEffect.getPotionEffect() != null && DEBUFFS.contains(bardEffect.getPotionEffect().getType());

        getLastEffectUsage().put(player.getName(), System.currentTimeMillis() + EFFECT_COOLDOWN);
        giveBardEffect(player, bardEffect, !negative, true);
        new MessageFormat(Locale.CLASS_CUSTOM_ITEM_USE.format(profile.getLocale()))
                .add("{effect}", bardEffect.getPotionEffect().getType().getName().toLowerCase())
                .send(player);

        if (player.getItemInHand().getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR));
            player.updateInventory();
        } else {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        }
    }

    public void giveBardEffect(Player source, BardEffect bardEffect, boolean friendly, boolean persistOldValues) {
        for (Player player : getNearbyPlayers(source, friendly)) {

            // CUSTOM
            // Bards can't get Strength.
            // Yes, that does need to use .equals. PotionEffectType is NOT an enum.
            if (HCFClass.BARD.isApply(player) && bardEffect.getPotionEffect() != null && bardEffect.getPotionEffect().getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                continue;
            }

            if (bardEffect.getPotionEffect() != null) {
                smartAddPotion(player, bardEffect.getPotionEffect(), persistOldValues);
            } else {
                Material material = source.getItemInHand().getType();
                giveCustomBardEffect(player, material);
            }
        }
    }

    public static void smartAddPotion(final Player player, PotionEffect potionEffect, boolean persistOldValues) {
        setRestoreEffect(player, potionEffect);
    }

    @AllArgsConstructor
    public static class SavedPotion {

        @Getter PotionEffect potionEffect;
        @Getter long time;
        @Getter private boolean perm;

    }

    public static void setRestoreEffect(Player player, PotionEffect effect) {
        boolean shouldCancel = true;
        Collection<PotionEffect> activeList = player.getActivePotionEffects();
        for (PotionEffect active : activeList) {
            if (!active.getType().equals(effect.getType())) continue;

            // If the current potion effect has a higher amplifier, ignore this one.
            if (effect.getAmplifier() < active.getAmplifier()) {
                return;
            } else if (effect.getAmplifier() == active.getAmplifier()) {
                // If the current potion effect has a longer duration, ignore this one.
                if (0 < active.getDuration() && (effect.getDuration() <= active.getDuration() || effect.getDuration() - active.getDuration() < 10)) {
                    return;
                }
            }

            restores.put(player.getUniqueId(), active.getType(), active);
            shouldCancel = false;
            break;
        }

        // Cancel the previous restore.
        player.addPotionEffect(effect, true);
        if (shouldCancel && effect.getDuration() > 120 && effect.getDuration() < 9600) {
            restores.remove(player.getUniqueId(), effect.getType());
        }
    }

    public void giveCustomBardEffect(Player player, Material material) {
        switch (material) {
            case WHEAT:
                for (Player nearbyPlayer : getNearbyPlayers(player, true)) {
                    nearbyPlayer.setFoodLevel(20);
                    nearbyPlayer.setSaturation(10F);
                }

                break;
            case FERMENTED_SPIDER_EYE:


                break;
            default:
                cPractice.get().getLogger().warning("No custom Bard effect defined for " + material + ".");
        }
    }

    public List<Player> getNearbyPlayers(Player player, boolean friendly) {
        List<Player> valid = new ArrayList<>();
        Profile profile = Profile.get(player.getUniqueId());

        Party sourceTeam = profile.getParty();
        Match match = profile.getMatch();

        // We divide by 2 so that the range isn't as much on the Y level (and can't be abused by standing on top of / under events)
        for (Entity entity : player.getNearbyEntities(BARD_RANGE, BARD_RANGE / 2, BARD_RANGE)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;

                if (sourceTeam == null) {
                    if (!friendly) {
                        valid.add(nearbyPlayer);
                    }

                    continue;
                }

                GameParticipant<MatchGamePlayer> gameParticipant = match.getParticipant(player);

                boolean isFriendly = !match.getGamePlayer(nearbyPlayer).isDead() &&
                    gameParticipant.containsPlayer(nearbyPlayer.getUniqueId());

                if (friendly && isFriendly) {
                    valid.add(nearbyPlayer);
                } else if (!friendly && !isFriendly) { // the isAlly is here so you can't give your allies negative effects, but so you also can't give them positive effects.
                    valid.add(nearbyPlayer);
                }
            }
        }

        valid.add(player);
        return (valid);
    }
}
