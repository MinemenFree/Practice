package rip.crystal.practice.api.rank;

import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.chat.Chat;
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
        else if (Bukkit.getPluginManager().getPlugin("HestiaCore") != null) {
            this.setRank(new HestiaCore());
            this.setRankSystem("HestiaCore");
        }
        else if (Bukkit.getPluginManager().getPlugin("mCore") != null) {
            this.setRank(new mCore());
            this.setRankSystem("mCore");
        }
        else if (Bukkit.getPluginManager().getPlugin("Synth") != null) {
            this.setRank(new Synth());
            this.setRankSystem("Synth");
        }
        else if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            this.setRank(new LuckPerms());
            this.setRankSystem("LuckPerms");
        }
        else if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            this.loadVault();

            if (this.getChat() == null) {
                this.setRank(new Default());
                this.setRankSystem("Default");
                return;
            }

            if (this.getChat().getName().contains("PermissionsEx")) {
                this.setRank(new PermissionsEx());
                this.setRankSystem("PermissionsEx");
            }
        }
        else {
            this.setRank(new Default());
            this.setRankSystem("Default");
        }
    }

    private void loadVault() {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) chat = rsp.getProvider();
    }
}
