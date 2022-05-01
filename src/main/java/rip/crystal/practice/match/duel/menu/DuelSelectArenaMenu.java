package rip.crystal.practice.match.duel.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.ArenaType;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class DuelSelectArenaMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return cPractice.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Profile profile = Profile.get(player.getUniqueId());

		Map<Integer, Button> buttons = new HashMap<>();

		for (Arena arena : Arena.getArenas()) {
			if (!arena.isSetup()) continue;

			if (!arena.getKits().contains(profile.getDuelProcedure().getKit().getName())) continue;

			if (profile.getDuelProcedure().getKit().getGameRules().isBuild()) {
				if (arena.getType() == ArenaType.SHARED) continue;

				if (arena.getType() != ArenaType.STANDALONE) continue;

				if (arena.isActive()) continue;
			}

			buttons.put(buttons.size(), new SelectArenaButton(arena));
		}

		buttons.put(this.size(buttons) + 8, new RandomArenaButton());

		return buttons;
	}

	@Override
	public void onClose(Player player) {
		if (!isClosedByMenu()) {
			Profile profile = Profile.get(player.getUniqueId());
			profile.setDuelProcedure(null);
		}
	}

	@AllArgsConstructor
	private static class RandomArenaButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.valueOf(cPractice.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.RANDOM_MATERIAL")))
				.name(cPractice.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.RANDOM_NAME"))
				.build();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
			Profile profile = Profile.get(player.getUniqueId());

			// Update and request the procedure
			profile.getDuelProcedure().setArena(Arena.getRandomArena(profile.getDuelProcedure().getKit()));
			profile.getDuelProcedure().send();

			// Set closed by menu
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			// Force close inventory
			player.closeInventory();
		}
	}

	@AllArgsConstructor
	private static class SelectArenaButton extends Button {

		private final Arena arena;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.valueOf(cPractice.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.MATERIAL")))
					.name(cPractice.get().getLangConfig().getString("DUEL.SELECT.ARENA_MENU.NAME").replace("{name}", arena.getName()))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			// Update and request the procedure
			profile.getDuelProcedure().setArena(this.arena);
			profile.getDuelProcedure().send();

			// Set closed by menu
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			// Force close inventory
			player.closeInventory();
		}

	}

}
