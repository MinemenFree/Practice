package rip.crystal.practice.game.event.game.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.EventGameState;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;

public class EventJoinCommand extends BaseCommand {

	@Command(name = "event.join")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getParty() != null) {
			new MessageFormat(Locale.EVENT_CANT_JOIN_IN_PARTY.format(profile.getLocale()));
			return;
		}

		if (profile.isBusy()) {
			new MessageFormat(Locale.EVENT_MUST_JOIN_IN_LOBBY.format(profile.getLocale()));
		} else {
			EventGame game = EventGame.getActiveGame();

			if (game != null) {
				if (game.getGameState() == EventGameState.WAITING_FOR_PLAYERS ||
						game.getGameState() == EventGameState.STARTING_EVENT) {
					if (game.getParticipants().size() < game.getMaximumPlayers()) {
						game.getGameLogic().onJoin(player);
					} else {
						new MessageFormat(Locale.EVENT_IS_FULL.format(profile.getLocale()));
					}
				} else {
					new MessageFormat(Locale.EVENT_ALREADY_STARTED.format(profile.getLocale()));
				}
			} else {
				new MessageFormat(Locale.EVENT_NO_ACTIVE_EVENT.format(profile.getLocale()));
			}
		}
	}
}
