package rip.crystal.practice.utilities;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionUtil {

	public static String getName(PotionEffectType potionEffectType) {
		if (potionEffectType.getName().equalsIgnoreCase("fire_resistance"))
			return "Fire Resistance";
		else if (potionEffectType.getName().equalsIgnoreCase("speed"))
			return "Speed";
		else if (potionEffectType.getName().equalsIgnoreCase("weakness"))
			return "Weakness";
		else if (potionEffectType.getName().equalsIgnoreCase("slowness"))
			return "Slowness";
		else if (potionEffectType.getName().equalsIgnoreCase("strength"))
			return "Strength";
		else if (potionEffectType.getName().equalsIgnoreCase("absorption"))
			return "Absorption";
		else if (potionEffectType.getName().equalsIgnoreCase("resistance"))
			return "Resistance";
		else if (potionEffectType.getName().equalsIgnoreCase("invisibility"))
			return "Invisibility";
		else return "Unknown";
	}

	public static String convertPotionEffectToString(PotionEffect potionEffect) {
		return potionEffect.getType().getName() + ":" + potionEffect.getAmplifier();
	}

	public static PotionEffect convertStringToPotionEffect(String key) {
		String[] args = key.split(":");
		return new PotionEffect(PotionEffectType.getByName(args[0]), Integer.MAX_VALUE, Integer.parseInt(args[1]));
	}

}
