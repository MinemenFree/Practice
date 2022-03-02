package rip.crystal.practice.utilities;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder implements Listener {

	private final ItemStack is;

	public ItemBuilder(Material mat) {
		is = new ItemStack(mat);
	}

	public ItemBuilder(ItemStack is) {
		this.is = is;
	}

	public ItemBuilder amount(int amount) {
		is.setAmount(amount);
		return this;
	}

	public ItemBuilder color(Color color) {
		if (is.getType() == Material.LEATHER_BOOTS || is.getType() == Material.LEATHER_CHESTPLATE || is.getType() == Material.LEATHER_HELMET
			|| is.getType() == Material.LEATHER_LEGGINGS) {
			LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
			meta.setColor(color);
			is.setItemMeta(meta);
			return this;
		} else {
			throw new IllegalArgumentException("color() only applicable for leather armor!");
		}
	}

	public ItemBuilder name(String name) {
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		is.setItemMeta(meta);
		return this;
	}

	public ItemBuilder lore(String name) {
		ItemMeta meta = is.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.add(ChatColor.translateAlternateColorCodes('&', name));
		meta.setLore(lore);

		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(String... lore) {
		List<String> toSet = new ArrayList<>();
		ItemMeta meta = is.getItemMeta();

		for (String string : lore) {
			toSet.add(ChatColor.translateAlternateColorCodes('&', string));
		}

		meta.setLore(toSet);
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(List<String> lore) {
		List<String> toSet = new ArrayList<>();
		ItemMeta meta = is.getItemMeta();

		for (String string : lore) {
			toSet.add(ChatColor.translateAlternateColorCodes('&', string));
		}

		meta.setLore(toSet);
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder durability(int durability) {
		is.setDurability((short) durability);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		is.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment) {
		is.addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder data(int dur) {
		is.setDurability((short) dur);
		return this;
	}

		public ItemBuilder type(Material material) {
		is.setType(material);
		return this;
	}

	public ItemBuilder clearLore() {
		ItemMeta meta = is.getItemMeta();

		meta.setLore(new ArrayList<>());
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder clearEnchantments() {
		for (Enchantment e : is.getEnchantments().keySet()) {
			is.removeEnchantment(e);
		}

		return this;
	}

	public ItemBuilder addItemFlag(ItemFlag itemFlag){
		ItemMeta meta = is.getItemMeta();
		meta.addItemFlags(itemFlag);
		is.setItemMeta(meta);

		return this;
	}

	public ItemBuilder addItemFlags(ItemFlag... itemFlag){
		ItemMeta meta = is.getItemMeta();
		meta.addItemFlags(itemFlag);
		is.setItemMeta(meta);

		return this;
	}

	public ItemStack build() {
		addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
		return is;
	}

}