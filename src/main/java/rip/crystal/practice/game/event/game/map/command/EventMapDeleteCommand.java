package rip.crystal.practice.game.event.game.map.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.utilities.chat.CC;

public class EventMapDeleteCommand extends BaseCommand {

	@Command(name = "event.map.delete", permission = "cpractice.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.RED + "Please usage: /event map delete (mapName)");
			player.sendMessage(CC.CHAT_BAR);
			return;
		}

		EventGameMap gameMap = EventGameMap.getByName(args[0]);
		if (gameMap == null) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.RED + "An event map with that name already exists.");
			player.sendMessage(CC.CHAT_BAR);
			return;
		}

		gameMap.delete();
		EventGameMap.getMaps().remove(gameMap);
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.GREEN + "You successfully deleted the event map \"" + gameMap.getMapName() + "\".");
		player.sendMessage(CC.CHAT_BAR);
	}
}
