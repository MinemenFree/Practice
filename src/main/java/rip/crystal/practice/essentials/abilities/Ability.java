package rip.crystal.practice.essentials.abilities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.Practice;
import rip.crystal.practice.utilities.Utils;
import rip.crystal.practice.utilities.chat.CC;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class Ability implements Listener {

    @Getter
    private static final List<Ability> abilities = Lists.newArrayList();

    private final String ability;
    private Table<String, UUID, Long> cooldown = HashBasedTable.create();

    public Ability(String ability) {
        this.ability = ability;
        abilities.add(this);
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Practice.get());
    }

    public boolean isAbility(ItemStack itemStack) {
        return (itemStack != null)
                && (itemStack.getType() != Material.AIR)
                && (itemStack.hasItemMeta())
                && (itemStack.getItemMeta().getDisplayName() != null)
                && (itemStack.getItemMeta().getLore() != null)
                && itemStack.getItemMeta().getDisplayName().equals(CC.translate(
                Practice.get().getAbilityManager().getDisplayName(ability)))
                && itemStack.getItemMeta().getLore().equals(CC.translate(
                Practice.get().getAbilityManager().getDescription(ability)));
    }

    public String getName() {
        return Practice.get().getAbilityManager().getDisplayName(this.getAbility());
    }

    public boolean hasCooldown(Player player) {
        return this.cooldown.contains(Practice.get().getAbilityManager().getDisplayName(this.getAbility()), player.getUniqueId())
                && this.cooldown.get(Practice.get().getAbilityManager().getDisplayName(this.getAbility()), player.getUniqueId()) > System.currentTimeMillis();
    }

    public void setCooldown(Player player, long time) {
        if (time < 1L) {
            this.cooldown.remove(Practice.get().getAbilityManager().getDisplayName(this.getAbility()), player.getUniqueId());
        }
        else {
            this.cooldown.put(Practice.get().getAbilityManager().getDisplayName(this.getAbility()), player.getUniqueId(), System.currentTimeMillis() + time);
        }
    }
    public String getCooldown(Player player) {
        long cooldownLeft = this.cooldown.get(Practice.get().getAbilityManager().getDisplayName(this.getAbility()), player.getUniqueId()) - System.currentTimeMillis();
        return Utils.formatLongMin(cooldownLeft);
    }
}
