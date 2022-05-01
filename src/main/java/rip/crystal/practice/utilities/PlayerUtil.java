
package rip.crystal.practice.utilities;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.playerversion.PlayerVersion;
import rip.crystal.practice.utilities.playerversion.PlayerVersionHandler;

import java.util.UUID;

public class PlayerUtil {

	public static void setLastAttacker(Player victim, Player attacker) {
		victim.setMetadata("lastAttacker", new FixedMetadataValue(cPractice.get(), attacker.getUniqueId()));
	}

	public static Player getLastAttacker(Player victim) {
		if (victim.hasMetadata("lastAttacker")) {
			return Bukkit.getPlayer((UUID) victim.getMetadata("lastAttacker").get(0).value());
		} else {
			return null;
		}
	}

	public static void reset(Player player) {
		reset(player, true);
	}

	public static int getPing(final Player player) {
		return ((CraftPlayer)player).getHandle().ping;
	}

	public static void decrement(Player player) {
		ItemStack itemStack = player.getItemInHand();
		if (itemStack.getAmount() <= 1) player.setItemInHand(new ItemStack(Material.AIR, 1));
		else itemStack.setAmount(itemStack.getAmount() - 1);
		player.updateInventory();
	}

	public static void reset(Player player, boolean resetHeldSlot) {
		player.setHealth(20.0D);
		player.setSaturation(20.0F);
		player.setFallDistance(0.0F);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setMaximumNoDamageTicks(20);
		player.setExp(0.0F);
		player.setLevel(0);
		player.setAllowFlight(false);
		player.setFlying(false);
		player.setWalkSpeed(0.2f);
		player.spigot().setCollidesWithEntities(true);
		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.getInventory().setContents(new ItemStack[36]);
		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

		if (resetHeldSlot) {
			player.getInventory().setHeldItemSlot(0);
		}

		player.updateInventory();
	}

	public static void denyMovement(Player player) {
		player.setFlying(false);
		player.setWalkSpeed(0.0F);
		player.setFoodLevel(0);
		player.setSprinting(false);
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
	}

	public static void allowMovement(Player player) {
		player.setFlying(false);
		player.setWalkSpeed(0.2F);
		player.setFoodLevel(20);
		player.setSprinting(true);
		player.removePotionEffect(PotionEffectType.JUMP);
	}

	public static PlayerVersion getPlayerVersion(Player player) {
		return PlayerVersionHandler.version.getPlayerVersion(player);
	}
}