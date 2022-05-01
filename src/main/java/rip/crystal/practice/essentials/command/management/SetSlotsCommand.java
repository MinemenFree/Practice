package rip.crystal.practice.essentials.command.management;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.BukkitReflection;
import rip.crystal.practice.utilities.chat.CC;

public class SetSlotsCommand extends BaseCommand {

	@Command(name = "setslots", permission = "cpractice.command.setslots")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please insert a valid slot.");
			return;
		}

		int slots;
		if (!StringUtils.isNumeric(args[0])) {
			player.sendMessage(CC.RED + "Please insert a valid integer");
			return;
		}
		slots = Integer.getInteger(args[0]);

		BukkitReflection.setMaxPlayers(cPractice.get().getServer(), slots);
		player.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
	}
}
