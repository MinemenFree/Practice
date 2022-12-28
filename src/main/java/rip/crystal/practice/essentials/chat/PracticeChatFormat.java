package rip.crystal.practice.essentials.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import rip.crystal.practice.Practice;
import rip.crystal.practice.essentials.chat.impl.ChatFormat;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;

public class PracticeChatFormat implements ChatFormat {
    @Override
    public String format(Player sender, Player receiver, String message) {
        Profile senderProfile = Profile.get(sender.getUniqueId());
        if (Clan.getByPlayer(sender) != null) {
            if (Practice.get().isPlaceholderAPI())
                return PlaceholderAPI.setPlaceholders(sender, CC.translate(Practice.get().getMainConfig().getString("CHAT.CLAN_FORMAT")
                        .replace("{prefix}", Practice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                        .replace("{suffix}", Practice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                        .replace("{color}", senderProfile.getColor())
                        .replace("{player}", sender.getName())
                        .replace("{message}", (sender.hasPermission("practice.chat.color") ? CC.translate(message) : CC.strip(message)))
                        .replace("{clan}", Clan.getByPlayer(sender).getColoredName())));
            return CC.translate(Practice.get().getMainConfig().getString("CHAT.CLAN_FORMAT")
                    .replace("{prefix}", Practice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                    .replace("{suffix}", Practice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                    .replace("{color}", senderProfile.getColor())
                    .replace("{player}", sender.getName())
                    .replace("{message}", (sender.hasPermission("practice.chat.color") ? CC.translate(message) : CC.strip(message)))
                    .replace("{clan}", Clan.getByPlayer(sender).getColoredName()));
        }
        if (cPractice.get().isPlaceholderAPI())
            return PlaceholderAPI.setPlaceholders(sender, CC.translate(Practice.get().getMainConfig().getString("CHAT.DEFAULT_FORMAT")
                    .replace("{prefix}", Practice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                    .replace("{suffix}", Practice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                    .replace("{color}", senderProfile.getColor())
                    .replace("{player}", sender.getName())
                    .replace("{message}", (sender.hasPermission("practice.chat.color") ? CC.translate(message) : CC.strip(message)))));
        return CC.translate(Practice.get().getMainConfig().getString("CHAT.DEFAULT_FORMAT")
                .replace("{prefix}", Practice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                .replace("{suffix}", Practice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                .replace("{color}", senderProfile.getColor())
                .replace("{player}", sender.getName())
                .replace("{message}", (sender.hasPermission("practice.chat.color") ? CC.translate(message) : CC.strip(message))));
    }
}
