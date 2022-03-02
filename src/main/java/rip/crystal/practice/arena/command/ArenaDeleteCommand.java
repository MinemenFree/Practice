package rip.crystal.practice.arena.command;

import rip.crystal.practice.arena.Arena;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ArenaDeleteCommand extends BaseCommand {

	@Command(name = "arena.delete", permission = "hysteria.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		Arena arena = Arena.getByName(args[0]);
		if (arena != null) {
			arena.delete();

			player.sendMessage(CC.translate("Deleted arena &f\"" + arena.getName() + "\""));
		} else {
			player.sendMessage(CC.translate("&cAn arena with that name does not exist."));
		}
	}

}
