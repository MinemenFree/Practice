package rip.crystal.practice.game.knockback.impl;

import club.insaneprojects.insanepaper.KnockbackHandler;
import club.insaneprojects.insanepaper.knockback.profile.KnockbackProfile;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.knockback.KnockbackProfiler;

import java.lang.reflect.Field;

public class InsanePaperSpigot implements KnockbackProfiler {

    @Override
    public void setKnockback(Player player, String kb) {
        KnockbackProfile knockbackProfile = KnockbackHandler.INSTANCE.getProfileByName(kb);
        CraftPlayer entityPlayer = (CraftPlayer) player;
        Class entityclass = entityPlayer.getClass();

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
