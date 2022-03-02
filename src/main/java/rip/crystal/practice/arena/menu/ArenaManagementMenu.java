package rip.crystal.practice.arena.menu;

import com.google.common.collect.Maps;
import rip.crystal.practice.arena.Arena;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ArenaManagementMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&c&lArena Management";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		for (Arena arena : Arena.getArenas()) {
			//if (arena.isActive()) {
				buttons.put(buttons.size(), new SelectArenaButton(arena));
			//}
		}
		return buttons;
	}

	@AllArgsConstructor
	private static class SelectArenaButton extends Button {

		private final Arena arena;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(arena.getDisplayIcon())
					.name("&c" + arena.getName())
					.lore("")
					.lore("&cArena Information:")
					.lore(" &c↣ &fIs Setup: &c" + arena.isSetup())
					.lore(" &c↣ &fActive: &c" + arena.isActive())
					.lore(" &c↣ &fType: &c" + arena.getType().toString())
					.lore("")
					.lore("&c&lLEFT-CLICK &cto teleport to arena.")
					.lore("&c&lRIGHT-CLICK &cto see arena status.")
					.lore("&c&lMIDDLE-CLICK &cto delete arena.")
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			// Set closed by menu
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			// Force close inventory
			player.closeInventory();

			/*if(clickType.isLeftClick()) {
				player.performCommand("arena teleport " + arena.getName());
			}
			if(clickType.isRightClick()) {
				player.performCommand("arena status " + arena.getName());
			}*/
			switch (clickType) {
				case LEFT:
					player.performCommand("arena teleport " + arena.getName());
					player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
					break;
				case RIGHT:
					player.performCommand("arena status " + arena.getName());
					player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
					break;
				case MIDDLE:
					player.performCommand("arena delete " + arena.getName());
					player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
					break;
			}
		}

	}

}
