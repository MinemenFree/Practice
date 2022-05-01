package rip.crystal.practice.game.event.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.Event;
import rip.crystal.practice.utilities.chat.CC;

public class EventSetLobbyCommand extends BaseCommand {

	@Command(name = "event.setlobby", permission = "cpractice.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /event setlobby (event)");
			return;
		}

		Event event = Event.getByName(args[0]);
		if (event == null) {
			player.sendMessage(CC.RED + "An event with that name does not exist.");
			return;
		}

		event.setLobbyLocation(player.getLocation());
		event.save();

		player.sendMessage(ChatColor.GOLD + "You updated the " + ChatColor.GREEN + event.getName() +
				ChatColor.GOLD + " Event's lobby location.");
	}
}
