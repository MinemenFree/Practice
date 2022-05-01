package rip.crystal.practice.essentials;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.event.SpawnTeleportEvent;
import rip.crystal.practice.game.knockback.Knockback;
import rip.crystal.practice.utilities.LocationUtil;
import rip.crystal.practice.utilities.chat.CC;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class Essentials {

	private final cPractice plugin;
	@Setter @Getter private Location spawn;
	@Setter @Getter private List<String> motd;

	public Essentials(cPractice plugin) {
		this.plugin = plugin;
		this.spawn = LocationUtil.deserialize(plugin.getMainConfig().getString("ESSENTIAL_SPAWN_LOCATION"));
		if(spawn != null){
			Bukkit.getWorlds().get(0).setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
		} else {
			Bukkit.getLogger().log(Level.WARNING, "World spawn not found");
		}
		this.motd = CC.translate(plugin.getLangConfig().getStringList("MOTD"));
	}

	public void setSpawnAndSave(Location location) {
		spawn = location;

		if (spawn == null) {
			plugin.getMainConfig().getConfiguration().set("ESSENTIAL_SPAWN_LOCATION", null);
		} else {
			plugin.getMainConfig().getConfiguration().set("ESSENTIAL_SPAWN_LOCATION", LocationUtil.serialize(this.spawn));
		}

		try {
			plugin.getMainConfig().getConfiguration().save(plugin.getMainConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void teleportToSpawn(Player player) {
		Location location = spawn == null ? plugin.getServer().getWorlds().get(0).getSpawnLocation() : spawn;

		/*if(player.hasPermission("cpractice.fly")) {
			player.setAllowFlight(true);
			player.setFlying(true);
		}*/

		SpawnTeleportEvent event = new SpawnTeleportEvent(player, location);
		event.call();

		Knockback.getKnockbackProfiler().setKnockback(player, "default");
		//Knockback.getKnockbackProfiler().setKnockback(player, "default");

		if (!event.isCancelled() && event.getLocation() != null) {
			player.teleport(event.getLocation());
		}
	}

	public void clearEntities(World world) {
		for (Entity entity : world.getEntities()) {
			if (entity.getType() == EntityType.PLAYER) continue;

			entity.remove();
		}
	}

	public int clearEntities(World world, EntityType... excluded) {
		int removed = 0;

		entityLoop:
		for (Entity entity : world.getEntities()) {
			if (entity instanceof Item) {
				removed++;
				entity.remove();
				continue;
			}

			for (EntityType type : excluded) {
				if (entity.getType() == EntityType.PLAYER) continue entityLoop;

				if (entity.getType() == type) continue entityLoop;
			}

			removed++;
			entity.remove();
		}

		return removed;
	}

}
