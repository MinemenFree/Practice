package rip.crystal.practice.game.kit.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.chat.CC;

public class KitGetLoadoutCommand extends BaseCommand {

	@Command(name = "kit.getloadout", aliases = {"getinv", "getinventory"}, permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit getloadout (kit)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}

		player.getInventory().setArmorContents(kit.getKitLoadout().getArmor());
		player.getInventory().setContents(kit.getKitLoadout().getContents());
		player.updateInventory();

		player.sendMessage(CC.GREEN + "You received the kit's loadout.");
	}
}
