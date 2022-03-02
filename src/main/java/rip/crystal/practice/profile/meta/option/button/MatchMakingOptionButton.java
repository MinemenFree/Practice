package rip.crystal.practice.profile.meta.option.button;

import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.meta.option.menu.ProfileOptionButton;
import rip.crystal.practice.utilities.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class MatchMakingOptionButton extends ProfileOptionButton {

	private static final List<Integer> PING_RANGES = Arrays.asList(
			50, 75, 100, 125, 150, 200, 250, 300, -1
	);

	@Override
	public String getOptionName() {
		return "&a&lMatch Making";
	}

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.ITEM_FRAME).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.ITEM_FRAME).build();
	}

	@Override
	public String getDescription() {
		return "test";
	}

	@Override
	public String getEnabledOption() {
		return "Ping Range: " + "test";
	}

	@Override
	public String getDisabledOption() {
		return "test no";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.get(player.getUniqueId()).getOptions().showScoreboard();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Profile profile = Profile.get(player.getUniqueId());
		//profile.getOptions().showScoreboard(!profile.getOptions().showScoreboard());
		Inventory inventory = player.getInventory();
		String[] args = ChatColor.stripColor("").split(":");
		//int range = handleRangeClick(clickType.isRightClick(), MatchMakingOptionButton.PING_RANGES, parseOrDefault(args[1], -1));
		//profile.setPingRange(range);
		player.sendMessage("Ping Range: " + (profile.getPingRange() == -1 ? "Unrestricted" : profile.getPingRange()));
		inventory.setItem(0, createItem(Material.STICK, "Ping Range: " + (profile.getPingRange() == -1 ? "Unrestricted" : profile.getPingRange())));
	}

	private int handleRangeClick(ClickType clickType, List<Integer> ranges, int current) {
		int min = ranges.get(0);
		int max = ranges.get(ranges.size() - 1);
		if(clickType.isRightClick()) {
			if (current == max) {
				current = min;
			} else {
				current = ranges.get(ranges.indexOf(current) + 1);
			}
		}

		return current;
	}

	private int parseOrDefault(String string, int def) {
		try {
			return Integer.parseInt(string.replace(" ", ""));
		} catch (NumberFormatException e) {
			return def;
		}
	}
	public static ItemStack createItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);

		item.setItemMeta(meta);
		return item;
	}
}
