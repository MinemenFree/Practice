package rip.crystal.practice.match.participant;

import lombok.Getter;
import lombok.Setter;
import rip.crystal.practice.player.profile.participant.GamePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchGamePlayer extends GamePlayer {

	private final Map<UUID, String> playerNames = new HashMap<>();
	@Getter private int elo;
	@Getter @Setter private int eloMod;
	@Getter private int hits;
	@Getter private int longestCombo;
	@Getter private int combo;
	@Getter private int potionsThrown;
	@Getter private int potionsMissed;
	@Getter private int kills;

	public MatchGamePlayer(UUID uuid, String username) {
		this(uuid, username, 0);
	}

	public MatchGamePlayer(UUID uuid, String username, int elo) {
		super(uuid, username);

		this.elo = elo;
	}

	public void incrementPotionsThrown() {
		potionsThrown++;
	}

	public void incrementKills() {
		kills++;
	}

	public void incrementPotionsMissed() {
		potionsMissed++;
	}

	public void handleHit() {
		hits++;
		combo++;

		if (combo > longestCombo) {
			longestCombo = combo;
		}
	}

	public void resetCombo() {
		combo = 0;
	}

}
