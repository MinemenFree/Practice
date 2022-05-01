package rip.crystal.practice.player.profile.participant.team;

import rip.crystal.practice.player.profile.participant.GamePlayer;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamGameParticipant<T extends GamePlayer> extends GameParticipant<T> {

	private final List<T> players;

	public TeamGameParticipant(T t) {
		super(t);

		this.players = new ArrayList<>();
		this.players.add(t);
	}

	@Override
	public List<T> getPlayers() {
		return players;
	}

	@Override
	public int getAliveCount() {
		int i = 0;

		for (GamePlayer gamePlayer : players) {
			if (!gamePlayer.isDead() && !gamePlayer.isDisconnected()) {
				i++;
			}
		}

		return i;
	}

	@Override
	public boolean isAllDead() {
		int i = 0;

		for (GamePlayer gamePlayer : players) {
			if (gamePlayer.isDead() || gamePlayer.isDisconnected()) {
				i++;
			}
		}

		return players.size() == i;
	}

	@Override
	public boolean containsPlayer(UUID uuid) {
		for (GamePlayer gamePlayer : players) {
			if (gamePlayer.getUuid().equals(uuid)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getConjoinedNames() {
		StringBuilder builder = new StringBuilder();

		if (players.size() == 2) {
			for (GamePlayer gamePlayer : players) {
				builder.append(gamePlayer.getUsername());
				builder.append(" and ");
			}

			return builder.substring(0, builder.length() - 5);
		} else {
			int processed = 0;

			for (GamePlayer gamePlayer : players) {
				processed++;

				builder.append(gamePlayer.getUsername());

				if (processed == players.size() - 1) {
					builder.append(" and ");
				} else {
					builder.append(", ");
				}
			}

			return builder.substring(0, builder.length() - ((processed == players.size() -1) ? 5 : 2));
		}
	}

}
