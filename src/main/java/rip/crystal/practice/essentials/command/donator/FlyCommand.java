package rip.crystal.practice.essentials.command.donator;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.chat.CC;

public class FlyCommand extends BaseCommand {

	@Command(name = "fly", permission = "cpractice.fly")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
			if (player.getAllowFlight()) {
				player.setAllowFlight(false);
				player.setFlying(false);
				player.updateInventory();
				player.sendMessage(CC.RED + "You are no longer flying.");
			} else {
				player.setAllowFlight(true);
				player.setFlying(true);
				player.updateInventory();
				player.sendMessage(CC.GREEN + "You are now flying.");
			}
		} else {
			player.sendMessage(CC.RED + "You cannot fly right now.");
		}
	}
}
