package rip.crystal.practice.utilities.chat;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public class StyleUtil {

	public static String colorPing(int ping) {
		if (ping <= 40) {
			return CC.GREEN + ping;
		} else if (ping <= 70) {
			return CC.YELLOW + ping;
		} else if (ping <= 100) {
			return CC.GOLD + ping;
		} else {
			return CC.RED + ping;
		}
	}

	public static String colorHealth(double health) {
		if (health > 15) {
			return CC.GREEN + convertHealth(health);
		} else if (health > 10) {
			return CC.GOLD + convertHealth(health);
		} else if (health > 5) {
			return CC.YELLOW + convertHealth(health);
		} else {
			return CC.RED + convertHealth(health);
		}
	}

	public static double convertHealth(double health) {
		double dividedHealth = health / 2;

		if (dividedHealth % 1 == 0) {
			return dividedHealth;
		}

		if (dividedHealth % .5 == 0) {
			return dividedHealth;
		}

		if (dividedHealth - ((int) dividedHealth) > .5) {
			return ((int) dividedHealth) + 1;
		} else if (dividedHealth - ((int) dividedHealth) > .25) {
			return ((int) dividedHealth) + .5;
		} else {
			return ((int) dividedHealth);
		}
	}

	public static String getHeartIcon() {
		return StringEscapeUtils.unescapeJava("\u2764");
	}

	public static String colorName(ChatColor color){
		return StringUtils.capitalize(color.name().replace("_", " ").toLowerCase());
	}

	public static String capitalize(String string){
		return StringUtils.capitalize(string.toLowerCase());
	}
}
