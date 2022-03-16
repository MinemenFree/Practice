package rip.crystal.practice.player.profile.hotbar.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class HotbarEntry {

    private ItemStack itemStack;
    private int slot;

}
