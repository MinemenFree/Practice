package rip.crystal.practice.chat.impl.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.chat.impl.Chat;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteChatCommand extends BaseCommand {

	@Command(name = "mutechat", permission = "hysteria.staff.mutechat", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();

		Chat.togglePublicChatMute();

		String senderName;

		if (sender instanceof Player) {
			senderName = Profile.get(((Player) sender).getUniqueId()).getColor() + sender.getName();
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		String context = Chat.isPublicChatMuted() ? "muted" : "unmuted";

		Bukkit.getOnlinePlayers().forEach(online -> {
			Profile profile = Profile.get(online.getUniqueId());
			new MessageFormat(Locale.MUTE_CHAT_BROADCAST.format(profile.getLocale()))
					.add("{sender_name}", senderName)
					.add("{context}", context)
					.send(online);
		});
	}
}
