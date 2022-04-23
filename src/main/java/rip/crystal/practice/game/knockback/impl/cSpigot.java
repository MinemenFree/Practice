package rip.crystal.practice.game.knockback.impl;
import net.audidevelopment.cspigot.knockback.Knockback;
import net.audidevelopment.cspigot.knockback.KnockbackModule;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.knockback.KnockbackProfiler;

import java.lang.reflect.Field;

public class cSpigot implements KnockbackProfiler {

    @Override
    public void setKnockback(Player player, String kb) {
        Knockback knockbackProfile = KnockbackModule.INSTANCE.getKbProfileByName(kb);
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Class aClass = entityPlayer.getClass().getSuperclass().getSuperclass();

        try {
            Field filed = aClass.getDeclaredField("knockbackProfile");
            filed.setAccessible(true);
            filed.set(entityPlayer, knockbackProfile);
            filed.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

