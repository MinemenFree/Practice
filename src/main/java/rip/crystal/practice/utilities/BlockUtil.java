package rip.crystal.practice.utilities;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class BlockUtil {

    private static ImmutableSet<Byte> blockSolidPassSet;
    private static Set<Byte> blockStairsSet;
    private static Set<Byte> blockLiquidsSet;
    private static Set<Byte> blockWebsSet;
    private static Set<Byte> blockIceSet;

    public static boolean isOnGround(Location location, int down) {
        double posX = location.getX();
        double posZ = location.getZ();
        double fracX = (posX % 1.0 > 0.0) ? Math.abs(posX % 1.0) : (1.0 - Math.abs(posX % 1.0));
        double fracZ = (posZ % 1.0 > 0.0) ? Math.abs(posZ % 1.0) : (1.0 - Math.abs(posZ % 1.0));
        int blockX = location.getBlockX();
        int blockY = location.getBlockY() - down;
        int blockZ = location.getBlockZ();
        World world = location.getWorld();
        if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
            return true;
        }
        if (fracX < 0.3) {
            if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }
                if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }
                return !BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }
                if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }
                return !BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracX > 0.7) {
            if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }
                if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }
                return !BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }
                if (!BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }
                return !BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracZ < 0.3) {
            return !BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId());
        } else return fracZ > 0.7 && !BlockUtil.blockSolidPassSet.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId());
        return false;
    }
    
    public static boolean isOnStairs(Location location, int down) {
        return isUnderBlock(location, BlockUtil.blockStairsSet, down);
    }
    
    private static boolean isUnderBlock(Location location, Set<Byte> itemIDs, int down) {
        double posX = location.getX();
        double posZ = location.getZ();
        double fracX = (posX % 1.0 > 0.0) ? Math.abs(posX % 1.0) : (1.0 - Math.abs(posX % 1.0));
        double fracZ = (posZ % 1.0 > 0.0) ? Math.abs(posZ % 1.0) : (1.0 - Math.abs(posZ % 1.0));
        int blockX = location.getBlockX();
        int blockY = location.getBlockY() - down;
        int blockZ = location.getBlockZ();
        World world = location.getWorld();
        if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
            return true;
        }
        if (fracX < 0.3) {
            if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }
                if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }
                return itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }
                if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }
                return itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracX > 0.7) {
            if (itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) return true;

                if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) return true;

                return itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {

                if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId()))return true;

                if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) return true;

                return itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracZ < 0.3) {
            return itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId());
        } else return fracZ > 0.7 && itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId());
        return false;
    }
    
    public static boolean isOnLiquid(Location location, int down) {
        return isUnderBlock(location, BlockUtil.blockLiquidsSet, down);
    }
    
    public static boolean isOnWeb(Location location, int down) {
        return isUnderBlock(location, BlockUtil.blockWebsSet, down);
    }
    
    public static boolean isOnIce(Location location, int down) {
        return isUnderBlock(location, BlockUtil.blockIceSet, down);
    }
    
    static {
        BlockUtil.blockSolidPassSet = ImmutableSet.of(
            (byte) 0, (byte) 6, (byte) 8, (byte) 9,
            (byte) 10, (byte) 11,
            (byte) 27, (byte) 29,
            (byte) 30, (byte) 31, (byte) 32, (byte) 37, (byte) 38, (byte) 39,
            (byte) 40,
            (byte) 50, (byte) 51, (byte) 55, (byte) 59,
            (byte) 63, (byte) 66, (byte) 68, (byte) 69,
            (byte) 70, (byte) 72, (byte) 75, (byte) 76, (byte) 77, (byte) 78,
            (byte) 83,
            (byte) 90,
            (byte) 104, (byte) 105, (byte) 115, (byte) 119,
            (byte) (-124), (byte) (-113),
            (byte) (-81));
        BlockUtil.blockStairsSet = new HashSet<>();
        BlockUtil.blockLiquidsSet = new HashSet<>();
        BlockUtil.blockWebsSet = new HashSet<>();
        BlockUtil.blockIceSet = new HashSet<>();
        BlockUtil.blockStairsSet.add((byte) 53);
        BlockUtil.blockStairsSet.add((byte) 67);
        BlockUtil.blockStairsSet.add((byte) 108);
        BlockUtil.blockStairsSet.add((byte) 109);
        BlockUtil.blockStairsSet.add((byte) 114);
        BlockUtil.blockStairsSet.add((byte) (-128));
        BlockUtil.blockStairsSet.add((byte) (-122));
        BlockUtil.blockStairsSet.add((byte) (-121));
        BlockUtil.blockStairsSet.add((byte) (-120));
        BlockUtil.blockStairsSet.add((byte) (-100));
        BlockUtil.blockStairsSet.add((byte) (-93));
        BlockUtil.blockStairsSet.add((byte) (-92));
        BlockUtil.blockStairsSet.add((byte) 126);
        BlockUtil.blockStairsSet.add((byte) (-76));
        BlockUtil.blockLiquidsSet.add((byte) 8);
        BlockUtil.blockLiquidsSet.add((byte) 9);
        BlockUtil.blockLiquidsSet.add((byte) 10);
        BlockUtil.blockLiquidsSet.add((byte) 11);
        BlockUtil.blockWebsSet.add((byte) 30);
        BlockUtil.blockIceSet.add((byte) 79);
        BlockUtil.blockIceSet.add((byte) (-82));
    }
}
