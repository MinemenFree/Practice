package rip.crystal.practice.match.task;

import rip.crystal.practice.match.Match;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class WaterCheckTask implements Runnable {

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() < 1) return;

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (Profile.get(player.getUniqueId()).getState() == ProfileState.FIGHTING) {
                Match match = Profile.get(player.getUniqueId()).getMatch();
                if (match.getKit().getGameRules().isSumo() || match.getKit().getGameRules().isSpleef()) {
                    if (player.getLocation().getBlock().getType() == Material.WATER
                            || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                        match.onDeath(player);
                    }
                }
            }
        });
    }
}
