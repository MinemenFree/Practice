package rip.crystal.practice.visual.tablist.impl;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import rip.crystal.practice.visual.tablist.impl.utils.IRubenHelper;
import rip.crystal.practice.visual.tablist.impl.utils.impl.ProtocolLibTabImpl;
import rip.crystal.practice.visual.tablist.impl.utils.ping.IPingProvider;
import rip.crystal.practice.visual.tablist.impl.utils.ping.impl.DefaultPingImpl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class TabList {

    private final JavaPlugin plugin;
    private final GhostlyAdapter adapter;
    private final Map<UUID, GhostlyTablist> tablists;
    private TabListThread thread;
    private IRubenHelper implementation;
    private TabListListeners listeners;

    //Tablist Ticks
    @Setter private long ticks = 20;
    @Setter private boolean hook = false;
    @Setter private IPingProvider pingProvider;

    public TabList(JavaPlugin plugin, GhostlyAdapter adapter) {
        if (plugin == null) {
            throw new RuntimeException("Ruben can not be instantiated without a plugin instance!");
        }

        this.plugin = plugin;
        this.adapter = adapter;
        this.tablists = new ConcurrentHashMap<>();

        this.registerImplementation();
        this.registerPingImplementation();

        this.setup();
    }

    private void registerImplementation() {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            this.implementation = new ProtocolLibTabImpl();
            plugin.getLogger().info("[RUBEN] Registered Implementation with ProtocolLib");
        } else {
            throw new IllegalArgumentException("if you use the cPractice tablist you need ProtocolLib");
        }

//        plugin.getLogger().info("[RUBEN] Unable to register Ruben with a proper implementation");
    }

    private void registerPingImplementation() {
        pingProvider = new DefaultPingImpl();
    }

    private void setup() {
        // Register Events
        this.plugin.getServer().getPluginManager().registerEvents(listeners = new TabListListeners(this), this.plugin);

        // Ensure that the thread has stopped running
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getTablists().containsKey(player.getUniqueId())) continue;

            getTablists().put(player.getUniqueId(), new GhostlyTablist(player, this));
        }

        // Start Thread
        this.thread = new TabListThread(this);
        this.thread.start();
    }

    /**
     *
     */
    public void disable() {
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }

        for (UUID uuid : getTablists().keySet()) {
            getTablists().get(uuid).cleanup();
        }
        getTablists().clear();

        this.implementation = null;
        this.pingProvider = null;
    }
}
