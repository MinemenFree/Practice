package rip.crystal.practice.utilities.playerversion.impl;

import org.bukkit.entity.Player;
import rip.crystal.practice.utilities.playerversion.IPlayerVersion;
import rip.crystal.practice.utilities.playerversion.PlayerVersion;

public class PlayerVersionDefaultImpl implements IPlayerVersion {
    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(0);
    }
}
