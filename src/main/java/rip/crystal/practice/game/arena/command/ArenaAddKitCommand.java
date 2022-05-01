package rip.crystal.practice.game.arena.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;

public class ArenaAddKitCommand extends BaseCommand {

	@Command(name = "arena.addkit", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		Arena arena = Arena.getByName(args[0]);
		if (arena == null) {
			player.sendMessage(ChatColor.RED + "An arena with that name does not exist.");
			return;
		}

		Kit kit = Kit.getByName(args[1]);
		if (kit == null) {
			player.sendMessage(ChatColor.RED + "A kit with that name does not exist.");
			return;
		}

		arena.getKits().add(kit.getName());
		arena.save();

		player.sendMessage(ChatColor.RED + "Added kit \"&f" + kit.getName() +
				"\" &cto arena \"&f" + arena.getName() + "&c\"");
	}

}
