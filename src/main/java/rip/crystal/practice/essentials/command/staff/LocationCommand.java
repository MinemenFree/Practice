package rip.crystal.practice.essentials.command.staff;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.LocationUtil;

public class LocationCommand extends BaseCommand {

	@Command(name = "location", aliases = {"loc"}, permission = "cpractice.command.loc")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(LocationUtil.serialize(player.getLocation()));
	}
}
