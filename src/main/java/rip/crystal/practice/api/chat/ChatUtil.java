package rip.crystal.practice.api.chat;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ChatUtil {

    public String LONG_LINE = "&7&m----------------------------------------";
    public String NORMAL_LINE = "&7&m-----------------------------";
    public String SHORT_LINE = "&7&m---------------";

    public String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public List<String> translate(List<String> in) {
        return in.stream().map(ChatUtil::translate).collect(Collectors.toList());
    }

    public String strip(String in) {
        return ChatColor.stripColor(in);
    }

    public void sender(CommandSender sender, String in) {
        sender.sendMessage(translate(in));
    }

    public void message(Player player, String in) {
        player.sendMessage(translate(in));
    }

    public void broadcast(String in) {
        Bukkit.broadcastMessage(translate(in));
    }

    public void log(String in) {
        Bukkit.getConsoleSender().sendMessage(translate(in));
    }

    public String capitalize(String str) {
        return WordUtils.capitalize(str);
    }

    public String toReadable(Enum<?> enu) {
        return WordUtils.capitalize(enu.name().replace("_", " ").toLowerCase());
    }
}
