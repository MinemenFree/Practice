package rip.crystal.practice.visual.tablist.impl;

import rip.crystal.practice.visual.tablist.impl.utils.BufferedTabObject;
import org.bukkit.entity.Player;

import java.util.Set;

public interface GhostlyAdapter {

    Set<BufferedTabObject> getSlots(Player player);

    String getFooter();
    String getHeader();

}
