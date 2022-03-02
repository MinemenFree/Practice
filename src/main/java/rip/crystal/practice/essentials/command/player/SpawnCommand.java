package rip.crystal.practice.essentials.command.player;

import rip.crystal.practice.cPractice;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

	@Command(name = "spawn", permission = "hysteria.command.spawn")
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