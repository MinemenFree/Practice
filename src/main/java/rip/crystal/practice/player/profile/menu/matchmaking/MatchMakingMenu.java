package rip.crystal.practice.player.profile.menu.matchmaking;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchMakingMenu extends Menu {
    public final List<Integer> getPingRanges = Arrays.asList(5, 10, 25, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, -1);
    public final List<Integer> getEloRanges = Arrays.asList(25, 50, 75, 100, 125, 150, 175, 200, 225, 250, -1);

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_GRAY + "Matchmaking Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> hashMap = new HashMap<>();
        hashMap.put(4, new PingRangeButton("&bPing Range", Material.REDSTONE_COMPARATOR, 0, Arrays.asList(CC.CHAT_BAR, "&7Change the maximum Ping difference", "&7between you and the other players.", "", "&8 ● &7Left-Click to increase", "&8 ● &7Right-Click to decrease", "&8 ● &7Shift-Click to reset", "")));
        return hashMap;
    }

    public int handleRangeClick(ClickType clickType, List<Integer> list, int n) {
        int n2 = list.get(0);
        int n3 = list.get(list.size() - 1);
        if (clickType == ClickType.LEFT) {
            n = n == n3 ? n2 : list.get(list.indexOf(n) + 1);
        } else if (clickType == ClickType.RIGHT) {
            n = n == n2 ? n3 : list.get(list.indexOf(n) - 1);
        } else if (clickType == ClickType.SHIFT_LEFT) {
            n = n3;
        }
        return n;
    }

    public int parseOrDefault(String string, int n) {
        try {
            return Integer.parseInt(string.replace(" ", ""));
        }
        catch (NumberFormatException numberFormatException) {
            return n;
        }
    }

}
