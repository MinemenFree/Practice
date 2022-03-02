package rip.crystal.practice.kit.command;

import rip.crystal.practice.kit.Kit;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitCreateCommand extends BaseCommand {

	@Command(name = "kit.create", permission = "hysteria.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit create (name)");
			return;
		}

		String kitName = args[0];
		if (Kit.getByName(kitName) != null) {
			player.sendMessage(CC.RED + "A kit with that name already exists.");
			return;
		}

		Kit kit = new Kit(kitName);
		kit.save();
		Kit.getKits().add(kit);

		player.sendMessage(CC.GREEN + "You created a new kit.");
	}
}
