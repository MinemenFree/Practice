package rip.crystal.practice.arena.command;

import rip.crystal.practice.arena.Arena;
import rip.crystal.practice.kit.Kit;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaRemoveKitCommand extends BaseCommand {

	@Command(name = "arena.removekit", permission = "hysteria.arena.admin")
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

		arena.getKits().remove(kit.getName());
		arena.save();

		player.sendMessage(ChatColor.RED + "Removed kit &f\"" + kit.getName() +
				"\" &cfrom arena &f\"" + arena.getName() + "\"");
	}
}
