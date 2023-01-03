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

    private static final Map<String, ChatColor> MAP;

    public static final String BLUE;
    public static final String AQUA;
    public static final String YELLOW;
    public static final String RED;
    public static final String GRAY;
    public static final String GOLD;
    public static final String GREEN;
    public static final String WHITE;
    public static final String BLACK;
    public static final String BOLD;
    public static final String ITALIC;
    public static final String UNDER_LINE;
    public static final String STRIKE_THROUGH;
    public static final String RESET;
    public static final String MAGIC;
    public static final String DARK_BLUE;
    public static final String DARK_AQUA;
    public static final String DARK_GRAY;
    public static final String DARK_GREEN;
    public static final String DARK_PURPLE;
    public static final String DARK_RED;
    public static final String PINK;
    public static final String MENU_BAR;
    public static final String CHAT_BAR;
    public static final String SB_BAR;
    public static final String TAB_BAR;

    static {
        MAP = new HashMap<>();

        for (ChatFormat chatFormat : ChatFormat.values()) {
            MAP.put(chatFormat.name().toLowerCase().replace("_", ""), chatFormat);
        }

        BLUE = ChatColor.BLUE.toString();
        AQUA = ChatColor.AQUA.toString();
        YELLOW = ChatColor.YELLOW.toString();
        RED = ChatColor.RED.toString();
        GRAY = ChatColor.GRAY.toString();
        GOLD = ChatColor.GOLD.toString();
        GREEN = ChatColor.GREEN.toString();
        WHITE = ChatColor.WHITE.toString();
        BLACK = ChatColor.BLACK.toString();
        BOLD = ChatColor.BOLD.toString();
        ITALIC = ChatColor.ITALIC.toString();
        UNDER_LINE = ChatColor.UNDERLINE.toString();
        STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
        RESET = ChatColor.RESET.toString();
        MAGIC = ChatColor.MAGIC.toString();
        DARK_BLUE = ChatColor.DARK_BLUE.toString();
        DARK_AQUA = ChatColor.DARK_AQUA.toString();
        DARK_GRAY = ChatColor.DARK_GRAY.toString();
        DARK_GREEN = ChatColor.DARK_GREEN.toString();
        DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
        DARK_RED = ChatColor.DARK_RED.toString();
        PINK = ChatColor.LIGHT_PURPLE.toString();
        MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------";
        CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------------";
        SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------";
        TAB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------";
    }

    public static Set<String> getFormatNames() {
        return MAP.keySet();
    }

    public static ChatColor getFormatFromName(String name) {
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
