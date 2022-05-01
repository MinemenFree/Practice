package rip.crystal.practice.game.knockback.impl;

import net.ghoulpvp.knockback.KnockbackManager;
import net.ghoulpvp.knockback.KnockbackProfile;
import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.knockback.KnockbackProfiler;

import java.lang.reflect.Field;

public class gSpigot implements KnockbackProfiler {

    @Override
    public void setKnockback(Player player, String kb) {
        KnockbackProfile knockbackProfile = KnockbackManager.getKnockbackManager().getProfile(kb);
        EntityHuman entityPlayer =  ((CraftPlayer) player).getHandle();
        Class entityclass = entityPlayer.getClass().getSuperclass();

        try {
            Field filed = entityclass.getDeclaredField("knockbackProfile");
            filed.setAccessible(true);
            filed.set(entityPlayer, knockbackProfile);
            filed.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
