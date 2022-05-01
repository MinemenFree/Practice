package rip.crystal.practice.game.kit.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.chat.CC;

public class KitSetSlotCommand extends BaseCommand {

	@Command(name = "kit.setslot", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit setslot (kit) (slot)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}

		int slot;
		if (!StringUtils.isNumeric(args[1])) {
			player.sendMessage(CC.RED + "Please usage a valid slot.");
			return;
		}
		slot = Integer.parseInt(args[1]);

		kit.setSlot(slot);
		player.sendMessage(CC.RED + "Slot changed to " + kit.getName() + " successfully to " + slot);
	}
}
