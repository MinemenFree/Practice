package rip.crystal.practice.match.command;

import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class StopSpectatingCommand extends BaseCommand {

	@Command(name = "stopspectating")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getState() == ProfileState.FIGHTING && profile.getMatch().getGamePlayer(player).isDead()) {
			profile.getMatch().getGamePlayer(player).setDisconnected(true);
			profile.setState(ProfileState.LOBBY);
			profile.setMatch(null);
		} else if (profile.getState() == ProfileState.SPECTATING) {
			profile.getMatch().removeSpectator(player);
		} else {
			player.sendMessage(CC.RED + "You are not spectating a match.");
		}
	}
}
