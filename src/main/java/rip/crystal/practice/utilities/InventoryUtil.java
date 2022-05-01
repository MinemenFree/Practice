package rip.crystal.practice.utilities;

import com.google.common.collect.Lists;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import rip.crystal.practice.cPractice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InventoryUtil {

	public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeInt(items.length);
			for (int i = 0; i < items.length; ++i) {
				dataOutput.writeObject(items[i]);
			}
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}


	public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack[] items = new ItemStack[dataInput.readInt()];
			for (int i = 0; i < items.length; ++i) {
				items[i] = (ItemStack) dataInput.readObject();
			}
			dataInput.close();
			return items;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public static void saveItemStacksKit(String path, ItemStack[] source, boolean armor) {
		for (int b = 0; b < source.length; b++) {
			if (armor) {
				cPractice.get().getKitsConfig().getConfiguration().set(path + ".loadout.armor." + b, Arrays.asList(source).get(b));
			} else {
				cPractice.get().getKitsConfig().getConfiguration().set(path + ".loadout.contents." + b, Arrays.asList(source).get(b));
			}
		}
	}

	public static ItemStack[] serializeItemStackKit(String source) {
		List<ItemStack> items = Lists.newArrayList();

		cPractice.get().getKitsConfig().getConfiguration().getConfigurationSection(source).getKeys(false)
				.forEach(s ->
					items.add(cPractice.get().getKitsConfig().getConfiguration().getItemStack(source + "." + s)));

		return items.toArray(new ItemStack[items.size()]);
	}

	public static ItemStack[] fixInventoryOrder(ItemStack[] source) {
		ItemStack[] fixed = new ItemStack[36];

		System.arraycopy(source, 0, fixed, 27, 9);
		System.arraycopy(source, 9, fixed, 0, 27);

		return fixed;
	}

