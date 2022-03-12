package rip.crystal.practice.game.arena.generator;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import lombok.Getter;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

@Getter
public class Schematic {

	private CuboidClipboard clipBoard;

	public Schematic(File file) throws IOException {
		SchematicFormat format = SchematicFormat.MCEDIT;

		try {
			clipBoard = format.load(file);
		} catch (DataException e) {
			e.printStackTrace();
		}
	}

	public void pasteSchematic(World world, int x, int y, int z) {
		Vector pastePos = new Vector(x, y, z);
		EditSession editSession = new EditSession(BukkitUtil.getLocalWorld(world), 999999);

		try {
			clipBoard.place(editSession, pastePos, true);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}

}
