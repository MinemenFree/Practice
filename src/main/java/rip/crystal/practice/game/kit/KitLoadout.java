package rip.crystal.practice.game.kit;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public class KitLoadout {

	private String customName = "Default";
	private ItemStack[] armor;
	private ItemStack[] contents;

	public KitLoadout() {
		this.armor = new ItemStack[4];
		this.contents = new ItemStack[36];
	}

	public KitLoadout(String customName) {
		this.customName = customName;
		this.armor = new ItemStack[4];
		this.contents = new ItemStack[36];
	}
}
