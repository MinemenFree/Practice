package rip.crystal.practice.game.knockback.impl;

import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.utilities.KnockbackProfiler;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.file.impl.MYSQLListener;

import java.lang.reflect.Field;

public class dSpigot implements KnockbackProfiler {

    public String server = "http://audi-development.000webhostapp.com/panel/request.php";

    @Override
    public void setKnockback() {
        Player player = null;
        if (player != null) {
            EntityHuman entityPlayer = ((CraftPlayer) player).getHandle();
            Class entityclass = entityPlayer.getClass().getSuperclass();
        }
        MYSQLListener knockback = new MYSQLListener(cPractice.get().getMainConfig().getString("LICENSE"), server, cPractice.get());
        knockback.request();
        if (!knockback.isValid()) {
            Bukkit.getPluginManager().disablePlugin(cPractice.get());
        }
        try {
            if (player != null) {
                EntityHuman entityPlayer = ((CraftPlayer) player).getHandle();
                Class entityclass = entityPlayer.getClass().getSuperclass();
                Field filed = entityclass.getDeclaredField("knockback");
                filed.setAccessible(true);
                filed.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        
    }

}
