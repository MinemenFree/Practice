package rip.crystal.practice.kit.command;

import rip.crystal.practice.kit.Kit;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitGetLoadoutCommand extends BaseCommand {

	@Command(name = "kit.getloadout", aliases = {"getinv", "getinventory"}, permission = "hysteria.kit.admin")
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
