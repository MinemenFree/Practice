package rip.crystal.practice.api.rank.impl;

import rip.crystal.practice.api.rank.Rank;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Default implements Rank {

    @Override
    public String getName(UUID uuid) {
        return "Default";
    }

    @Override
    public String getPrefix(UUID uuid) {
        return "Default";
    }

    @Override
    public String getSuffix(UUID uuid) {
        return "Default";
    }

    @Override
    public String getColor(UUID uuid) {
        return "Default";
    }

    @Override
    public String getRealName(Player player) {
        return "Default";
    }

    @Override
    public String getTag(Player player) {
        return "Default";
    }

/*  @Override
    public boolean isRankTemporary(UUID uuid) {
        return false;
    }
*/
    @Override
    public int getWeight(UUID uuid) {
        return 0;
    }
}
