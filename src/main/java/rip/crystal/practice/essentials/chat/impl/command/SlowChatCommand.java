package rip.crystal.practice.essentials.chat.impl.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.essentials.chat.impl.Chat;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class SlowChatCommand extends BaseCommand {

	@Command(name = "slowchat", permission = "cpractice.staff.slowchat", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();
		String[] args = commandArgs.getArgs();

		if (args.length != 0) {
			int seconds;
			if (!StringUtils.isNumeric(args[0])) {
				sender.sendMessage(CC.RED + "Please insert a valid Integer.");
				return;
			}
			seconds = Integer.getInteger(args[0]);

			if (seconds < 0 || seconds > 60) {
				sender.sendMessage(ChatColor.RED + "A delay can only be between 1-60 seconds.");
				return;
			}

			String context = seconds == 1 ? "" : "s";

			sender.sendMessage(ChatColor.GREEN + "You have updated the chat delay to " + seconds + " second" + context + ".");
			Chat.setDelayTime(seconds);
			return;
		}

		Chat.togglePublicChatDelay();

		String senderName;

		if (sender instanceof Player) {
			senderName = Profile.get(((Player) sender).getUniqueId()) + sender.getName();
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		String context = Chat.getDelayTime() == 1 ? "" : "s";

		if (Chat.isPublicChatDelayed()) {
			Bukkit.getOnlinePlayers().forEach(online -> {
				Profile profile = Profile.get(online.getUniqueId());
				new MessageFormat(Locale.DELAY_CHAT_ENABLED_BROADCAST.format(profile.getLocale()))
						.add("{sender_name}", senderName)
						.add("{delay_time}", String.valueOf(Chat.getDelayTime()))
						.add("{context}", context)
						.send(online);
			});
		} else {
			Bukkit.getOnlinePlayers().forEach(online -> {
				Profile profile = Profile.get(online.getUniqueId());
				new MessageFormat(Locale.DELAY_CHAT_DISABLED_BROADCAST.format(profile.getLocale()))
						.add("{sender_name}", senderName)
						.send(online);
			});
		}
	}
}
