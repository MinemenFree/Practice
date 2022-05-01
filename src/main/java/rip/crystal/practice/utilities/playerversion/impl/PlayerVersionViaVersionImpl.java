package rip.crystal.practice.utilities.playerversion.impl;

import org.bukkit.entity.Player;
import rip.crystal.practice.utilities.playerversion.IPlayerVersion;
import rip.crystal.practice.utilities.playerversion.PlayerVersion;
import us.myles.ViaVersion.api.Via;

public class PlayerVersionViaVersionImpl implements IPlayerVersion {
    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(Via.getAPI().getPlayerVersion(player.getUniqueId()));
    }
}
