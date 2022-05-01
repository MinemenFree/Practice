package rip.crystal.practice.game.event.game.map.impl;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.game.event.impl.brackets.BracketsGameLogic;
import rip.crystal.practice.game.event.impl.gulag.GulagGameLogic;
import rip.crystal.practice.game.event.impl.sumo.SumoGameLogic;
import rip.crystal.practice.player.profile.participant.GamePlayer;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.profile.visibility.VisibilityLogic;
import rip.crystal.practice.utilities.LocationUtil;

import java.io.IOException;

public class TeamEventGameMap extends EventGameMap {

	@Getter @Setter private Location spawnPointA;
	@Getter @Setter private Location spawnPointB;

	public TeamEventGameMap(String mapName) {
		super(mapName);
	}

	@Override
	public void teleportFighters(EventGame game) {
		int locationIndex = 0;
		Location[] locations = new Location[]{ spawnPointA, spawnPointB };

		if (game.getGameLogic() instanceof SumoGameLogic) {
			GameParticipant<? extends GamePlayer>[] participants = new GameParticipant[] {
					((SumoGameLogic) game.getGameLogic()).getParticipantA(),
					((SumoGameLogic) game.getGameLogic()).getParticipantB()
			};

			for (GameParticipant<? extends GamePlayer> participant : participants) {
				int processed = 0;

				for (GamePlayer gamePlayer : participant.getPlayers()) {
					processed++;

					Player player = gamePlayer.getPlayer();

					if (player != null) {
						player.teleport(locations[locationIndex]);
						VisibilityLogic.handle(player);
					}

					if (processed == participant.getPlayers().size()) locationIndex++;
				}
			}
		} else if (game.getGameLogic() instanceof GulagGameLogic) {
			GameParticipant<? extends GamePlayer>[] participants = new GameParticipant[] {
					((GulagGameLogic) game.getGameLogic()).getParticipantA(),
					((GulagGameLogic) game.getGameLogic()).getParticipantB()
			};

			for (GameParticipant<? extends GamePlayer> participant : participants) {
				int processed = 0;

				for (GamePlayer gamePlayer : participant.getPlayers()) {
					processed++;

					Player player = gamePlayer.getPlayer();

					if (player != null) player.teleport(locations[locationIndex]);

					if (processed == participant.getPlayers().size()) locationIndex++;
				}
			}
		} else if (game.getGameLogic() instanceof BracketsGameLogic) {
			GameParticipant<? extends GamePlayer>[] participants = new GameParticipant[] {
					((BracketsGameLogic) game.getGameLogic()).getParticipantA(),
					((BracketsGameLogic) game.getGameLogic()).getParticipantB()
			};

			for (GameParticipant<? extends GamePlayer> participant : participants) {
				int processed = 0;

				for (GamePlayer gamePlayer : participant.getPlayers()) {
					processed++;

					Player player = gamePlayer.getPlayer();

					if (player != null) player.teleport(locations[locationIndex]);

					if (processed == participant.getPlayers().size()) locationIndex++;
				}
			}
		}
	}

	@Override
	public boolean isSetup() {
		return spectatorPoint != null && spawnPointA != null && spawnPointB != null;
	}

	@Override
	public void save() {
		super.save();

		FileConfiguration config = cPractice.get().getEventsConfig().getConfiguration();
		config.set("EVENT_MAPS." + getMapName() + ".TYPE", "TEAM");
		config.set("EVENT_MAPS." + getMapName() + ".SPAWN_POINT_A", LocationUtil.serialize(spawnPointA));
		config.set("EVENT_MAPS." + getMapName() + ".SPAWN_POINT_B", LocationUtil.serialize(spawnPointB));

		try {
			config.save(cPractice.get().getEventsConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
