package rip.crystal.practice.game.event.game.map.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.game.event.game.map.impl.SpreadEventGameMap;
import rip.crystal.practice.game.event.game.map.impl.TeamEventGameMap;
import rip.crystal.practice.utilities.chat.CC;

public class EventMapCreateCommand extends BaseCommand {

	@Command(name = "event.map.create", permission = "cpractice.event.maps")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0 || args.length == 1) {
			new MessageFormat(Locale.EVENT_MAP_CREATION_USAGE.format(profile.getLocale()));
			return;
		}

		String mapName = args[0];
		String mapType = args[1];

		if (EventGameMap.getByName(mapName) != null) {
			new MessageFormat(Locale.EVENT_MAP_ALREADY_EXISTS.format(profile.getLocale()));
			return;
		}

		EventGameMap gameMap;

		if (mapType.equalsIgnoreCase("TEAM")) {
			gameMap = new TeamEventGameMap(mapName);
		} else if (mapType.equalsIgnoreCase("SPREAD")) {
			gameMap = new SpreadEventGameMap(mapName);
		} else {
			player.sendMessage(CC.CHAT_BAR);
			new MessageFormat(Locale.EVENT_MAP_TYPE_NOT_VALID.format(profile.getLocale()));
			player.sendMessage(CC.RED + "That event map type is not valid. Pick either \"TEAM\" or \"SPREAD\"!");
			player.sendMessage(CC.CHAT_BAR);
			return;
		}

		gameMap.save();

		EventGameMap.getMaps().add(gameMap);

		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.GREEN + "You successfully created the event map \"" + mapName + "\".");
		player.sendMessage(CC.CHAT_BAR);
	}
}
