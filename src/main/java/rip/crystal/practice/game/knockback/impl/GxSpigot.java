package rip.crystal.practice.game.knockback.impl;

import club.gexin.gxspigot.knockback.KnockbackProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.knockback.KnockbackProfiler;

import java.lang.reflect.Field;

public class GxSpigot implements KnockbackProfiler {

    @Override
    public void setKnockback(Player player, String kb) {
        KnockbackProfile knockbackProfile = club.gexin.gxspigot.GxSpigot.INSTANCE.getConfig().getKbProfileByName(kb);
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
