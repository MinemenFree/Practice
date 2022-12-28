package rip.crystal.practice.api.rank;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import rip.crystal.practice.api.rank.impl.*;

@Getter @Setter
public class RankManager {

    @Getter
    private static RankManager instance;
    private Plugin plugin;
    private String rankSystem;
    private Rank rank;
    private Chat chat;

    public RankManager(Plugin plugin) {
        instance = this;
        this.plugin = plugin;
    }

    public void loadRank() {

        if (Bukkit.getPluginManager().getPlugin("AquaCore") != null) {
            this.setRank(new AquaCore());
            this.setRankSystem("AquaCore");
        }
        else if (Bukkit.getPluginManager().getPlugin("cCore") != null) {
            this.setRank(new cCore());
            this.setRankSystem("cCore");
        }
        else if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            this.setRank(new LuckPerms());
            this.setRankSystem("LuckPerms");
        }
        else {
            this.setRank(new Default());
            this.setRankSystem("Default");
        }
    }
}
