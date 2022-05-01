package rip.crystal.practice.game.kit.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class KitEditorSelectKitMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return cPractice.get().getKiteditorConfig().getString("KITEDITOR.SELECT-KIT.TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(cPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(cPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
		this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

		Kit.getKits().forEach(kit -> {
			if (kit.isEnabled()) {
				buttons.put(kit.getSlot(), new KitDisplayButton(kit));
				//buttons.put(buttons.size(), new KitDisplayButton(kit));|
			}
//			if (kit.isEnabled() && !kit.getGameRules().isOnlyHCF()) {
//				buttons.put(buttons.size(), new KitDisplayButton(kit));
//			}
		});

		return buttons;
	}

	@Override
	public int getSize() {
		return 5/*cPractice.get().getMainConfig().getInteger("QUEUES.SIZE")*/ * 9;
	}

	@AllArgsConstructor
	private class KitDisplayButton extends Button {

		private Kit kit;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(kit.getDisplayIcon())
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.name(cPractice.get().getKiteditorConfig().getString("KITEDITOR.SELECT-KIT.NAMECOLOR") + kit.getName())
					.lore(cPractice.get().getKiteditorConfig().getStringList("KITEDITOR.SELECT-KIT.LORE"))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			player.closeInventory();
			Profile profile = Profile.get(player.getUniqueId());
			profile.getKitEditorData().setSelectedKit(kit);

			new KitManagementMenu(kit).openMenu(player);
		}

	}
}
