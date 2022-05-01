package rip.crystal.practice.utilities.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

public class JumpToMenuButton extends Button {

	private final Menu menu;
	private final ItemStack itemStack;

	public JumpToMenuButton(Menu menu, ItemStack itemStack) {
		this.menu = menu;
		this.itemStack = itemStack;
	}

	@Override
	public ItemStack getButtonItem(Player player) {
		return itemStack;
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		menu.openMenu(player);
	}

}
