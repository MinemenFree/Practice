package rip.crystal.practice.player.profile.participant.alone;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.participant.GamePlayer;
import rip.crystal.practice.utilities.MessageFormat;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GameParticipant<T extends GamePlayer> {

	@Getter private final T leader;
	@Getter @Setter private int roundWins;
	@Getter @Setter private boolean eliminated;
	@Getter @Setter private boolean hasBed = true;

	public GameParticipant(T leader) {
		this.leader = leader;
	}

	public List<T> getPlayers() {
		return Collections.singletonList(leader);
	}

	public boolean isAllDead() {
		return leader.isDead();
	}

	public int getAliveCount() {
		return leader.isDead() ? 0 : 1;
	}

	public boolean containsPlayer(UUID uuid) {
		return leader.getUuid().equals(uuid);
	}

	public String getConjoinedNames() {
		return leader.getUsername();
	}

	public void reset() {
		eliminated = false;
		for (GamePlayer gamePlayer : getPlayers()) {
			gamePlayer.setDead(false);
		}
	}

	public void sendMessage(String message) {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					player.sendMessage(message);
				}
			}
		}
	}

	public void sendMessage(Locale lang, MessageFormat messageFormat) {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					messageFormat.setMessage(lang.format(Profile.get(player.getUniqueId()).getLocale()));
					messageFormat.send(player);
				}
			}
		}
	}

	public void sendMessage(List<String> messages) {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					for (String message : messages) {
						player.sendMessage(message);
					}
				}
			}
		}
	}

	public void sendSound(Sound sound, float volume, float pitch) {
		for (GamePlayer gamePlayer : getPlayers()) {
			if (!gamePlayer.isDisconnected()) {
				Player player = gamePlayer.getPlayer();

				if (player != null) {
					player.playSound(player.getLocation(), sound, volume, pitch);
				}
			}
		}
	}

	public void destroyBed() {
		this.hasBed = false;
	}
}
