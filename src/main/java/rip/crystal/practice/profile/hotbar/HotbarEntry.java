package rip.crystal.practice.profile.hotbar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class HotbarEntry {

    private ItemStack itemStack;
    private int slot;

}
