package rip.crystal.practice.match.menu;

import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import rip.crystal.practice.utilities.InventoryUtil;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.PotionUtil;
import rip.crystal.practice.utilities.TimeUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;
import rip.crystal.practice.utilities.menu.button.DisplayButton;

import java.util.*;

public class ViewInventoryMenu extends Menu {

	private final Player target;

	public ViewInventoryMenu(Player target) {
		this.target = target;
	}

	@Override
	public String getTitle(Player player) {
		return CC.RED + target.getName() + "'s Inventory";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		if (player == null) {
			return buttons;
		}

		ItemStack[] fixedContents = InventoryUtil.fixInventoryOrder(this.target.getInventory().getContents());

		for (int i = 0; i < fixedContents.length; ++i) {
			ItemStack itemStack = fixedContents[i];

			if (itemStack != null) {
				if (itemStack.getType() != Material.AIR) {
					buttons.put(i, new DisplayButton(itemStack, true));
				}
			}
		}

		for (int i = 0; i < target.getInventory().getArmorContents().length; ++i) {
			ItemStack itemStack = target.getInventory().getArmorContents()[i];

			if (itemStack != null && itemStack.getType() != Material.AIR) {
				buttons.put(39 - i, new DisplayButton(itemStack, true));
			}
		}

		int pos = 45;
		buttons.put(pos++, new HealthButton(target.getHealth() == 0.0 ? 0 : (int) Math.round(target.getHealth() / 2.0)));
		buttons.put(pos++, new HungerButton(target.getFoodLevel()));
		buttons.put(pos, new EffectsButton(target.getActivePotionEffects()));
		return buttons;
	}

	@Override
	public boolean isAutoUpdate() {
		return true;
	}

	@AllArgsConstructor
	private static class HealthButton extends Button {

		private final int health;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.MELON)
					.name("&cHealth: &f" + health + "/10 " + StringEscapeUtils.unescapeJava("\u2764"))
					.amount(health == 0 ? 1 : health)
					.build();
		}

	}

	@AllArgsConstructor
	private static class HungerButton extends Button {

		private final int hunger;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.COOKED_BEEF)
					.name("&cHunger: &f" + hunger + "/20")
					.amount(hunger == 0 ? 1 : hunger)
					.build();
		}

	}

	@AllArgsConstructor
	private static class EffectsButton extends Button {

		private final Collection<PotionEffect> effects;

		@Override
		public ItemStack getButtonItem(Player player) {
			ItemBuilder builder = new ItemBuilder(Material.POTION).name("&cPotion Effects");

			if (effects.isEmpty()) {
				builder.lore(CC.RED + "No effects");
			} else {
				List<String> lore = new ArrayList<>();

				effects.forEach(effect -> {
					String name = PotionUtil.getName(effect.getType()) + " " + (effect.getAmplifier() + 1);
					String duration = " (" + TimeUtil.millisToTimer(effect.getDuration() / 20 * 1000L) + ")";
					lore.add(CC.RED + name + CC.GRAY + duration);
				});

				builder.lore(lore);
			}

			return builder.build();
		}

	}

}
