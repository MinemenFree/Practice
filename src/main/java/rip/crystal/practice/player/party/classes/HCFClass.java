package rip.crystal.practice.player.party.classes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Getter
public enum HCFClass {
    ARCHER("Archer",
            Lists.newArrayList(
                    new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2),
                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0)
            ),
            new Material[] {
                    Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
                    Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS}),
    ROGUE("Rogue",
            Lists.newArrayList(
                    new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1),
                    new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2)
            ),
            new Material[] {
                    Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
                    Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS}),
    BARD("BARD",
            Lists.newArrayList(
                    new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1),
                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1),
                    new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0)
            ),
            new Material[] {
                    Material.GOLD_HELMET, Material.GOLD_CHESTPLATE,
                    Material.GOLD_LEGGINGS, Material.GOLD_BOOTS});

    final public static Map<UUID, HCFClass> classMap = Maps.newHashMap();

    private final String name;
    private final List<PotionEffect> effects;
    private final Material[] armor;

    public boolean isApply(Player player){
        return player.getInventory().getHelmet() != null &&
                player.getInventory().getHelmet().getType() == armor[0] &&
                player.getInventory().getChestplate() != null &&
                player.getInventory().getChestplate().getType() == armor[1] &&
                player.getInventory().getLeggings() != null &&
                player.getInventory().getLeggings().getType() == armor[2] &&
                player.getInventory().getBoots() != null &&
                player.getInventory().getBoots().getType() == armor[3];
    }

    public void equip(Player player){
        if (classMap.containsKey(player.getUniqueId())) return;
        classMap.put(player.getUniqueId(), this);
        effects.forEach(player::addPotionEffect);
    }
}
