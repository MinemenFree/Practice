package rip.crystal.practice.api.utilities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.UUID;

@UtilityClass
public class CooldownUtil {

    private final Table<String, UUID, Long> cooldown = HashBasedTable.create();

    public boolean hasCooldown(String name, Player player) {
        return cooldown.contains(name, player.getUniqueId()) && cooldown.get(name, player.getUniqueId()) > System.currentTimeMillis();
    }

    public void setCooldown(String name, Player player, long time) {
        if (time == 0L) {
            cooldown.remove(name, player.getUniqueId());
        }
        if (time <= 0L) {
            cooldown.remove(name, player.getUniqueId());
        }
        else {
            cooldown.put(name, player.getUniqueId(), System.currentTimeMillis() + time);
        }
    }

    public String getCooldown(String name, Player player) {
        long cooldownLeft = cooldown.get(name, player.getUniqueId()) - System.currentTimeMillis();
        return JavaUtil.formatLongMin(cooldownLeft);
    }
}
