package rip.crystal.practice.game.kit.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.kit.KitLoadout;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;
import rip.crystal.practice.utilities.menu.button.BackButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KitManagementMenu extends Menu {

	private static final Button PLACEHOLDER = Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 7, " ");

	private final Kit kit;

	public KitManagementMenu(Kit kit) {
		this.kit = kit;

		setPlaceholder(true);
		setUpdateAfterClick(false);
	}

	@Override
	public String getTitle(Player player) {
		return cPractice.get().getKiteditorConfig().getString("KITEDITOR.MANAGE.TITLE").replace("{kit}", kit.getName());
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		Profile profile = Profile.get(player.getUniqueId());
		KitLoadout[] kitLoadouts = profile.getKitData().get(kit).getLoadouts();

		if (kitLoadouts == null) {
			return buttons;
		}

		int startPos = -1;

		for (int i = 0; i < 4; i++) {
			startPos += 2;

			KitLoadout kitLoadout = kitLoadouts[i];
			buttons.put(startPos, kitLoadout == null ? new CreateKitButton(i) : new KitDisplayButton(kitLoadout));
			buttons.put(startPos + 18, new LoadKitButton(i));
			buttons.put(startPos + 27, kitLoadout == null ? PLACEHOLDER : new RenameKitButton(kit, kitLoadout));
			buttons.put(startPos + 36, kitLoadout == null ? PLACEHOLDER : new DeleteKitButton(kit, kitLoadout));
		}

		buttons.put(36, new BackButton(new KitEditorSelectKitMenu()));

		return buttons;
	}

	@Override
	public void onClose(Player player) {
		if (!isClosedByMenu()) {
			Profile profile = Profile.get(player.getUniqueId());
			profile.getKitEditorData().setSelectedKit(null);
		}
	}

	@AllArgsConstructor
	private class DeleteKitButton extends Button {

		private Kit kit;
		private KitLoadout kitLoadout;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.STAINED_CLAY)
					.name(cPractice.get().getKiteditorConfig().getString("KITEDITOR.DeleteKitButton.NAME"))
					.durability(14)
					.lore(Arrays.asList(
							"&9Click to delete this kit.",
							"&9You will &lNOT &cbe able to",
							"&9recover this kitloadout."
					))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());
			profile.getKitData().get(kit).deleteKit(kitLoadout);

			new KitManagementMenu(kit).openMenu(player);
		}

	}

	@AllArgsConstructor
	private class CreateKitButton extends Button {

		private int index;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.IRON_SWORD)
					.name(cPractice.get().getKiteditorConfig().getString("KITEDITOR.CreateKitButton.NAME"))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());
			Kit kit = profile.getKitEditorData().getSelectedKit();

			// TODO: this shouldn't be null but sometimes it is?
			if (kit == null) {
				player.closeInventory();
				return;
			}

			KitLoadout kitLoadout = new KitLoadout("Kit " + (index + 1));

			if (kit.getKitLoadout() != null) {
				if (kit.getKitLoadout().getArmor() != null) {
					kitLoadout.setArmor(kit.getKitLoadout().getArmor());
				}

				if (kit.getKitLoadout().getContents() != null) {
					kitLoadout.setContents(kit.getKitLoadout().getContents());
				}
			}

			profile.getKitData().get(kit).replaceKit(index, kitLoadout);
			profile.getKitEditorData().setSelectedKitLoadout(kitLoadout);

			new KitEditorMenu(index).openMenu(player);
		}

	}

	@AllArgsConstructor
	private class RenameKitButton extends Button {

		private Kit kit;
		private KitLoadout kitLoadout;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.SIGN)
					.name(cPractice.get().getKiteditorConfig().getString("KITEDITOR.RenameKitButton.NAME"))
					.lore(cPractice.get().getKiteditorConfig().getString("KITEDITOR.RenameKitButton.LORE"))
					.build();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);
			Profile profile = Profile.get(player.getUniqueId());

			player.closeInventory();
			//player.sendMessage(Locale.KIT_EDITOR_START_RENAMING.format(kitLoadout.getCustomName()));

			new MessageFormat(Locale.KIT_EDITOR_START_RENAMING
				.format(profile.getLocale()))
				.add("{kit_name}", kitLoadout.getCustomName())
				.send(player);

			profile.getKitEditorData().setSelectedKit(kit);
			profile.getKitEditorData().setSelectedKitLoadout(kitLoadout);
			profile.getKitEditorData().setActive(true);
			profile.getKitEditorData().setRename(true);
		}

	}

	@AllArgsConstructor
	private class LoadKitButton extends Button {

		private int index;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.BOOK)
					.name(cPractice.get().getKiteditorConfig().getString("KITEDITOR.LoadKitButton.NAME"))
					.lore(cPractice.get().getKiteditorConfig().getString("KITEDITOR.LoadKitButton.LORE"))
					.build();
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
			Profile profile = Profile.get(player.getUniqueId());

			// TODO: this shouldn't be null but sometimes it is?
			if (profile.getKitEditorData().getSelectedKit() == null) {
				player.closeInventory();
				return;
			}

			KitLoadout kit = profile.getKitData().get(profile.getKitEditorData().getSelectedKit()).getLoadout(index);

			if (kit == null) {
				kit = new KitLoadout("Kit " + (index + 1));
				kit.setArmor(profile.getKitEditorData().getSelectedKit().getKitLoadout().getArmor());
				kit.setContents(profile.getKitEditorData().getSelectedKit().getKitLoadout().getContents());
				profile.getKitData().get(profile.getKitEditorData().getSelectedKit()).replaceKit(index, kit);
			}

			profile.getKitEditorData().setSelectedKitLoadout(kit);

			new KitEditorMenu(index).openMenu(player);
		}

	}

	@AllArgsConstructor
	private class KitDisplayButton extends Button {

		private KitLoadout kitLoadout;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.BOOK)
					.name(cPractice.get().getKiteditorConfig().getString("KITEDITOR.KitDisplayButton.NAME").replace("{kit}", kitLoadout.getCustomName()))
					.build();
		}

	}

}
