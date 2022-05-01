package rip.crystal.practice.match.task;

import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.match.MatchSnapshot;

public class MatchSnapshotCleanupTask extends BukkitRunnable {

	@Override
	public void run() {
		MatchSnapshot.getSnapshots().entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue().getCreatedAt() >= 45_000);
	}

}
