package rip.crystal.practice.utilities.playerversion.impl;

import rip.crystal.practice.utilities.playerversion.IPlayerVersion;
import rip.crystal.practice.utilities.playerversion.PlayerVersion;
import org.bukkit.entity.Player;

public class PlayerVersionDefaultImpl implements IPlayerVersion {
    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(0);
    }
}
