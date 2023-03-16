package rip.crystal.practice.match.task;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.match.Match;

@AllArgsConstructor
public class MatchResetTask extends BukkitRunnable {

	private final Match match;

	public void run() {
		if (this.match.getKit().getGameRules().isBuild() || this.match.getKit().getGameRules().isHcftrap() || this.match.getKit().getGameRules().isBridge() || this.match.getKit().getGameRules().isSpleef()) {
			EditSession editSession;
			if (!this.match.getPlacedBlocks().isEmpty()) {
				editSession = new EditSession(BukkitUtil.getLocalWorld(this.match.getArena().getSpawnA().getWorld()), Integer.MAX_VALUE);
				editSession.setFastMode(true);
				for (Location location : this.match.getPlacedBlocks()) {
					try {
						editSession.setBlock(new Vector(location.getBlockX(), location.getBlockY(), location.getZ()), new BaseBlock(0));
					}
					catch (Exception exception) {
						exception.printStackTrace();
					}
				}
				editSession.flushQueue();
				this.match.getPlacedBlocks().clear();
				this.match.getArena().setActive(false);
			}
			if (!this.match.getChangedBlocks().isEmpty()) {
				editSession = new EditSession(BukkitUtil.getLocalWorld(this.match.getArena().getSpawnA().getWorld()), Integer.MAX_VALUE);
				editSession.setFastMode(true);
				for (BlockState blockState : this.match.getChangedBlocks()) {
					try {
						editSession.setBlock(new Vector(blockState.getLocation().getBlockX(), blockState.getLocation().getBlockY(), blockState.getLocation().getZ()), new BaseBlock(blockState.getTypeId(), (int)blockState.getRawData()));
					}
					catch (Exception exception) {
						exception.printStackTrace();
					}
				}
				editSession.flushQueue();
				if (this.match.getKit().getGameRules().isBuild() || this.match.getKit().getGameRules().isHcftrap() || this.match.getKit().getGameRules().isSpleef()) {
					this.match.getChangedBlocks().clear();
					this.match.getArena().setActive(false);
				}
			}
			match.getArena().setActive(false);
			this.cancel();
		} else {
			match.getArena().setActive(false);
			this.cancel();
		}
	}
}