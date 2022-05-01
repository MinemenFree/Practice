package rip.crystal.practice.essentials.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.chat.impl.ChatFormat;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;

public class cPracticeChatFormat implements ChatFormat {
    @Override
    public String format(Player sender, Player receiver, String message) {
        Profile senderProfile = Profile.get(sender.getUniqueId());
        if (Clan.getByPlayer(sender) != null) {
            if (cPractice.get().isPlaceholderAPI())
                return PlaceholderAPI.setPlaceholders(sender, CC.translate(cPractice.get().getMainConfig().getString("CHAT.CLAN_FORMAT")
                        .replace("{prefix}", cPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                        .replace("{suffix}", cPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                        .replace("{color}", senderProfile.getColor())
                        .replace("{player}", sender.getName())
                        .replace("{message}", (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message)))
                        .replace("{clan}", Clan.getByPlayer(sender).getColoredName())));
            return CC.translate(cPractice.get().getMainConfig().getString("CHAT.CLAN_FORMAT")
                    .replace("{prefix}", cPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                    .replace("{suffix}", cPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                    .replace("{color}", senderProfile.getColor())
                    .replace("{player}", sender.getName())
                    .replace("{message}", (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message)))
                    .replace("{clan}", Clan.getByPlayer(sender).getColoredName()));
        }
        if (cPractice.get().isPlaceholderAPI())
            return PlaceholderAPI.setPlaceholders(sender, CC.translate(cPractice.get().getMainConfig().getString("CHAT.DEFAULT_FORMAT")
                    .replace("{prefix}", cPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                    .replace("{suffix}", cPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                    .replace("{color}", senderProfile.getColor())
                    .replace("{player}", sender.getName())
                    .replace("{message}", (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message)))));
        return CC.translate(cPractice.get().getMainConfig().getString("CHAT.DEFAULT_FORMAT")
                .replace("{prefix}", cPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                .replace("{suffix}", cPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                .replace("{color}", senderProfile.getColor())
                .replace("{player}", sender.getName())
                .replace("{message}", (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message))));
    }
}