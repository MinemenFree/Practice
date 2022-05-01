package rip.crystal.practice.game.event.game.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.utilities.chat.CC;

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
