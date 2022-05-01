package rip.crystal.practice.match.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.match.MatchSnapshot;
import rip.crystal.practice.match.menu.MatchDetailsMenu;
import rip.crystal.practice.utilities.chat.CC;

import java.util.UUID;

public class ViewInventoryCommand extends BaseCommand {

	@Command(name = "viewinv")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /viewinv (id)");
			return;
		}

		String id = args[0];
		MatchSnapshot cachedInventory;

		try {
			cachedInventory = MatchSnapshot.getByUuid(UUID.fromString(id));
		} catch (Exception e) {
			cachedInventory = MatchSnapshot.getByName(id);
		}

		if (cachedInventory == null) {
			player.sendMessage(CC.RED + "Couldn't find an inventory for that ID.");
			return;
		}

		new MatchDetailsMenu(cachedInventory).openMenu(player);
	}
}
