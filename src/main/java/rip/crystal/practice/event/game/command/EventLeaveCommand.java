package rip.crystal.practice.event.game.command;

import rip.crystal.practice.event.game.EventGame;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

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
