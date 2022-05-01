package rip.crystal.practice.game.kit;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import rip.crystal.practice.Locale;
import rip.crystal.practice.game.kit.menu.KitEditorSelectKitMenu;
import rip.crystal.practice.game.kit.menu.KitManagementMenu;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.hotbar.impl.HotbarItem;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class KitEditorListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (profile.getKitEditorData().isRenaming()) {
			event.setCancelled(true);

			if (event.getMessage().length() > 16) {

				new MessageFormat(Locale.KIT_EDITOR_NAME_TOO_LONG
					.format(profile.getLocale()))
					.send(event.getPlayer());
				return;
			}

			String previousName = profile.getKitEditorData().getSelectedKitLoadout().getCustomName();
			String newName = CC.translate(event.getMessage());

			new MessageFormat(Locale.KIT_EDITOR_RENAMED
				.format(profile.getLocale()))
				.add("{previous_name}", previousName)
				.add("{new_name}", newName)
				.send(event.getPlayer());

			Kit selectedKit = profile.getKitEditorData().getSelectedKit();

			profile.getKitEditorData().setSelectedKit(null);
			profile.getKitEditorData().getSelectedKitLoadout().setCustomName(newName);
			profile.getKitEditorData().setActive(false);
			profile.getKitEditorData().setRename(false);

			if (profile.getState() != ProfileState.FIGHTING) {
				new KitManagementMenu(selectedKit).openMenu(event.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR ||
		                                event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			HotbarItem hotbarItem = Hotbar.fromItemStack(event.getItem());

			if (hotbarItem != null) {
				boolean cancelled = true;

				if (hotbarItem == HotbarItem.KIT_EDITOR) {
					Profile profile = Profile.get(event.getPlayer().getUniqueId());

					if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
						new KitEditorSelectKitMenu().openMenu(event.getPlayer());
					}
				} else {
					cancelled = false;
				}

				event.setCancelled(cancelled);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();

			if (event.getClickedInventory() != null && event.getClickedInventory() instanceof CraftingInventory) {
				if (player.getGameMode() != GameMode.CREATIVE) {
					event.setCancelled(true);
					return;
				}
			}

			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getState() != ProfileState.FIGHTING && player.getGameMode() == GameMode.SURVIVAL) {
				Inventory clicked = event.getClickedInventory();

				if (profile.getKitEditorData().isActive()) {
					if (clicked == null) {
						event.setCancelled(true);
						event.setCursor(null);
						player.updateInventory();
					} else if (clicked.equals(player.getOpenInventory().getTopInventory())) {
						if (event.getCursor().getType() != Material.AIR &&
						    event.getCurrentItem().getType() == Material.AIR ||
						    event.getCursor().getType() != Material.AIR &&
						    event.getCurrentItem().getType() != Material.AIR) {
							event.setCancelled(true);
							event.setCursor(null);
							player.updateInventory();
						}
					}
				} /*else {
					if (clicked != null && clicked.equals(player.getInventory())) {
						event.setCancelled(true);
					}
				}*/
			}
		}
	}

}
