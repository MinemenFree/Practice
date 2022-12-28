package rip.crystal.practice.essentials.command.management;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.Practice;
import rip.crystal.practice.utilities.chat.CC;

public class SetSpawnCommand extends BaseCommand {

	@Command(name = "setspawn", permission = "practice.command.setspawn")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		Practice.get().getEssentials().setSpawnAndSave(player.getLocation());
		player.sendMessage(CC.GREEN + "You updated this world's spawn.");
	}
}
