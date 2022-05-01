package rip.crystal.practice.game.arena;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.cuboid.Cuboid;
import rip.crystal.practice.game.arena.impl.SharedArena;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.LocationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Arena extends Cuboid {

	@Getter private final static List<Arena> arenas = new ArrayList<>();

	@Getter protected String name;
	@Setter protected Location spawnA;
	@Setter protected Location spawnB;
	@Setter private ItemStack displayIcon;
	@Getter protected boolean active;
	@Getter @Setter private List<String> kits = new ArrayList<>();
	@Getter @Setter private String author = "Unknown";

	private final transient Map<Long, ChunkSnapshot> chunkSnapshots = Maps.newHashMap();

	public Arena(String name, Location location1, Location location2) {
		super(location1, location2);
		this.name = name;
		this.displayIcon = new ItemStack(Material.GRASS);
	}

	public ArenaType getType() {
		return ArenaType.DUPLICATE;
	}

	public boolean isSetup() {
		return getLowerCorner() != null && getUpperCorner() != null && spawnA != null && spawnB != null;
	}

	public int getMaxBuildHeight() {
		int highest = (int) (Math.max(spawnA.getY(), spawnB.getY()));
		return highest + 5;
	}

	public Location getSpawnA() {
		if (spawnA == null) return null;

		return spawnA.clone();
	}

	public Location getSpawnB() {
		if (spawnB == null) return null;

		return spawnB.clone();
	}

	public void setActive(boolean active) {
		if (getType() != ArenaType.SHARED) this.active = active;
	}

	public void save() {

	}

	public void delete() {
		arenas.remove(this);
	}

	public static void init() {
		FileConfiguration configuration = cPractice.get().getArenasConfig().getConfiguration();

		Location location1FFA = LocationUtil.deserialize(cPractice.get().getMainConfig().getString("FFA.SAFEZONE.location1"));
		Location location2FFA = LocationUtil.deserialize(cPractice.get().getMainConfig().getString("FFA.SAFEZONE.location2"));
		cPractice.get().getFfaManager().setFfaSafezone(new Cuboid(location1FFA, location2FFA));

		if (configuration.contains("arenas")) {
			for (String arenaName : configuration.getConfigurationSection("arenas").getKeys(false)) {
				String path = "arenas." + arenaName;

				ArenaType arenaType = ArenaType.valueOf(configuration.getString(path + ".type"));
				Location location1 = LocationUtil.deserialize(configuration.getString(path + ".cuboid.location1"));
				Location location2 = LocationUtil.deserialize(configuration.getString(path + ".cuboid.location2"));

				Arena arena;

				if (arenaType == ArenaType.STANDALONE) arena = new StandaloneArena(arenaName, location1, location2);
				else if (arenaType == ArenaType.SHARED) arena = new SharedArena(arenaName, location1, location2);
				else continue;

				if (configuration.contains(path + ".icon")) {
					arena.setDisplayIcon(new ItemBuilder(Material.valueOf(configuration.getString(path + ".icon.material")))
							.durability(configuration.getInt(path + ".icon.durability"))
							.build());
				}

				if (configuration.contains(path + ".spawnA"))
					arena.setSpawnA(LocationUtil.deserialize(configuration.getString(path + ".spawnA")));

				if (configuration.contains(path + ".spawnB"))
					arena.setSpawnB(LocationUtil.deserialize(configuration.getString(path + ".spawnB")));

				if (configuration.contains(path + ".author")) {
					String author = configuration.getString(path + ".author");
					arena.setAuthor(author);
				}

				if (configuration.contains(path + ".kits")) {
					for (String kitName : configuration.getStringList(path + ".kits")) {
						arena.getKits().add(kitName);
					}
				}

				if (arena instanceof StandaloneArena && configuration.contains(path + ".spawnred") && configuration.contains(path + ".spawnblue")) {
					StandaloneArena standaloneArena = (StandaloneArena) arena;
					location1 = LocationUtil.deserialize(configuration.getString(path + ".spawnred.location1"));
					location2 = LocationUtil.deserialize(configuration.getString(path + ".spawnred.location2"));
					standaloneArena.setSpawnRed(new Cuboid(location1, location2));
					location1 = LocationUtil.deserialize(configuration.getString(path + ".spawnblue.location1"));
					location2 = LocationUtil.deserialize(configuration.getString(path + ".spawnblue.location2"));
					standaloneArena.setSpawnBlue(new Cuboid(location1, location2));
				}

				if (arena instanceof StandaloneArena && configuration.contains(path + ".duplicates")) {
					for (String duplicateId : configuration.getConfigurationSection(path + ".duplicates").getKeys(false)) {
						location1 = LocationUtil.deserialize(configuration.getString(path + ".duplicates." + duplicateId + ".cuboid.location1"));
						location2 = LocationUtil.deserialize(configuration.getString(path + ".duplicates." + duplicateId + ".cuboid.location2"));
						Location spawn1 = LocationUtil.deserialize(configuration.getString(path + ".duplicates." + duplicateId + ".spawnA"));
						Location spawn2 = LocationUtil.deserialize(configuration.getString(path + ".duplicates." + duplicateId + ".spawnB"));

						Arena duplicate = new Arena(arenaName, location1, location2);

						duplicate.setSpawnA(spawn1);
						duplicate.setSpawnB(spawn2);
						duplicate.setKits(arena.getKits());

						((StandaloneArena) arena).getDuplicates().add(duplicate);

						Arena.getArenas().add(duplicate);
					}
				}

				Arena.getArenas().add(arena);
			}
		}

		getArenas().forEach(arenas -> arenas.getChunks().forEach(Chunk::load));
	}

	public static Arena getByName(String name) {
		for (Arena arena : arenas) {
			if (arena.getType() != ArenaType.DUPLICATE && arena.getName() != null &&
			    arena.getName().equalsIgnoreCase(name)) {
				return arena;
			}
		}

		return null;
	}

	public static Arena getRandomArena(Kit kit) {
		List<Arena> _arenas = new ArrayList<>();

		for (Arena arena : arenas) {
			if (!arena.isSetup()) continue;

			if (!arena.getKits().contains(kit.getName())) continue;

			if ((kit.getGameRules().isBuild() || kit.getGameRules().isHcftrap() || kit.getGameRules().isSpleef() || kit.getGameRules().isBridge()) && !arena.isActive() && (arena.getType() == ArenaType.STANDALONE || arena.getType() == ArenaType.DUPLICATE)) {
				_arenas.add(arena);
			} else if ((!kit.getGameRules().isBuild() || !kit.getGameRules().isHcftrap() || !kit.getGameRules().isSpleef() || !kit.getGameRules().isBridge()) && arena.getType() == ArenaType.SHARED) {
				_arenas.add(arena);
			}
		}

		if (_arenas.isEmpty()) return null;

		return _arenas.get(ThreadLocalRandom.current().nextInt(_arenas.size()));
	}

	public ItemStack getDisplayIcon() {
		return this.displayIcon.clone();
	}
}
