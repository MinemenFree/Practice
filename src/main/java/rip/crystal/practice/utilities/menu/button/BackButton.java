package rip.crystal.practice.utilities.menu.button;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.Arrays;

@AllArgsConstructor
public class BackButton extends Button {

	private Menu back;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.REDSTONE)
				.name(CC.RED + CC.BOLD + "Back")
				.lore(Arrays.asList(
						CC.RED + "Click here to return to",
						CC.RED + "the previous menu.")
				)
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Button.playNeutral(player);
		back.openMenu(player);
	}

}
