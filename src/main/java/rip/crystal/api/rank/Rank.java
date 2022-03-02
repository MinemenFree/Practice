package rip.crystal.api.rank;

import java.util.UUID;

public interface Rank {

    String getName(UUID uuid);
    String getPrefix(UUID uuid);
    String getSuffix(UUID uuid);
    String getColor(UUID uuid);
}
