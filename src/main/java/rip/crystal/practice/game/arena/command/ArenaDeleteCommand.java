package rip.crystal.practice.game.arena.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.utilities.chat.CC;

public class ArenaDeleteCommand extends BaseCommand {

	@Command(name = "arena.delete", permission = "cpractice.arena.admin")
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
