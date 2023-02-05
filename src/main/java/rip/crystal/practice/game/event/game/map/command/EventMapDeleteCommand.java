package rip.crystal.practice.game.event.game.map.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class EventMapDeleteCommand extends BaseCommand {

	@Command(name = "event.map.delete", permission = "cpractice.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			new MessageFormat(Locale.EVENT_MAP_DELETION_USAGE.format(profile.getLocale()));
			return;
		}

		EventGameMap gameMap = EventGameMap.getByName(args[0]);
		if (gameMap == null) {
			new MessageFormat(Locale.EVENT_MAP_DOES_NOT_EXIST.format(profile.getLocale()));
			return;
		}

		gameMap.delete();
		EventGameMap.getMaps().remove(gameMap);
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.GREEN + "You successfully deleted the event map \"" + gameMap.getMapName() + "\".");
		player.sendMessage(CC.CHAT_BAR);
	}
}
