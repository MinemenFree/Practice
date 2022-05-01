package rip.crystal.practice.essentials.chat.impl.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

public class ClearChatCommand extends BaseCommand {

	@Command(name = "clearchat", aliases = {"cc"}, permission = "cpractice.staff.clearchat", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();

		String[] strings = new String[101];

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!player.hasPermission("cpractice.staff")) {
				player.sendMessage(strings);
			}
		}

		String senderName;

		if (sender instanceof Player) {
			Profile profile = Profile.get(((Player) sender).getUniqueId());
			senderName = profile.getColor() + sender.getName();
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		Bukkit.getOnlinePlayers().forEach(online -> {
			Profile profile = Profile.get(online.getUniqueId());
			new MessageFormat(Locale.CLEAR_CHAT_BROADCAST.format(profile.getLocale()))
					.add("{sender_name}", senderName)
					.send(online);
		});
	}
}
