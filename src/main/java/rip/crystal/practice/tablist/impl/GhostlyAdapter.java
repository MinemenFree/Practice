package rip.crystal.practice.tablist.impl;

import rip.crystal.practice.tablist.impl.utils.BufferedTabObject;
import org.bukkit.entity.Player;

import java.util.Set;

public interface GhostlyAdapter {

    Set<BufferedTabObject> getSlots(Player player);

    String getFooter();
    String getHeader();

}
