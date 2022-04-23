package rip.crystal.practice.player.profile.follow;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.hotbar.impl.HotbarItem;
import rip.crystal.practice.utilities.chat.CC;

import java.util.Map;
import java.util.UUID;

public class Follow {
    public static Map<UUID, Follow> follows;
    private final UUID follower;
    private final UUID followed;
    private final Player followedPlayer;

    public static Follow getByFollowed(UUID uUID) {
        return follows.values().stream().filter(follow -> follow.getFollowed().equals(uUID)).findFirst().orElse(null);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.follower);
    }

    public void follow() {
        Player player = this.getPlayer();
        Profile profile = Profile.get(this.followed);
        this.detect();
        player.getInventory().clear();
        player.getInventory().setItem(Hotbar.getSlot(HotbarItem.FOLLOW), Hotbar.getItem(HotbarItem.FOLLOW));
        player.updateInventory();
    }

    public void detect() {
        Player player = this.getPlayer();
        Profile profile = Profile.get(this.followed);
        if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
            player.showPlayer(this.followedPlayer);
            player.teleport(this.followedPlayer.getLocation());
        } else if (profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.SPECTATING) {
            profile.getMatch().addSpectator(player, this.followedPlayer);
        } else {
            player.sendMessage(CC.translate("&cCan't follow."));
        }
    }

    //public List<String> getScoreboardLines() {
        //BasicConfigurationFile basicConfigurationFile = cPractice.get().getScoreboardConfig();
        //List<String> list = basicConfigurationFile.getStringList("sbpath"[1]);
        //list.replaceAll(string -> string.replace("sbpath"[2], Profile.get(this.followed).getColor()).replace("sbpath"[3], this.followedPlayer.getName()));
        //return list;
    //}

    public UUID getFollower() {
        return this.follower;
    }

    public UUID getFollowed() {
        return this.followed;
    }

    public Player getFollowedPlayer() {
        return this.followedPlayer;
    }

    public Follow(UUID uUID, UUID uUID2, Player player) {
        this.follower = uUID;
        this.followed = uUID2;
        this.followedPlayer = player;
    }

    public static Map<UUID, Follow> getFollows() {
        return follows;
    }
}
