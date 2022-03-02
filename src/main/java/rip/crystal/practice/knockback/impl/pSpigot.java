package rip.crystal.practice.knockback.impl;

import rip.crystal.practice.knockback.KnockbackProfiler;
import me.scalebound.pspigot.KnockbackProfile;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

public class pSpigot implements KnockbackProfiler {

    @Override
    public void setKnockback(Player player, String kb) {
        final KnockbackProfile craftKnockbackProfile = SpigotConfig.getKbProfileByName(kb);
        if (craftKnockbackProfile != null) {
            player.setKbProfile(craftKnockbackProfile);
        }
    }
}
