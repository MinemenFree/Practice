package rip.crystal.practice.essentials.command.staff;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

import java.util.Random;

public class CreateWorldCommand extends BaseCommand {

	@Command(name = "createvoidworld", permission = "cpractice.command.createworld")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please insert a world name.");
			return;
		}
		else if (args.length == 1) {
			player.sendMessage(CC.RED + "Please insert a type (normal | nether | the_end)");
			return;
		}

		String world = args[0];
		String type = args[1];
		WorldCreator worldCreator = new WorldCreator(world)
				.generator(new ChunkGenerator() {
					@Override
					public byte[] generate(World world, Random random, int x, int z) {
						return new byte[32768];
					}
				});
		if (type.equalsIgnoreCase("normal")) {
			worldCreator.environment(World.Environment.NORMAL);
			Bukkit.createWorld(worldCreator);
		}
		else if (type.equalsIgnoreCase("nether")) {
			worldCreator.environment(World.Environment.NETHER);
			Bukkit.createWorld(worldCreator);
		}
		else if (type.equalsIgnoreCase("the_end")) {
			worldCreator.environment(World.Environment.THE_END);
			Bukkit.createWorld(worldCreator);
		}
		else {
			player.sendMessage(CC.RED + "Please insert a valid type (normal | nether | the_end)");
			return;
		}
		player.sendMessage(CC.GREEN + "Creating World!!!");
	}
}
