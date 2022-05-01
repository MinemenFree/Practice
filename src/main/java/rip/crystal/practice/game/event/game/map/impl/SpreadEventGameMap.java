package rip.crystal.practice.game.event.game.map.impl;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.game.event.impl.sumo.SumoGameLogic;
import rip.crystal.practice.game.event.impl.tnttag.TNTTagGameLogic;
import rip.crystal.practice.player.profile.participant.GamePlayer;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.profile.visibility.VisibilityLogic;
import rip.crystal.practice.utilities.LocationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpreadEventGameMap extends EventGameMap {

	@Getter private final List<Location> spawnLocations = new ArrayList<>();

	public SpreadEventGameMap(String mapName) {
		super(mapName);
	}

	@Override
	public void teleportFighters(EventGame game) {
		int i = 0;

		Location[] locations = spawnLocations.toArray(new Location[0]);


		if (game.getGameLogic() instanceof SumoGameLogic) {
			Player participantA = ((SumoGameLogic) game.getGameLogic()).getParticipantA().getLeader().getPlayer();
			Player participantB = ((SumoGameLogic) game.getGameLogic()).getParticipantB().getLeader().getPlayer();

			if (participantA != null) {
				participantA.teleport(locations[0]);
				VisibilityLogic.handle(participantA);
			}
			if (participantB != null) {
				participantB.teleport(locations[1]);
				VisibilityLogic.handle(participantB);
			}
			return;
		}

		for (GameParticipant<GamePlayer> participant : game.getParticipants()) {
			if (game.getGameLogic() instanceof TNTTagGameLogic) {
				if (participant.isEliminated()) continue;
				for (GamePlayer gamePlayer : participant.getPlayers()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						player.teleport(locations[i]);

						i++;

						if (i == locations.length) i = 0;
					}
				}
			}
		}
	}

	@Override
	public boolean isSetup() {
		return spectatorPoint != null && !spawnLocations.isEmpty();
	}

	@Override
	public void save() {
		super.save();

		FileConfiguration config = cPractice.get().getEventsConfig().getConfiguration();
		config.set("EVENT_MAPS." + getMapName() + ".TYPE", "SPREAD");
		config.set("EVENT_MAPS." + getMapName() + ".SPECTATOR_POINT", LocationUtil.serialize(spectatorPoint));
		config.set("EVENT_MAPS." + getMapName() + ".SPAWN_LOCATIONS", spawnLocations
				.stream().map(LocationUtil::serialize).collect(Collectors.toList()));

		try {
			config.save(cPractice.get().getEventsConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
