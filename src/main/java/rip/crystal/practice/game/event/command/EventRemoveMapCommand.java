package rip.crystal.practice.game.event.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.Event;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.utilities.chat.CC;

public class EventRemoveMapCommand extends BaseCommand {

	@Command(name = "event.removemap", permission = "cpractice.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0 || args.length == 1) {
			player.sendMessage(CC.RED + "Please usage: /event removemap (event) (map)");
			return;
		}

		Event event = Event.getByName(args[0]);
		if (event == null) {
			player.sendMessage(CC.RED + "An event type by that name does not exist.");
			player.sendMessage(CC.RED + "Types: sumo, tntrun, spleef");
			return;
		}

		EventGameMap gameMap = EventGameMap.getByName(args[1]);
		if (gameMap == null) {
			player.sendMessage(CC.RED + "A map with that name does not exist.");
			return;
		}

		if (event.getAllowedMaps().remove(gameMap.getMapName())) {
			event.save();

			player.sendMessage(CC.GREEN + "You successfully removed the \"" + gameMap.getMapName() +
					"\" map from the \"" + event.getName() + "\" event.");
		}
	}
}
