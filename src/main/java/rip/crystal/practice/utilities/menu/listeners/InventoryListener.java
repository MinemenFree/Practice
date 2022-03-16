package rip.crystal.practice.utilities.menu.listeners;
/* 
   Made by cpractice Development Team
   Created on 12.11.2021
*/

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        /*final Player player = (Player)event.getWhoClicked();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            Profile profile = Profile.get(player.getUniqueId());
            if (profile.getState() == ProfileState.LOBBY) {
                event.setCancelled(true);
            }
        }*/
    }
}

