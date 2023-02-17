package rip.crystal.practice.player.queue;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.hotbar.impl.HotbarItem;
import rip.crystal.practice.player.queue.menus.QueuesMenu;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

public class QueueListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (profile.getState() == ProfileState.QUEUEING) {
			profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		BasicConfigurationFile config = cPractice.get().getMainConfig();
		
		if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			HotbarItem hotbarItem = Hotbar.fromItemStack(event.getItem());

			if (hotbarItem != null) {
				Profile profile = Profile.get(event.getPlayer().getUniqueId());
				boolean cancelled = true;

				if (hotbarItem == HotbarItem.QUEUE_JOIN_RANKED) {
					if (!profile.isBusy() && !config.getBoolean("QUEUES.ENABLED")) {
					    Queue.getRankedMenu().openMenu(event.getPlayer());
					}
				} else if (hotbarItem == HotbarItem.QUEUES_JOIN && config.getBoolean("QUEUES.ENABLED")) {
                    new QueuesMenu().openMenu(event.getPlayer());
                } else if (hotbarItem == HotbarItem.QUEUE_JOIN_UNRANKED && !config.getBoolean("QUEUES.ENABLED")) {
					if (!profile.isBusy()) {
                        Queue.getUnRankedMenu().openMenu(event.getPlayer());
					}
				} else if (hotbarItem == HotbarItem.QUEUE_LEAVE) {
					if (profile.getState() == ProfileState.QUEUEING) {
						profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
					}
				} else {
					cancelled = false;
				}

				event.setCancelled(cancelled);
			}
		}
	}

}
