package rip.crystal.practice.visual.tablist.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TabListListeners implements Listener {

    private final TabList instance;

    public TabListListeners(TabList instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        instance.getTablists().put(player.getUniqueId(), new GhostlyTablist(player, instance));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        instance.getTablists().remove(player.getUniqueId());
    }

}
