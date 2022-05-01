package rip.crystal.practice.game.event.game.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.chat.CC;

public class EventLeaveCommand extends BaseCommand {

	@Command(name = "event.leave")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getState() == ProfileState.EVENT) {
			if (EventGame.getActiveGame().getGameHost().getUsername().equals(player.getName())) {
				player.sendMessage(CC.CHAT_BAR);
				player.sendMessage(CC.translate("&cYou cannot leave the event since you are the one who hosts."));
				player.sendMessage(CC.CHAT_BAR);
				return;
			}
			EventGame.getActiveGame().getGameLogic().onLeave(player);
		} else {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.RED + "You are not in an event.");
			player.sendMessage(CC.CHAT_BAR);
		}
	}
}
