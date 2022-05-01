package rip.crystal.practice.match.task;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;

public class MatchLiquidTask implements Runnable {

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() < 1) return;

        Bukkit.getOnlinePlayers().forEach(player -> {
            Profile profile = Profile.get(player.getUniqueId());
            if (profile.getState() == ProfileState.FIGHTING) {
                Match match = profile.getMatch();
                if (match.getKit().getGameRules().isSumo() || match.getKit().getGameRules().isSpleef()) {
                    if (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER || player.getLocation().getBlock().getType() == Material.LAVA) {
                        match.onDeath(player);
                    }
                }
            }
        });
    }
}
