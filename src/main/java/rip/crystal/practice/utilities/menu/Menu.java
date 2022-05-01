package rip.crystal.practice.utilities.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.chat.CC;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Getter
@Setter
public abstract class Menu {

	public static Map<String, Menu> currentlyOpenedMenus = new HashMap<>();

	@Getter protected cPractice plugin = cPractice.get();
	private Map<Integer, Button> buttons = new HashMap<>();
	private boolean autoUpdate = false;
	private boolean updateAfterClick = true;
	private boolean closedByMenu = false;
	private boolean placeholder = false;
	private Button placeholderButton = Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15, " ");

	private ItemStack createItemStack(Player player, Button button) {
		ItemStack item = button.getButtonItem(player);

		if (item.getType() != Material.SKULL_ITEM) {
			ItemMeta meta = item.getItemMeta();

			if (meta != null && meta.hasDisplayName()) {
				meta.setDisplayName(meta.getDisplayName());
			}

			item.setItemMeta(meta);
		}

		return item;
	}

	public void openMenu(final Player player) {
		this.buttons = this.getButtons(player);

		Menu previousMenu = Menu.currentlyOpenedMenus.get(player.getName());
		Inventory inventory = null;
		int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
		boolean update = false;
		String title = CC.translate(this.getTitle(player));

		if (title.length() > 32) title = title.substring(0, 32);

		if (player.getOpenInventory() != null) {
			if (previousMenu == null) {
				player.closeInventory();
			} else {
				int previousSize = player.getOpenInventory().getTopInventory().getSize();

				if (previousSize == size && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
					inventory = player.getOpenInventory().getTopInventory();
					update = true;
				} else {
					previousMenu.setClosedByMenu(true);
					player.closeInventory();
				}
			}
		}

		if (inventory == null) inventory = Bukkit.createInventory(player, size, title);
		/*inventory.setContents(new ItemStack[inventory.getSize()]);
		ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
		itemStack.setDurability((short) 15);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(" ");
		itemStack.setItemMeta(itemMeta);
		for(int i = 0; i < inventory.getSize(); i++){
			inventory.setItem(i, itemStack);
		}*/
		currentlyOpenedMenus.put(player.getName(), this);

		for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
			inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
		}

		if (this.isPlaceholder()) {
			for (int index = 0; index < size; index++) {
				if (this.buttons.get(index) == null) {
					this.buttons.put(index, this.placeholderButton);
					inventory.setItem(index, this.placeholderButton.getButtonItem(player));
				}
			}
		}

		if (update) {
			player.updateInventory();
		} else {
			player.openInventory(inventory);
		}

		this.onOpen(player);
		this.setClosedByMenu(false);
	}

	public int size(Map<Integer, Button> buttons) {
		int highest = 0;

		for (int buttonValue : buttons.keySet()) {
			if (buttonValue > highest) {
				highest = buttonValue;
			}
		}

		return (int) (Math.ceil((highest + 1) / 9D) * 9D);
	}

	protected void bottomTopButtons(final boolean full, final Map buttons, final ItemStack itemStack) {
		IntStream.range(0, this.getSize()).filter(slot -> buttons.get(slot) == null).forEach(slot -> {
			if (slot < 9 || slot > this.getSize() - 10 || (full && (slot % 9 == 0 || (slot + 1) % 9 == 0))) {
				buttons.put(slot, new Button() {
					ItemStack itemStack;

					@Override
					public ItemStack getButtonItem(final Player player) {
						return this.itemStack;
					}
				});
			}
		});
	}

	public void fillEmptySlots(Map<Integer, Button> buttons, final ItemStack itemStack) {
		int bound = this.getSize();
		for (int slot = 0; slot < bound; ++slot) {
			if (buttons.get(slot) != null) continue;
			buttons.put(slot, new Button() {

				@Override
				public ItemStack getButtonItem(Player player) {
					return itemStack;
				}
			});
		}
	}

	public int getSize() {
		return -1;
	}

	public int getSlot(int x, int y) {
		return ((9 * y) + x);
	}

	public abstract String getTitle(Player player);

	public abstract Map<Integer, Button> getButtons(Player player);

	public void onOpen(Player player) {
	}

	public void onClose(Player player) {
	}

}
