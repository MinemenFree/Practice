package rip.crystal.practice.api.rank;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface Rank {

    String getName(UUID uuid);
    String getPrefix(UUID uuid);
    String getSuffix(UUID uuid);
    String getColor(UUID uuid);
    String getTag(Player player);
    String getRealName(Player player);
/*  Boolean isRankTemporary(UUID uuid);*/
    int getWeight(UUID uuid);
}
