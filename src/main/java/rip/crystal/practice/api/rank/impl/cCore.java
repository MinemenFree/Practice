package rip.crystal.practice.api.rank.impl;

import net.audidevelopment.core.api.player.GlobalPlayer;
import net.audidevelopment.core.api.player.PlayerData;
import net.audidevelopment.core.plugin.cCoreAPI;
import rip.crystal.practice.api.rank.Rank;

import java.util.UUID;

public class cCore implements Rank {

    @Override
    public String getName(UUID uuid) {
        PlayerData data = cCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getName();
    }

    @Override
    public String getPrefix(UUID uuid) {
        PlayerData data = cCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getPrefix();
    }

    @Override
    public String getSuffix(UUID uuid) {
        PlayerData data = cCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getSuffix();
    }

    @Override
    public String getColor(UUID uuid) {
        PlayerData data = cCoreAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getColor() + data.getHighestRank().getName();
    }

    @Override
    public int getWeight(UUID uuid) {
        GlobalPlayer globalPlayer = cCoreAPI.INSTANCE.getGlobalPlayer(uuid);
        return globalPlayer == null ? 0 : globalPlayer.getRankWeight();
    }
}
