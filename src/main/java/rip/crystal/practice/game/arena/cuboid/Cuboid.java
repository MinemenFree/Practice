package rip.crystal.practice.game.arena.cuboid;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class Cuboid implements Iterable<Location> {

	private String worldName;
	private int x1, y1, z1;
	private int x2, y2, z2;

	/**
	 * Construct a Cuboid given two Location objects which represent any two corners of the Cuboid.
	 *
	 * @param l1 one of the corners
	 * @param l2 the other corner
	 */
	public Cuboid(Location l1, Location l2) {
		this(l1.getWorld().getName(),
				l1.getBlockX(), l1.getBlockY(), l1.getBlockZ(),
				l2.getBlockX(), l2.getBlockY(), l2.getBlockZ()
		);

	}

	/**
	 * Construct a Cuboid in the given World and xyz coords
	 *
	 * @param world the Cuboid's world
	 * @param x1    X coord of corner 1
	 * @param y1    Y coord of corner 1
	 * @param z1    Z coord of corner 1
	 * @param x2    X coord of corner 2
	 * @param y2    Y coord of corner 2
	 * @param z2    Z coord of corner 2
	 */
	public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		this(world.getName(), x1, y1, z1, x2, y2, z2);
	}

	/**
	 * Construct a Cuboid in the given world name and xyz coords.
	 *
	 * @param worldName the Cuboid's world name
	 * @param x1        X coord of corner 1
	 * @param y1        Y coord of corner 1
	 * @param z1        Z coord of corner 1
	 * @param x2        X coord of corner 2
	 * @param y2        Y coord of corner 2
	 * @param z2        Z coord of corner 2
	 */
	public Cuboid(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
		this.worldName = worldName;
		this.x1 = Math.min(x1, x2);
		this.x2 = Math.max(x1, x2);
		this.y1 = Math.min(y1, y2);
		this.y2 = Math.max(y1, y2);
		this.z1 = Math.min(z1, z2);
		this.z2 = Math.max(z1, z2);
	}

	/**
	 * Get the Location of the lower northeast corner of the Cuboid (minimum XYZ coords).
	 *
	 * @return Location of the lower northeast corner
	 */
	public Location getLowerCorner() {
		return new Location(getWorld(), x1, y1, z1);
	}

	/**
	 * Get the Location of the upper southwest corner of the Cuboid (maximum XYZ coords).
	 *
	 * @return Location of the upper southwest corner
	 */
	public Location getUpperCorner() {
		return new Location(getWorld(), x2, y2, z2);
	}

	/**
	 * Get the the center of the Cuboid
	 *
	 * @return Location at the centre of the Cuboid
	 */
	public Location getCenter() {
		return new Location(
				getWorld(), getLowerX() + (getUpperX() - getLowerX()) / 2,
				getLowerY() + (getUpperY() - getLowerY()) / 2, getLowerZ() + (getUpperZ() - getLowerZ()) / 2
		);
	}

	/**
	 * Get the Cuboid's world.
	 *
	 * @return the World object representing this Cuboid's world
	 *
	 * @throws IllegalStateException if the world is not loaded
	 */
	public World getWorld() {

		World world = Bukkit.getWorld(worldName);
		if (world == null) {
			throw new IllegalStateException("world '" + worldName + "' is not loaded");
		}
		return world;
	}

	/**
	 * Get the size of this Cuboid along the X axis
	 *
	 * @return Size of Cuboid along the X axis
	 */
	public int getSizeX() {
		return (x2 - x1) + 1;
	}

	/**
	 * Get the size of this Cuboid along the Y axis
	 *
	 * @return Size of Cuboid along the Y axis
	 */
	public int getSizeY() {
		return (y2 - y1) + 1;
	}

	/**
	 * Get the size of this Cuboid along the Z axis
	 *
	 * @return Size of Cuboid along the Z axis
	 */
	public int getSizeZ() {
		return (z2 - z1) + 1;
	}

	/**
	 * Get the minimum X coord of this Cuboid
	 *
	 * @return the minimum X coord
	 */
	public int getLowerX() {
		return x1;
	}

	/**
	 * Get the minimum Y coord of this Cuboid
	 *
	 * @return the minimum Y coord
	 */
	public int getLowerY() {
		return y1;
	}

	/**
	 * Get the minimum Z coord of this Cuboid
	 *
	 * @return the minimum Z coord
	 */
	public int getLowerZ() {
		return z1;
	}

	/**
	 * Get the maximum X coord of this Cuboid
	 *
	 * @return the maximum X coord
	 */
	public int getUpperX() {
		return x2;
	}

	/**
	 * Get the maximum Y coord of this Cuboid
	 *
	 * @return the maximum Y coord
	 */
	public int getUpperY() {
		return y2;
	}

	/**
	 * Get the maximum Z coord of this Cuboid
	 *
	 * @return the maximum Z coord
	 */
	public int getUpperZ() {
		return z2;
	}

	/**
	 * Get the Blocks at the four corners of the Cuboid, without respect to y-value
	 *
	 * @return array of Block objects representing the Cuboid corners
	 */
	public Location[] getCorners() {
		Location[] res = new Location[4];
		World w = getWorld();
		res[0] = new Location(w, x1, 0, z1); // ++x
		res[1] = new Location(w, x2, 0, z1); // ++z
		res[2] = new Location(w, x2, 0, z2); // --x
		res[3] = new Location(w, x1, 0, z2); // --z
		return res;
	}

	/**
	 * Expand the Cuboid in the given direction by the given amount. Negative amounts will shrink the Cuboid in the
	 * given direction. Shrinking a cuboid's face past the opposite face is not an error and will return a valid
	 * Cuboid.
	 *
	 * @param dir    the direction in which to expand
	 * @param amount the number of blocks by which to expand
	 *
	 * @return a new Cuboid expanded by the given direction and amount
	 */
	public Cuboid expand(CuboidDirection dir, int amount) {
		switch (dir) {
			case NORTH:
				return new Cuboid(worldName, x1 - amount, y1, z1, x2, y2, z2);
			case SOUTH:
				return new Cuboid(worldName, x1, y1, z1, x2 + amount, y2, z2);
			case EASY:
				return new Cuboid(worldName, x1, y1, z1 - amount, x2, y2, z2);
			case WEST:
				return new Cuboid(worldName, x1, y1, z1, x2, y2, z2 + amount);
			case DOWN:
				return new Cuboid(worldName, x1, y1 - amount, z1, x2, y2, z2);
			case UP:
				return new Cuboid(worldName, x1, y1, z1, x2, y2 + amount, z2);
			default:
				throw new IllegalArgumentException("invalid direction " + dir);
		}
	}

	/**
	 * Shift the Cuboid in the given direction by the given amount.
	 *
	 * @param dir    the direction in which to shift
	 * @param amount the number of blocks by which to shift
	 *
	 * @return a new Cuboid shifted by the given direction and amount
	 */
	public Cuboid shift(CuboidDirection dir, int amount) {
		return expand(dir, amount).expand(dir.opposite(), -amount);
	}

	/**
	 * Outset (grow) the Cuboid in the given direction by the given amount.
	 *
	 * @param dir    the direction in which to outset (must be HORIZONTAL, VERTICAL, or BOTH)
	 * @param amount the number of blocks by which to outset
	 *
	 * @return a new Cuboid outset by the given direction and amount
	 */
	public Cuboid outset(CuboidDirection dir, int amount) {
		Cuboid c;
		switch (dir) {
			case HORIZONTAL:
				c = expand(CuboidDirection.NORTH, amount).expand(CuboidDirection.SOUTH, amount)
				                                         .expand(CuboidDirection.EASY, amount)
				                                         .expand(CuboidDirection.WEST, amount);
				break;
			case VERTICAL:
				c = expand(CuboidDirection.DOWN, amount).expand(CuboidDirection.UP, amount);
				break;
			case BOTH:
				c = outset(CuboidDirection.HORIZONTAL, amount).outset(CuboidDirection.VERTICAL, amount);
				break;
			default:
				throw new IllegalArgumentException("invalid direction " + dir);
		}
		return c;
	}

	/**
	 * Inset (shrink) the Cuboid in the given direction by the given amount. Equivalent to calling outset() with a
	 * negative amount.
	 *
	 * @param dir    the direction in which to inset (must be HORIZONTAL, VERTICAL, or BOTH)
	 * @param amount the number of blocks by which to inset
	 *
	 * @return a new Cuboid inset by the given direction and amount
	 */
	public Cuboid inset(CuboidDirection dir, int amount) {
		return outset(dir, -amount);
	}

	/**
	 * Return true if the point at (x,y,z) is contained within this Cuboid.
	 *
	 * @param x the X coord
	 * @param y the Y coord
	 * @param z the Z coord
	 *
	 * @return true if the given point is within this Cuboid, false otherwise
	 */
	public boolean contains(int x, int y, int z) {
		return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
	}

	/**
	 * Return true if the point at (x,z) is contained within this Cuboid.
	 *
	 * @param x the X coord
	 * @param z the Z coord
	 *
	 * @return true if the given point is within this Cuboid, false otherwise
	 */
	public boolean contains(int x, int z) {
		return x >= x1 && x <= x2 && z >= z1 && z <= z2;
	}

	/**
	 * Check if the given Location is contained within this Cuboid.
	 *
	 * @param l the Location to check for
	 *
	 * @return true if the Location is within this Cuboid, false otherwise
	 */
	public boolean contains(Location l) {
		if (!worldName.equals(l.getWorld().getName())) {
			return false;
		}
		return contains(l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}

	/**
	 * Check if the given Block is contained within this Cuboid.
	 *
	 * @param b the Block to check for
	 *
	 * @return true if the Block is within this Cuboid, false otherwise
	 */
	public boolean contains(Block b) {
		return contains(b.getLocation());
	}

	/**
	 * Get the volume of this Cuboid.
	 *
	 * @return the Cuboid volume, in blocks
	 */
	public int volume() {
		return getSizeX() * getSizeY() * getSizeZ();
	}

	/**
	 * Get the Cuboid representing the face of this Cuboid. The resulting Cuboid will be one block thick in the axis
	 * perpendicular to the requested face.
	 *
	 * @param dir which face of the Cuboid to get
	 *
	 * @return the Cuboid representing this Cuboid's requested face
	 */
	public Cuboid getFace(CuboidDirection dir) {
		switch (dir) {
			case DOWN:
				return new Cuboid(worldName, x1, y1, z1, x2, y1, z2);
			case UP:
				return new Cuboid(worldName, x1, y2, z1, x2, y2, z2);
			case NORTH:
				return new Cuboid(worldName, x1, y1, z1, x1, y2, z2);
			case SOUTH:
				return new Cuboid(worldName, x2, y1, z1, x2, y2, z2);
			case EASY:
				return new Cuboid(worldName, x1, y1, z1, x2, y2, z1);
			case WEST:
				return new Cuboid(worldName, x1, y1, z2, x2, y2, z2);
			default:
				throw new IllegalArgumentException("Invalid direction " + dir);
		}
	}

	/**
	 * Get the Cuboid big enough to hold both this Cuboid and the given one.
	 *
	 * @return a new Cuboid large enough to hold this Cuboid and the given Cuboid
	 */
	public Cuboid getBoundingCuboid(Cuboid other) {
		if (other == null) {
			return this;
		}

		int xMin = Math.min(getLowerX(), other.getLowerX());
		int yMin = Math.min(getLowerY(), other.getLowerY());
		int zMin = Math.min(getLowerZ(), other.getLowerZ());
		int xMax = Math.max(getUpperX(), other.getUpperX());
		int yMax = Math.max(getUpperY(), other.getUpperY());
		int zMax = Math.max(getUpperZ(), other.getUpperZ());

		return new Cuboid(worldName, xMin, yMin, zMin, xMax, yMax, zMax);
	}

	/**
	 * Get a block relative to the lower NE point of the Cuboid.
	 *
	 * @param x the X coord
	 * @param y the Y coord
	 * @param z the Z coord
	 *
	 * @return the block at the given position
	 */
	public Block getRelativeBlock(int x, int y, int z) {
		return getWorld().getBlockAt(x1 + x, y1 + y, z1 + z);
	}

	/**
	 * Get a block relative to the lower NE point of the Cuboid in the given World. This version of getRelativeBlock()
	 * should be used if being called many times, to avoid excessive calls to getWorld().
	 *
	 * @param w the World
	 * @param x the X coord
	 * @param y the Y coord
	 * @param z the Z coord
	 *
	 * @return the block at the given position
	 */
	public Block getRelativeBlock(World w, int x, int y, int z) {
		return w.getBlockAt(x1 + x, y1 + y, z1 + z);
	}

	/**
	 * Get a list of the chunks which are fully or partially contained in this cuboid.
	 *
	 * @return a list of Chunk objects
	 */
	public List<Chunk> getChunks() {
		List<Chunk> chunks = new ArrayList<>();

		World w = getWorld();

		// These operators get the lower bound of the chunk, by complementing 0xf (15) into 16
		// and using an OR gate on the integer coordinate

		int x1 = getLowerX() & ~0xf;
		int x2 = getUpperX() & ~0xf;
		int z1 = getLowerZ() & ~0xf;
		int z2 = getUpperZ() & ~0xf;

		for (int x = x1; x <= x2; x += 16) {
			for (int z = z1; z <= z2; z += 16) {
				chunks.add(w.getChunkAt(x >> 4, z >> 4));
			}
		}

		return chunks;
	}

	/**
	 * @return horizontal walls of the cuboid
	 */
	public Cuboid[] getWalls() {

		return new Cuboid[]{
				getFace(CuboidDirection.NORTH),
				getFace(CuboidDirection.SOUTH),
				getFace(CuboidDirection.WEST),
				getFace(CuboidDirection.EASY)
		};
	}

	/**
	 * @return read-only location iterator
	 */
	public Iterator<Location> iterator() {
		return new LocationCuboidIterator(getWorld(), x1, y1, z1, x2, y2, z2);
	}

	@Override
	public String toString() {
		return "Cuboid: " + worldName + "," + x1 + "," + y1 + "," + z1 + "=>" + x2 + "," + y2 + "," + z2;
	}

	public class LocationCuboidIterator implements Iterator<Location> {

		private final World w;
		private final int baseX;
        private final int baseY;
        private final int baseZ;
		private int x, y, z;
		private final int sizeX;
        private final int sizeY;
        private final int sizeZ;

		public LocationCuboidIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
			this.w = w;
			baseX = x1;
			baseY = y1;
			baseZ = z1;
			sizeX = Math.abs(x2 - x1) + 1;
			sizeY = Math.abs(y2 - y1) + 1;
			sizeZ = Math.abs(z2 - z1) + 1;
			x = y = z = 0;
		}

		public boolean hasNext() {
			return x < sizeX && y < sizeY && z < sizeZ;
		}

		public Location next() {
			Location b = new Location(w, baseX + x, baseY + y, baseZ + z);
			if (++x >= sizeX) {
				x = 0;
				if (++y >= sizeY) {
					y = 0;
					++z;
				}
			}
			return b;
		}

		public void remove() {
		}
	}


}