//	public static String serializeInventory(ItemStack[] source) {
//		StringBuilder builder = new StringBuilder();
//
//		for (ItemStack itemStack : source) {
//			builder.append(serializeItemStack(itemStack));
//			builder.append(";");
//		}
//
//		return builder.toString();
//	}
//
//	public static ItemStack[] deserializeInventory(String source) {
//		List<ItemStack> items = new ArrayList<>();
//		String[] split = source.split(";");
//
//		for (String piece : split) {
//			items.add(deserializeItemStack(piece));
//		}
//
//		return items.toArray(new ItemStack[0]);
//	}
//
//	public static String serializeItemStack(ItemStack item) {
//		StringBuilder builder = new StringBuilder();
//
//		if (item == null) {
//			return "null";
//		}
//
//		String isType = String.valueOf(item.getType().getId());
//		builder.append("t@").append(isType);
//
//		if (item.getDurability() != 0) {
//			String isDurability = String.valueOf(item.getDurability());
//			builder.append(":d@").append(isDurability);
//		}
//
//		if (item.getAmount() != 1) {
//			String isAmount = String.valueOf(item.getAmount());
//			builder.append(":a@").append(isAmount);
//		}
//
//		Map<Enchantment, Integer> enchantments = item.getEnchantments();
//
//		if (enchantments.size() > 0) {
//			for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
//				builder.append(":e@").append(enchantment.getKey().getId()).append("@").append(enchantment.getValue());
//			}
//		}
//
//		if (item.hasItemMeta()) {
//			ItemMeta itemMeta = item.getItemMeta();
//
//			if (itemMeta.hasDisplayName()) {
//				builder.append(":dn@").append(itemMeta.getDisplayName());
//			}
//
//			if (itemMeta.hasLore()) {
//				builder.append(":l@").append(itemMeta.getLore());
//			}
//		}
//
//		if (item.getType() == Material.POTION) {
//			Potion potion = Potion.fromItemStack(item);
//
//			builder.append(":pd@")
//					.append(potion.getType().getDamageValue())
//					.append("-")
//					.append(potion.getLevel());
//
//			for (PotionEffect effect : potion.getEffects()) {
//				builder.append("=")
//						.append(effect.getType().getId())
//						.append("-")
//						.append(effect.getDuration())
//						.append("-")
//						.append(effect.getAmplifier());
//			}
//		}
//
//		return builder.toString();
//	}
//
//	public static ItemStack deserializeItemStack(String in) {
//		ItemStack item = null;
//		ItemMeta meta = null;
//
//		if (in.equals("null")) {
//			return new ItemStack(Material.AIR);
//		}
//
//		String[] split = in.split(":");
//
//		for (String itemInfo : split) {
//			String[] itemAttribute = itemInfo.split("@");
//			String attributeId = itemAttribute[0];
//
//			switch (attributeId) {
//				case "t": {
//					item = new ItemStack(Material.getMaterial(Integer.parseInt(itemAttribute[1])));
//					meta = item.getItemMeta();
//					break;
//				}
//				case "d": {
//					if (item != null) {
//						item.setDurability(Short.parseShort(itemAttribute[1]));
//						break;
//					}
//					break;
//				}
//				case "a": {
//					if (item != null) {
//						item.setAmount(Integer.parseInt(itemAttribute[1]));
//						break;
//					}
//					break;
//				}
//				case "e": {
//					if (item != null) {
//						item.addUnsafeEnchantment(
//								Enchantment.getById(Integer.parseInt(itemAttribute[1])),
//								Integer.parseInt(itemAttribute[2])
//						);
//						break;
//					}
//					break;
//				}
//				case "dn": {
//					if (meta != null) {
//						meta.setDisplayName(CC.translate(itemAttribute[1]));
//						break;
//					}
//					break;
//				}
//				case "l": {
//					itemAttribute[1] = itemAttribute[1].replace("[", "");
//					itemAttribute[1] = itemAttribute[1].replace("]", "");
//					List<String> lore = Arrays.asList(itemAttribute[1].split(","));
//
//					for (int x = 0; x < lore.size(); ++x) {
//						String s = lore.get(x);
//
//						if (s != null) {
//							if (s.toCharArray().length != 0) {
//								if (s.charAt(0) == ' ') {
//									s = s.replaceFirst(" ", "");
//								}
//
//								lore.set(x, s);
//							}
//						}
//					}
//
//					if (meta != null) {
//						meta.setLore(lore);
//						break;
//					}
//
//					break;
//				}
//				case "pd": {
//					if (item != null && item.getType() == Material.POTION) {
//						String[] effectsList = itemAttribute[1].split("=");
//						String[] potionData = effectsList[0].split("-");
//
//						Potion potion = new Potion(PotionType.getByDamageValue(Integer.parseInt(potionData[0])),
//								Integer.parseInt(potionData[1]));
//						potion.setSplash(item.getDurability() >= 16000);
//
//						PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
//
//						for (int i = 1; i < effectsList.length; i++) {
//							String[] effectData = effectsList[1].split("-");
//
//							PotionEffect potionEffect = new PotionEffect(PotionEffectType.getById(
//									Integer.parseInt(effectData[0])), Double.valueOf(effectData[1]).intValue(),
//									Integer.parseInt(effectData[2]), false
//							);
//
//							potionMeta.addCustomEffect(potionEffect, true);
//						}
//
//						item = potion.toItemStack(item.getAmount());
//						item.setItemMeta(potionMeta);
//					}
//
//					break;
//				}
//			}
//		}
//
//		if (meta != null && (meta.hasDisplayName() || meta.hasLore())) {
//			item.setItemMeta(meta);
//		}
//
//		return item;
//	}

	public static void removeCrafting(Material material) {
		Iterator<Recipe> iterator = cPractice.get().getServer().recipeIterator();

		while (iterator.hasNext()) {
			Recipe recipe = iterator.next();

			if (recipe != null && recipe.getResult().getType() == material) {
				iterator.remove();
			}
		}
	}

	public static ItemStack[] leatherArmor(Color color){
		return new ItemStack[]{
				new ItemBuilder(Material.LEATHER_BOOTS).color(color).build(),
				new ItemBuilder(Material.LEATHER_LEGGINGS).color(color).build(),
				new ItemBuilder(Material.LEATHER_CHESTPLATE).color(color).build(),
				new ItemBuilder(Material.LEATHER_HELMET).color(color).build()
		};
	}
}
