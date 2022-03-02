package rip.crystal.practice.match;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class MatchSnapshot {

	@Getter
	private static Map<UUID, MatchSnapshot> snapshots = new HashMap<>();

	private final UUID uuid;
	private final String username;
	private UUID opponent;
	private final double health;
	private final int hunger;
	private final ItemStack[] armor, contents;
	private final Collection<PotionEffect> effects;
	private int potionsThrown, potionsMissed, longestCombo, totalHits;
	private long createdAt;

	public MatchSnapshot(Player player, boolean dead) {
		this.uuid = player.getUniqueId();
		this.username = player.getName();
		this.health = dead ? 0 : (player.getHealth() == 0 ? 0 : Math.round(player.getHealth() / 2));
		this.hunger = player.getFoodLevel();
		this.armor = player.getInventory().getArmorContents();
		this.contents = player.getInventory().getContents();
		this.effects = player.getActivePotionEffects();
		this.createdAt = System.currentTimeMillis();
	}

	public int getRemainingPotions() {
		int amount = 0;

		for (ItemStack itemStack : this.contents) {
			if (itemStack != null && itemStack.getType() == Material.POTION && itemStack.getDurability() == 16421) {
				amount++;
			}
		}

		return amount;
	}

	public boolean shouldDisplayRemainingPotions() {
		return this.getRemainingPotions() > 0 || potionsThrown > 0 || potionsMissed > 0;
	}

	public double getPotionAccuracy() {
		if (potionsMissed == 0) {
			return 100.0;
		} else if (potionsThrown == potionsMissed) {
			return 50.0;
		}

		return Math.round(100.0D - (((double) potionsMissed / (double) potionsThrown) * 100.0D));
	}

	public static MatchSnapshot getByUuid(UUID uuid) {
		return snapshots.get(uuid);
	}

	public static MatchSnapshot getByName(String name) {
		for (MatchSnapshot snapshot : snapshots.values()) {
			if (snapshot.getUsername().equalsIgnoreCase(name)) {
				return snapshot;
			}
		}

		return null;
	}

}
