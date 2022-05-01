package rip.crystal.practice.essentials.command.staff;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

public class DayCommand extends BaseCommand {

	@Command(name = "day", permission = "cpractice.command.day")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.setPlayerTime(6000L, false);
		player.sendMessage(CC.GREEN + "It's now day time.");
	}
}
