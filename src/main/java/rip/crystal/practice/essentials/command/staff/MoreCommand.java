package rip.crystal.practice.essentials.command.staff;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

public class MoreCommand extends BaseCommand {

	@Command(name = "more", permission = "v.command.more")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		if (player.getItemInHand() == null) {
			player.sendMessage(CC.RED + "There is nothing in your hand.");
			return;
		}

		player.getItemInHand().setAmount(64);
		player.updateInventory();
		player.sendMessage(CC.GREEN + "You gave yourself more of the item in your hand.");
	}
}
