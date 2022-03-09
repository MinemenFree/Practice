package rip.crystal.practice.profile.meta.option.button;

import rip.crystal.practice.cosmetics.menu.CosmeticsMenu;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.menu.MatchMakingMenu;
import rip.crystal.practice.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.utilities.menu.Button;

import java.util.ArrayList;
import java.util.List;

public class MatchMakingOptionButton extends Button {

	@Override
	public ItemStack getButtonItem(Player player) {

		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add("&eClick to open Matchmaking Settings!");

		return new ItemBuilder(Material.LEATHER_CHESTPLATE)
				.name("&9&lMatchmaking Settings")
				.lore(lore)
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Profile profile = Profile.get(player.getUniqueId());
		new MatchMakingMenu().openMenu(player);
	}

}
