package rip.crystal.practice.essentials.command.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class SudoCommand extends BaseCommand {

	@Command(name = "sudo", permission = "cpractice.command.sudo")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0 || args.length == 1) {
			player.sendMessage(CC.RED + "Please usage: /sudo (player) (message)");
			return;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			new MessageFormat(Locale.PLAYER_NOT_FOUND
					.format(Profile.get(player.getUniqueId()).getLocale()))
					.send(player);
			return;
		}

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 1; i < args.length; i++) {
			stringBuilder.append(args[i]).append(" ");
		}

		target.chat(CC.translate(stringBuilder.toString()));
		player.sendMessage(ChatColor.GREEN + "Forced target to chat!");
	}
}
