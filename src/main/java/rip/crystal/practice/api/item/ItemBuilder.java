package rip.crystal.practice.api.item;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import rip.crystal.practice.api.chat.ChatUtil;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
    }

    public ItemBuilder(String material) {
        this.itemStack = new ItemStack(Material.valueOf(material), 1);
    }

    public ItemBuilder(int material) {
        this.itemStack = new ItemStack(material, 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilder(Material material, int damage) {
        this.itemStack = new ItemStack(material, 1, (short) damage);
    }

    public ItemBuilder(Material material, int amount, int damage) {
        this.itemStack = new ItemStack(material, amount, (short) damage);
    }

    public ItemBuilder name(String name) {
        if (name != null) {
            name = ChatUtil.translate(name);
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.setDisplayName(name);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        if (lore != null) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.setLore(ChatUtil.translate(lore));
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder lore(String... lore) {
        if (lore != null) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.setLore(ChatUtil.translate(Arrays.asList(lore)));
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(boolean enchanted) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder enchant(boolean enchanted, int level) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, level, true);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder enchant(boolean enchanted, Enchantment enchant, int level) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.addEnchant(enchant, level, true);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder data(int dur) {
        this.itemStack.setDurability((short) dur);
        return this;
    }

    public ItemBuilder owner(String owner) {
        if (this.itemStack.getType() == Material.SKULL_ITEM) {
            SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
            meta.setOwner(owner);
            this.itemStack.setItemMeta(meta);
            return this;
        }

        throw new IllegalArgumentException("setOwner() only applicable for Skull Item");
    }

    public ItemBuilder armorColor(Color color) {
        try {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.itemStack.getItemMeta();
            leatherArmorMeta.setColor(color);
            this.itemStack.setItemMeta(leatherArmorMeta);
        }
        catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("Error set armor color.");
        }
        return this;
    }

    public ItemStack build() {
        return this.itemStack;
    }
}