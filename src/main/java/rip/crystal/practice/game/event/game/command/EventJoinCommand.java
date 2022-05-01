package rip.crystal.practice.game.event.game.command;

import org.bukkit.entity.Player;
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
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.RED + "You cannot join the event while in a party.");
			player.sendMessage(CC.CHAT_BAR);
			return;
		}

		if (profile.isBusy()) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.RED + "You must be in the lobby to join the event.");
			player.sendMessage(CC.CHAT_BAR);
		} else {
			EventGame game = EventGame.getActiveGame();

			if (game != null) {
				if (game.getGameState() == EventGameState.WAITING_FOR_PLAYERS ||
						game.getGameState() == EventGameState.STARTING_EVENT) {
					if (game.getParticipants().size() < game.getMaximumPlayers()) {
						game.getGameLogic().onJoin(player);
					} else {
						player.sendMessage(CC.CHAT_BAR);
						player.sendMessage(CC.RED + "The event is full.");
						player.sendMessage(CC.CHAT_BAR);
					}
				} else {
					player.sendMessage(CC.CHAT_BAR);
					player.sendMessage(CC.RED + "The event has already started.");
					player.sendMessage(CC.CHAT_BAR);
				}
			} else {
				player.sendMessage(CC.CHAT_BAR);
				player.sendMessage(CC.RED + "There is no active event.");
				player.sendMessage(CC.CHAT_BAR);
			}
		}
	}
}
