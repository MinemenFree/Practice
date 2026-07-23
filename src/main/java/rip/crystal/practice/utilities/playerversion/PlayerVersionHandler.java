package rip.crystal.practice.utilities.playerversion;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import rip.crystal.practice.utilities.playerversion.impl.PlayerVersionDefaultImpl;
import rip.crystal.practice.utilities.playerversion.impl.PlayerVersionProtocolLibImpl;
import rip.crystal.practice.utilities.playerversion.impl.PlayerVersionViaVersionImpl;

public class PlayerVersionHandler {

    public static IPlayerVersion version = new PlayerVersionDefaultImpl();

    public static void init() {
        /* Plugin Manager */
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        /* Detect plugin */
        if (pluginManager.getPlugin("ViaVersion") != null) {
            PlayerVersionViaVersionImpl via = new PlayerVersionViaVersionImpl();

            // ViaVersion is installed but its API package is unrecognised - fall through
            // to ProtocolLib rather than returning a wrong version for every player.
            if (via.isAvailable()) {
                version = via;
                return;
            }
        }

        if (pluginManager.getPlugin("ProtocolLib") != null) {
            version = new PlayerVersionProtocolLibImpl();
        }
    }
}
