package rip.crystal.practice.api.rank;

import net.audidevelopment.core.api.player.GlobalPlayer;
import net.audidevelopment.core.api.player.PlayerData;
import net.audidevelopment.core.plugin.cCoreAPI;
import me.activated.core.api.player.GlobalPlayer;
import me.activated.core.api.player.PlayerData;
import me.activated.core.plugin.AquaCoreAPI;
import java.util.UUID;

public interface Rank {

    String getName(UUID uuid);
    String getPrefix(UUID uuid);
    String getSuffix(UUID uuid);
    Tag getTag(UUID uuid);
    String getColor(UUID uuid);
    int getWeight(UUID uuid);
}
