package rip.crystal.practice.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    public static String formatLongMin(final long time) {
        final long totalSecs = time / 1000L;
        return String.format("%02d:%02d", totalSecs / 60L, totalSecs % 60L);
    }
}
