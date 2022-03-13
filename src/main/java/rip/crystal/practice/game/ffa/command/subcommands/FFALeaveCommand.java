package rip.crystal.practice.game.ffa.command.subcommands;
/* 
   Made by Hysteria Development Team
   Created on 27.11.2021
*/

import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.knockback.Knockback;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import me.scalebound.pspigot.KnockbackProfile;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

public class FFALeaveCommand extends BaseCommand {

    @Command(name="ffa.leave")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) {
            player.sendMessage(CC.translate("&cYou can only use this command in FFA Arena."));
            return;
        }

        for (String message : cPractice.get().getFfaConfig().getStringList("MESSAGES.PLAYER-LEAVE-GLOBAL")) {
            this.broadcastMessage(CC.translate(message.replace("{player}", player.getName())));
        }

        if(cPractice.get().getServer().getName().equalsIgnoreCase("pSpigot")) {
            KnockbackProfile knockbackProfile = SpigotConfig.getKbProfileByName("default");
            player.getPlayer().setKbProfile(knockbackProfile);
        } else {
            Knockback.getKnockbackProfiler().setKnockback(player.getPlayer(), "default");
        }

        PlayerUtil.reset(player);
        profile.setState(ProfileState.LOBBY);
        Hotbar.giveHotbarItems(player);
        cPractice.get().getEssentials().teleportToSpawn(player);
    }

    private void broadcastMessage(String message) {
        for (Profile ffaPlayer : cPractice.get().getFfaManager().getFFAPlayers()) {
            ffaPlayer.msg(message);
        }
    }
}
