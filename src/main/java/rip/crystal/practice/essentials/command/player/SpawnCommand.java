package rip.crystal.practice.essentials.command.player;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.chat.CC;

public class SpawnCommand extends BaseCommand {

	@Command(name = "spawn", permission = "cpractice.command.spawn")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if(profile.getState() == ProfileState.SPECTATING || profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.EVENT || profile.getState() == ProfileState.FFA) {
			player.sendMessage(CC.translate("&cYou cannot do /spawn while spectating or in game."));
			return;
		}

		cPractice.get().getEssentials().teleportToSpawn(player);
		player.sendMessage(CC.GREEN + "You teleported to this world's spawn.");
	}
}