package rip.crystal.practice.event.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.event.game.map.command.EventMapCommand;
import rip.crystal.practice.event.game.map.command.EventMapsCommand;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;
import rip.crystal.practice.event.game.command.*;

public class EventCommand extends BaseCommand {

	public EventCommand() {
		super();
		new EventAddMapCommand();
		new EventAdminCommand();
		new EventRemoveMapCommand();
		new EventSetLobbyCommand();
		new EventCancelCommand();
		new EventClearCooldownCommand();
		new EventForceStartCommand();
		new EventInfoCommand();
		new EventJoinCommand();
		new EventLeaveCommand();
		new EventMapCommand();
		new EventMapsCommand();
	}

	@Command(name = "event")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		new MessageFormat(Locale.EVENT_HELP.format(Profile.get(player.getUniqueId()).getLocale())).send(player);
	}
}
