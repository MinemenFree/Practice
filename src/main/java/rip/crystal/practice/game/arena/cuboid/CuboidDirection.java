package rip.crystal.practice.game.arena.cuboid;

/**
 * Represents directions that can be applied to certain faces and actions of a Cuboid
 */
public enum CuboidDirection {

	NORTH,
	EASY,
	SOUTH,
	WEST,
	UP,
	DOWN,
	HORIZONTAL,
	VERTICAL,
	BOTH,
	UNKNOWN;

	public CuboidDirection opposite() {
		switch (this) {
			case NORTH: return SOUTH;
			case EASY: return WEST;
			case SOUTH: return NORTH;
			case WEST: return EASY;
			case HORIZONTAL: return VERTICAL;
			case VERTICAL: return HORIZONTAL;
			case UP: return DOWN;
			case DOWN: return UP;
			case BOTH: return BOTH;
			default: return UNKNOWN;
		}
	}
}