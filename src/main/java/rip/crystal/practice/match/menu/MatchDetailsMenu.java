package rip.crystal.practice.match.menu;

import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import rip.crystal.practice.Locale;
import rip.crystal.practice.match.MatchSnapshot;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.*;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;
import rip.crystal.practice.utilities.menu.button.DisplayButton;

import java.util.*;

@AllArgsConstructor
public class MatchDetailsMenu extends Menu {

	private final MatchSnapshot snapshot;

	@Override
	public String getTitle(Player player) {
		return "&6Inventory of " + snapshot.getUsername();
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		ItemStack[] fixedContents = InventoryUtil.fixInventoryOrder(snapshot.getContents());

		for (int i = 0; i < fixedContents.length; i++) {
			ItemStack itemStack = fixedContents[i];

			if (itemStack != null && itemStack.getType() != Material.AIR) {
				buttons.put(i, new DisplayButton(itemStack, true));
			}
		}

		for (int i = 0; i < snapshot.getArmor().length; i++) {
			ItemStack itemStack = snapshot.getArmor()[i];

			if (itemStack != null && itemStack.getType() != Material.AIR) {
				buttons.put(39 - i, new DisplayButton(itemStack, true));
			}
		}

		int pos = 45;

		buttons.put(pos++, new HealthButton(snapshot.getHealth()));
		buttons.put(pos++, new HungerButton(snapshot.getHunger()));
		buttons.put(pos++, new EffectsButton(snapshot.getEffects()));

		if (snapshot.shouldDisplayRemainingPotions()) {
			buttons.put(pos++, new PotionsButton(snapshot.getUsername(), snapshot.getRemainingPotions()));
		}

		buttons.put(pos, new StatisticsButton(snapshot));

		if (this.snapshot.getOpponent() != null) {
			buttons.put(53, new SwitchInventoryButton(this.snapshot.getOpponent()));
		}

		return buttons;
	}

	@Override
	public void onOpen(Player player) {
		new MessageFormat(Locale.VIEWING_INVENTORY.format(Profile.get(player.getUniqueId()).getLocale()))
			.add("{target_name}", snapshot.getUsername())
			.send(player);
	}

	@AllArgsConstructor
	private class SwitchInventoryButton extends Button {

		private final UUID opponent;

		@Override
		public ItemStack getButtonItem(Player player) {
			MatchSnapshot snapshot = MatchSnapshot.getByUuid(opponent);

			if (snapshot != null) {
				return new ItemBuilder(Material.LEVER)
						.name("&cOpponent's Inventory")
						.lore("&fSwitch to &c" + snapshot.getUsername() + "&f's inventory")
						.build();
			} else {
				return new ItemStack(Material.AIR);
			}
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			if (snapshot.getOpponent() != null) {
				player.chat("/viewinv " + snapshot.getOpponent().toString());
			}
		}

	}

	@AllArgsConstructor
	private static class HealthButton extends Button {

		private final double health;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.MELON)
					.name("&cHealth: &a" + health + "/10 &4" + StringEscapeUtils.unescapeJava("\u2764"))
					.amount((int) (health == 0 ? 1 : health))
					.build();
		}

	}

	@AllArgsConstructor
	private static class HungerButton extends Button {

		private final int hunger;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.COOKED_BEEF)
					.name("&6Hunger: &a" + hunger + "/20")
					.amount(hunger == 0 ? 1 : hunger)
					.build();
		}

	}

	@AllArgsConstructor
	private static class EffectsButton extends Button {

		private final Collection<PotionEffect> effects;

		@Override
		public ItemStack getButtonItem(Player player) {
			ItemBuilder builder = new ItemBuilder(Material.POTION).name("&9Potion Effects");

			if (effects.isEmpty()) {
				builder.lore("&cNo potion effects");
			} else {
				List<String> lore = new ArrayList<>();

				effects.forEach(effect -> {
					String name = PotionUtil.getName(effect.getType()) + " " + (effect.getAmplifier() + 1);
					String duration = " (" + TimeUtil.millisToTimer((effect.getDuration() / 20) * 1000L) + ")";
					lore.add(CC.PINK + name + CC.GRAY + duration);
				});

				builder.lore(lore);
			}

			return builder.build();
		}

	}

	@AllArgsConstructor
	private static class PotionsButton extends Button {

		private final String name;
		private final int potions;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.POTION)
					.durability(16421)
					.amount(potions == 0 ? 1 : potions)
					.name("&dPotions")
					.lore("&a" + name + " &ehad &a" + potions + " &epotion" + (potions == 1 ? "" : "s") + " left.")
					.build();
		}

	}

	@AllArgsConstructor
	private static class StatisticsButton extends Button {

		private final MatchSnapshot snapshot;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.PAPER)
					.name("&9Statistics")
					.lore(Arrays.asList(
							"&fTotal Hits: &9" + snapshot.getTotalHits(),
							"&fLongest Combo: &9" + snapshot.getLongestCombo(),
							"&fPotions Thrown: &9" + snapshot.getPotionsThrown(),
							"&fPotions Missed: &9" + snapshot.getPotionsMissed(),
							"&fPotion Accuracy: &9" + snapshot.getPotionAccuracy()
					))
					.build();
		}

	}

}
