package rip.crystal.practice.game.arena.command;

import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.ArenaType;
import rip.crystal.practice.game.arena.generator.ArenaGenerator;
import rip.crystal.practice.game.arena.generator.Schematic;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;

import java.io.File;
import java.util.Objects;

public class ArenaGenerateCommand extends BaseCommand {

	@Command(name = "arena.generate", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		File schematicsFolder = new File(cPractice.get().getDataFolder().getPath() + File.separator + "schematics");

		if (!schematicsFolder.exists()) {
			player.sendMessage(CC.RED + "The \"schematics\" folder doesn't exist.");
			return;
		}

		for (File file : Objects.requireNonNull(schematicsFolder.listFiles())) {
			if (!file.isDirectory() && file.getName().contains(".schematic")) {
				boolean duplicate = file.getName().endsWith("_duplicate.schematic");

				String name = file.getName()
						.replace(".schematic", Strings.EMPTY)
						.replace("_duplicate", Strings.EMPTY);

				Arena parent = Arena.getByName(name);

				if (parent != null) {
					if (!(parent instanceof StandaloneArena)) {
						System.out.println("Skipping " + name + " because it's not duplicate and an arena with that name already exists.");
						continue;
					}
				}

				TaskUtil.run(() -> {
					try {
						new ArenaGenerator(name, player.getLocation().getWorld(), new Schematic(file), duplicate ?
								(parent != null ? ArenaType.DUPLICATE : ArenaType.STANDALONE) : ArenaType.SHARED)
								.generate(file, (StandaloneArena) parent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		}

		player.sendMessage(CC.GREEN + "Generating arenas... See console for details.");
	}
}
