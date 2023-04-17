package rip.crystal.practice.game.event.game.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.impl.sumo.SumoEvent;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.MessageFormat;

public class EventInfoCommand extends BaseCommand {

	@Command(name = "event.info")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(getPlayer().getUniqueId());

		if (EventGame.getActiveGame() == null) {
			new MessageFormat(Locale.EVENT_NO_ACTIVE_EVENT.format(profile.getLocale()));
			return;
		}

		EventGame game = EventGame.getActiveGame();

		new MessageFormat(Locale.EVENT_INFO.format(profile.getLocale()))
                	.add("<player_limit>", Integer.toString(game.getMaximumPlayers()))
			.add("<event_type>", game.getEvent().getName())
                	.add("<remaining_players>", Integer.toString(game.getRemainingPlayers()))
                	.add("<game_state>", game.getGameState().getReadable())
			.send(player);
		/*player.sendMessage(CC.translate("&7(*) &c&lEvent &7(*)"));
		player.sendMessage(CC.RED + "Player Limit: " + CC.WHITE + game.getMaximumPlayers());
		player.sendMessage(CC.RED + "Event Type: " + CC.WHITE + game.getEvent().getName());
		player.sendMessage(CC.RED + "Players: " + CC.WHITE + game.getRemainingPlayers());
		player.sendMessage(CC.RED + "State: " + CC.WHITE + game.getGameState().getReadable());*/

		if (game.getEvent() instanceof SumoEvent) {
			new MessageFormat(Locale.EVENT_SUMO_ROUND.format(profile.getLocale()))
                		.add("<player_limit>", Integer.toString(game.getMaximumPlayers()))
				.add("<event_type>", game.getEvent().getName())
                		.add("<remaining_players>", Integer.toString(game.getRemainingPlayers()))
                		.add("<game_state>", game.getGameState().getReadable())
				.add("<sumo_round>", Integer.toString(game.getGameLogic().getRoundNumber()))
				.send(player);
			//player.sendMessage(CC.RED + "Round: " + CC.WHITE + game.getGameLogic().getRoundNumber());
		}
	}
}
