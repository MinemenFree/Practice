package rip.crystal.practice.essentials.command.staff;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

public class RetardCommand extends BaseCommand {

	@Command(name = "retard", permission = "cpractice.command.nigga")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&4&lOPTIFINE-REDUCER IS A RETARD!"));
		player.sendMessage(CC.CHAT_BAR);
	}
}
