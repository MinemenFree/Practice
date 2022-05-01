package rip.crystal.practice.player.party.classes.bard;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.party.classes.HCFClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BardEnergyTask extends BukkitRunnable {
    @Getter
    private static final Map<String, Float> energy = new ConcurrentHashMap<>();
    public static final float MAX_ENERGY = 100;
    public static final float ENERGY_REGEN_PER_SECOND = 1;

    @Override
    public void run() {
        for (Player player : cPractice.get().getServer().getOnlinePlayers()) {
            if (!HCFClass.BARD.isApply(player)) {
                continue;
            }

            if (energy.containsKey(player.getName())) {
                if (energy.get(player.getName()) == MAX_ENERGY) {
                    continue;
                }

                energy.put(player.getName(), Math.min(MAX_ENERGY, energy.get(player.getName()) + ENERGY_REGEN_PER_SECOND));
            } else {
                energy.put(player.getName(), 0F);
            }

            int manaInt = energy.get(player.getName()).intValue();

            if (manaInt % 10 == 0) {
                player.sendMessage(ChatColor.AQUA + "Bard Energy: " + ChatColor.GREEN + manaInt);
            }
        }
    }
}
