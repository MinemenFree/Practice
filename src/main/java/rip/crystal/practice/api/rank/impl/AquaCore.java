package rip.crystal.practice.api.rank.impl;

import me.activated.core.api.player.GlobalPlayer;
import me.activated.core.api.player.PlayerData;
import me.activated.core.plugin.AquaCoreAPI;
import rip.crystal.practice.api.rank.Rank;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AquaCore implements Rank {

    @Override
    public String getName(UUID uuid) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getName();
    }

    @Override
    public String getPrefix(UUID uuid) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getPrefix();
    }

    @Override
    public String getSuffix(UUID uuid) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getSuffix();
    }

    @Override
    public String getColor(UUID uuid) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getColor() + data.getHighestRank().getName();
    }
    @Override
    public String getRealName(Player player) {
        return AquaCoreAPI.INSTANCE.getRealName(player);
    }

    @Override
    public String getTag(Player player) {
        return AquaCoreAPI.INSTANCE.getTag(player.getUniqueId()).getFormat();
    }

/*  @Override
    public boolean isRankTemporary(UUID uuid) {
        PlayerData data = AquaCoreAPI.INSTANCE.getPlayerData(uuid);

        if (data == null) return false;

        for (Grant grant : data.getActiveGrants()) {
            if (data.getHighestRank().equals(grant.getRank())) {
                return !grant.isPermanent();
            }
        }

        return false;
    }
*/
    @Override
    public int getWeight(UUID uuid) {
        GlobalPlayer globalPlayer = AquaCoreAPI.INSTANCE.getGlobalPlayer(uuid);
        return globalPlayer == null ? 0 : globalPlayer.getRankWeight();
    }

}
