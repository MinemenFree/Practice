package rip.crystal.practice.visual.tablist.impl.utils.ping.impl;

import org.bukkit.entity.Player;
import rip.crystal.practice.visual.tablist.impl.utils.ping.IPingProvider;

public class DefaultPingImpl implements IPingProvider {

    @Override
    public int getDefaultPing(Player player) {
        return 0;
    }

}
