package rip.crystal.practice.game.event.game.map.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.game.event.game.map.impl.SpreadEventGameMap;
import rip.crystal.practice.game.event.game.map.impl.TeamEventGameMap;
import rip.crystal.practice.utilities.chat.CC;

public class EventMapSetSpawnCommand extends BaseCommand {

	@Command(name = "event.map.setspawn", aliases = {"addspawn"}, permission = "cpractice.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0 || args.length == 1) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.RED + "Please usage: /event map setspawn (mapName) (a|b|spectator|spread)");
			player.sendMessage(CC.CHAT_BAR);
			return;
		}

		EventGameMap map = EventGameMap.getByName(args[0]);
		String field = args[1];
		if (map == null) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.RED + "An event map with that name does not exist.");
			player.sendMessage(CC.CHAT_BAR);
		} else {
			switch (field.toLowerCase()) {
				case "spectator": {
					map.setSpectatorPoint(player.getLocation());

					player.sendMessage(CC.CHAT_BAR);
					player.sendMessage(CC.GREEN + "You successfully updated " +
							map.getMapName() + "'s " + field + " location.");
					player.sendMessage(CC.CHAT_BAR);
				}
				break;
				case "a": {
					if (!(map instanceof TeamEventGameMap)) {
						player.sendMessage(CC.CHAT_BAR);
						player.sendMessage(CC.RED + "That type of map only has spread locations!");
						player.sendMessage(CC.RED + "To add a location to the spread list, use " +
								"/event map set <map> spread.");
						player.sendMessage(CC.CHAT_BAR);
						break;
					}

					TeamEventGameMap teamMap = (TeamEventGameMap) map;
					teamMap.setSpawnPointA(player.getLocation());

					player.sendMessage(CC.CHAT_BAR);
					player.sendMessage(CC.GREEN + "You successfully updated " +
							map.getMapName() + "'s " + field + " location.");
					player.sendMessage(CC.CHAT_BAR);
				}
				break;
				case "b": {
					if (!(map instanceof TeamEventGameMap)) {
						player.sendMessage(CC.CHAT_BAR);
						player.sendMessage(CC.RED + "That type of map only has spread locations!");
						player.sendMessage(CC.RED + "To add a location to the spread list, use " +
								"/event map set <map> spread.");
						player.sendMessage(CC.CHAT_BAR);
						break;
					}

					TeamEventGameMap teamMap = (TeamEventGameMap) map;
					teamMap.setSpawnPointB(player.getLocation());

					player.sendMessage(CC.GREEN + "You successfully updated " +
							map.getMapName() + "'s " + field + " location.");
				}
				break;
				case "spread": {
					if (!(map instanceof SpreadEventGameMap)) {
						player.sendMessage(CC.RED + "That type of map does not have spread locations!");
						player.sendMessage(CC.RED + "To set one of the locations, use " +
								"/event map set <map> <a/b>.");
						break;
					}

					SpreadEventGameMap spreadMap = (SpreadEventGameMap) map;
					spreadMap.getSpawnLocations().add(player.getLocation());

					player.sendMessage(CC.GREEN + "You successfully added a location to " +
							map.getMapName() + "'s " + field + " list.");
				}
				break;
				default:
					player.sendMessage(CC.RED + "A field by that name does not exist.");
					player.sendMessage(CC.RED + "Fields: spectator, a, b");
					return;
			}

			map.save();
		}
	}
}
