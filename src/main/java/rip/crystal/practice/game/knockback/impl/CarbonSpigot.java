package rip.crystal.practice.game.knockback.impl;

import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.knockback.KnockbackProfiler;
import xyz.refinedev.spigot.knockback.IKnockback;
import xyz.refinedev.spigot.knockback.KnockbackAPI;

import java.lang.reflect.Field;

public class CarbonSpigot implements KnockbackProfiler {

    @Override
    public void setKnockback(Player player, String kb) {
        IKnockback<?, ?, ?, ?> knockbackProfile = KnockbackAPI.getByName(kb);
        EntityHuman entityPlayer =  ((CraftPlayer) player).getHandle();
        Class entityclass = entityPlayer.getClass().getSuperclass();

        try {
            Field filed = entityclass.getDeclaredField("knockback");
            filed.setAccessible(true);
            filed.set(entityPlayer, knockbackProfile);
            filed.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
