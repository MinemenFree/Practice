package rip.crystal.practice.game.event.game.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.chat.CC;

public class EventClearCooldownCommand extends BaseCommand {

	@Command(name = "event.clearcooldown", aliases = {"clearcd"}, permission = "cpractice.event.admin", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();

		EventGame.setCooldown(new Cooldown(0));
		sender.sendMessage(CC.CHAT_BAR);
		sender.sendMessage(ChatColor.GREEN + "You cleared the event cooldown.");
		sender.sendMessage(CC.CHAT_BAR);
	}
}
