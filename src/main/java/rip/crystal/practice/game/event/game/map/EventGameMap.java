package rip.crystal.practice.game.event.game.map;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.map.impl.SpreadEventGameMap;
import rip.crystal.practice.game.event.game.map.impl.TeamEventGameMap;
import rip.crystal.practice.utilities.LocationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class EventGameMap {

	@Getter private static final List<EventGameMap> maps;

	static {
		maps = new ArrayList<>();
	}

	@Getter protected final String mapName;
	@Getter @Setter protected Location spectatorPoint;

	public EventGameMap(String mapName) {
		this.mapName = mapName;
	}

	public abstract boolean isSetup();

	/**
	 * Teleports the fighters to their spawn locations.
	 */
	public abstract void teleportFighters(EventGame game);

	public void save() {
		FileConfiguration config = cPractice.get().getEventsConfig().getConfiguration();
		config.set("EVENT_MAPS." + mapName, null);
		config.set("EVENT_MAPS." + mapName + ".SPECTATOR_POINT", LocationUtil.serialize(spectatorPoint));

		try {
			config.save(cPractice.get().getEventsConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		FileConfiguration config = cPractice.get().getEventsConfig().getConfiguration();
		config.set("EVENT_MAPS." + mapName, null);

		try {
			config.save(cPractice.get().getEventsConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static EventGameMap getByName(String mapName) {
		for (EventGameMap gameMap : maps) {
			if (gameMap.getMapName().equalsIgnoreCase(mapName)) {
				return gameMap;
			}
		}

		return null;
	}

	public static List<EventGameMap> getCompletedMaps() {
		List<EventGameMap> gameMaps = new ArrayList<>();

		maps.forEach(eventMap -> {
			if (eventMap.isSetup()) {
				gameMaps.add(eventMap);
			}
		});

		return gameMaps;
	}

	public static void init() {
		FileConfiguration config = cPractice.get().getEventsConfig().getConfiguration();

		for (String key : config.getConfigurationSection("EVENT_MAPS").getKeys(false)) {
			final String path = "EVENT_MAPS." + key + ".";

			EventGameMap gameMap;

			switch (config.getString(path + "TYPE")) {
				case "TEAM": {
					TeamEventGameMap teamGameMap = new TeamEventGameMap(key);
					teamGameMap.setSpectatorPoint(LocationUtil
							.deserialize(config.getString(path + "SPECTATOR_POINT")));
					teamGameMap.setSpawnPointA(LocationUtil
							.deserialize(config.getString(path + "SPAWN_POINT_A")));
					teamGameMap.setSpawnPointB(LocationUtil
							.deserialize(config.getString(path + "SPAWN_POINT_B")));

					gameMap = teamGameMap;
				}
				break;
				case "SPREAD": {
					SpreadEventGameMap spreadGameMap = new SpreadEventGameMap(key);
					spreadGameMap.setSpectatorPoint(LocationUtil.deserialize(config.getString(path + "SPECTATOR_POINT")));

					for (String serializedLocation : config.getStringList(path + "SPAWN_LOCATIONS")) {
						spreadGameMap.getSpawnLocations().add(LocationUtil.deserialize(serializedLocation));
					}

					gameMap = spreadGameMap;
				}
				break;
				default:
					continue;
			}

			maps.add(gameMap);
		}
	}

}
