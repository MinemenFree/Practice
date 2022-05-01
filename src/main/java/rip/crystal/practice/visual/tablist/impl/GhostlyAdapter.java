package rip.crystal.practice.visual.tablist.impl;

import org.bukkit.entity.Player;
import rip.crystal.practice.visual.tablist.impl.utils.BufferedTabObject;

import java.util.Set;

public interface GhostlyAdapter {

    Set<BufferedTabObject> getSlots(Player player);

    String getFooter();
    String getHeader();

}
