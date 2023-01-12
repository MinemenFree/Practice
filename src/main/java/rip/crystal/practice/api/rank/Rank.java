package rip.crystal.practice.api.rank;

import java.util.UUID;

public interface Rank {

    String getName(UUID uuid);
    String getPrefix(UUID uuid);
    String getSuffix(UUID uuid);
    Value getTag(UUID uuid);
    String getColor(UUID uuid);
    int getWeight(UUID uuid);
}
