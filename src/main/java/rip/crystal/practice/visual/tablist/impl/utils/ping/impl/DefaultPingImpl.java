package rip.crystal.practice.visual.tablist.impl.utils.ping.impl;

import rip.crystal.practice.visual.tablist.impl.utils.ping.IPingProvider;
import org.bukkit.entity.Player;

public class DefaultPingImpl implements IPingProvider {

    @Override
    public int getDefaultPing(Player player) {
        return 0;
    }

}
