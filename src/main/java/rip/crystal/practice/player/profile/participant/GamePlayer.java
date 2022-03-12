package rip.crystal.practice.player.profile.participant;

import lombok.Data;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class GamePlayer {

	private UUID uuid;
	private String username;
	private boolean disconnected;
	private boolean dead;

	private List<UUID> players;
	private List<UUID> alivePlayers = new ArrayList<>();
	private String leaderName;
	@Setter
	private UUID leader;

	public GamePlayer(UUID uuid, String username) {
		this.uuid = uuid;
		this.username = username;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

}
