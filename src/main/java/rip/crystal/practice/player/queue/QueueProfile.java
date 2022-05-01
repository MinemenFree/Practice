package rip.crystal.practice.player.queue;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

import java.util.UUID;

@Data
public class QueueProfile {

	private final Queue queue;
	private final UUID playerUuid;
	private int elo;
	private int range = 25;
	private long start = System.currentTimeMillis();
	private int ticked;

	public QueueProfile(Queue queue, UUID playerUuid) {
		this.queue = queue;
		this.playerUuid = playerUuid;
	}

	public void tickRange() {
		ticked++;

		if (ticked % 6 == 0) {
			range += 3;

			if (ticked >= 50) {
				ticked = 0;

				if (queue.isRanked()) {
					Player player = Bukkit.getPlayer(playerUuid);
					if (player != null) {
						new MessageFormat(Locale.QUEUE_RANGE_INCREMENT.format(Profile.get(player.getUniqueId()).getLocale()))
							.add("{queue_name}", queue.getQueueName())
							.add("{min_range}", String.valueOf(getMinRange()))
							.add("{max_range}", String.valueOf(getMaxRange()))
							.send(player);
					}
				}
			}
		}
	}

	public boolean isInRange(int elo) {
		return elo >= (this.elo - this.range) && elo <= (this.elo + this.range);
	}

	public long getPassed() {
		return System.currentTimeMillis() - this.start;
	}

	public int getMinRange() {
		int min = this.elo - this.range;

		return Math.max(min, 0);
	}

	public int getMaxRange() {
		int max = this.elo + this.range;

		return Math.min(max, 2500);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof QueueProfile && ((QueueProfile) o).getPlayerUuid().equals(this.playerUuid);
	}

}
