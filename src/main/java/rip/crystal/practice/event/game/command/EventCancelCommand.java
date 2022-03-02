package rip.crystal.practice.event.game.command;

import rip.crystal.practice.event.game.EventGame;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class EventCancelCommand extends BaseCommand {

	@Command(name = "event.cancel", permission = "cpractice.event.admin", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();

		if (EventGame.getActiveGame() != null) {
			EventGame.getActiveGame().getGameLogic().cancelEvent();
		} else {
			sender.sendMessage(CC.CHAT_BAR);
			sender.sendMessage(ChatColor.RED + "There is no active event.");
			sender.sendMessage(CC.CHAT_BAR);
		}
	}
}
