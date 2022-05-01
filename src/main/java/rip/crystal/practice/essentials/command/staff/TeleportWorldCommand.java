package rip.crystal.practice.essentials.command.staff;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

public class TeleportWorldCommand extends BaseCommand {

	@Command(name = "tpworld", aliases = {"world", "changeworld"}, permission = "cpractice.command.tpworld")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /tpworld (world)");
			return;
		}

		World world = Bukkit.getWorld(args[0]);
		if (world == null) {
			player.sendMessage(CC.RED + "A world with that name does not exist.");
		} else {
			player.teleport(world.getSpawnLocation());
			player.sendMessage(CC.GOLD + "Teleported you to " + world.getName());
		}
	}
}
