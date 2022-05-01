package rip.crystal.practice.utilities.playerversion.impl;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;
import rip.crystal.practice.utilities.playerversion.IPlayerVersion;
import rip.crystal.practice.utilities.playerversion.PlayerVersion;

public class PlayerVersionProtocolLibImpl implements IPlayerVersion {

    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(
                ProtocolLibrary.getProtocolManager().getProtocolVersion(player)
        );
    }
}
