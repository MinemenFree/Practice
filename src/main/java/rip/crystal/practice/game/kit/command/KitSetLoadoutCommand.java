package rip.crystal.practice.game.kit.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.chat.CC;

public class KitSetLoadoutCommand extends BaseCommand {

	@Command(name = "kit.setloadout", aliases = {"setinv", "setinventory"}, permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit setloadout (kit)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}

		kit.getKitLoadout().setArmor(player.getInventory().getArmorContents());
		kit.getKitLoadout().setContents(player.getInventory().getContents());
		kit.save();

		player.sendMessage(CC.GREEN + "You updated the kit's loadout.");
	}
}
