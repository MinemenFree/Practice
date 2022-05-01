package rip.crystal.practice.game.event.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.command.*;
import rip.crystal.practice.game.event.game.map.command.EventMapCommand;
import rip.crystal.practice.game.event.game.map.command.EventMapsCommand;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

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
