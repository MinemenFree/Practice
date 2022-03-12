package rip.crystal.practice.game.event.game.map.vote;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventGameMapVoteData {

	@Getter private final List<UUID> players = new ArrayList<>();

	public void addVote(UUID uuid) {
		if (!players.contains(uuid)) {
			players.add(uuid);
		}
	}

	public boolean hasVote(UUID uuid) {
		return players.contains(uuid);
	}

}
