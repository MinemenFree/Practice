package rip.crystal.practice.game.event.game.map.command;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.game.event.game.map.impl.SpreadEventGameMap;
import rip.crystal.practice.game.event.game.map.impl.TeamEventGameMap;
import rip.crystal.practice.utilities.chat.CC;

public class EventMapStatusCommand extends BaseCommand {

	@Command(name = "event.map.status", permission = "cpractice.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /event map status (mapName)");
			return;
		}

		EventGameMap gameMap = EventGameMap.getByName(args[0]);
		if (gameMap == null) {
			player.sendMessage(CC.RED + "An event map with that name does not exist.");
		} else {
			player.sendMessage(CC.GOLD + CC.BOLD + "Event Map Status " + CC.GRAY + "(" +
					(gameMap.isSetup() ? CC.GREEN : CC.RED) + gameMap.getMapName() + CC.GRAY + ")");
			player.sendMessage(CC.GREEN + "Spectator Location: " + CC.YELLOW +
					(gameMap.getSpectatorPoint() == null ?
							StringEscapeUtils.unescapeJava("\u2717") :
							StringEscapeUtils.unescapeJava("\u2713")));

			if (gameMap instanceof TeamEventGameMap) {
				TeamEventGameMap teamGameMap = (TeamEventGameMap) gameMap;

				player.sendMessage(CC.GREEN + "Spawn A Location: " + CC.YELLOW +
						(teamGameMap.getSpawnPointA() == null ?
								StringEscapeUtils.unescapeJava("\u2717") :
								StringEscapeUtils.unescapeJava("\u2713")));

				player.sendMessage(CC.GREEN + "Spawn B Location: " + CC.YELLOW +
						(teamGameMap.getSpawnPointB() == null ?
								StringEscapeUtils.unescapeJava("\u2717") :
								StringEscapeUtils.unescapeJava("\u2713")));
			} else if (gameMap instanceof SpreadEventGameMap) {
				SpreadEventGameMap spreadGameMap = (SpreadEventGameMap) gameMap;

				player.sendMessage(CC.GREEN + "Spread Locations: " + CC.YELLOW +
						(spreadGameMap.getSpawnLocations().isEmpty() ?
								StringEscapeUtils.unescapeJava("\u2717") :
								StringEscapeUtils.unescapeJava("\u2713")));
			}
		}
	}
}
