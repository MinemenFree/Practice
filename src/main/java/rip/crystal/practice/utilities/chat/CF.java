package rip.crystal.practice.utilities.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.profile.conversation.command.Configurator;

import java.util.*;

public class CF {

    public static Configurator c;

    private static final Map<String, ChatFormat> MAP;

    public static final String BOLD;
    public static final String ITALIC;
    public static final String UNDER_LINE;
    public static final String STRIKE_THROUGH;
    public static final String RESET;
    public static final String MAGIC;

    static {
        MAP = new HashMap<>();

        for (ChatFormat chatFormat : ChatFormat.values()) {
            MAP.put(chatFormat.name().toLowerCase().replace("_", ""), chatFormat);
        }

        BOLD = ChatFormat.BOLD.toString();
        ITALIC = ChatFormat.ITALIC.toString();
        UNDER_LINE = ChatFormat.UNDERLINE.toString();
        STRIKE_THROUGH = ChatFormat.STRIKETHROUGH.toString();
        RESET = ChatFormat.RESET.toString();
        MAGIC = ChatFormat.MAGIC.toString();
    }

    public static Set<String> getFormatNames() {
        return MAP.keySet();
    }

    public static ChatFormat getFormatFromName(String name) {
        if (MAP.containsKey(name.trim().toLowerCase())) {
            return MAP.get(name.trim().toLowerCase());
        }

        ChatFormat formatting;

        try {
            color = ChatFormat.valueOf(name.toUpperCase().replace(" ", "_"));
        } catch (Exception e) {
            return null;
        }

        return color;
    }

    public static String translate(String in) {
        return ChatFormat.translateAlternateFormatCodes('&', in);
    }

    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            toReturn.add(ChatFormat.translateAlternateFormatCodes('&', line));
        }

        return toReturn;
    }

    public static String[] translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatFormat.translateAlternateFormatCodes('&', line));
            }
        }

        return toReturn.toArray(new String[toReturn.size()]);
    }

    public static void sender(CommandSender sender, String in) {
        sender.sendMessage(translate(in));
    }

    public static void message(Player player, String in) {
        player.sendMessage(translate(in));
    }

    public static String strip(String message) {
        return ChatFormat.stripFormat(message);
    }
}
