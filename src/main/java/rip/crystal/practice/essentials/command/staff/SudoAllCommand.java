package rip.crystal.practice.essentials.command.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

public class SudoAllCommand extends BaseCommand {

	@Command(name = "sudoall", permission = "cpractice.command.sudoall")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage /sudoall (message)");
			return;
		}

		boolean noMe = false;
		StringBuilder message = new StringBuilder();

		if (args[args.length - 1].equals("-s")) {
			args[args.length - 1] = null;
			noMe = true;
		}
		for (String arg : args) {
			message.append(arg).append(" ");
		}

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (noMe && onlinePlayer.equals(player)) continue;
			onlinePlayer.chat(CC.translate(message.toString()));
		}

		player.sendMessage(ChatColor.GREEN + "Forced all players to chat!");
	}
}
