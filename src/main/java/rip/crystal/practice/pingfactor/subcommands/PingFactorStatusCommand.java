package rip.crystal.practice.pingfactor.subcommands;
/*
   Made by Hysteria Development Team
   Created on 10.10.2021
*/

import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class PingFactorStatusCommand extends BaseCommand {

    @Command(name = "pingfactor.status", aliases = "pf.status")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        //String[] args = commandArgs.getArgs();
        Profile profile = Profile.get(player.getUniqueId());

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&7(*) &9&lPing Range &7(*)"));
        player.sendMessage(CC.translate("&9Profile: &f" + player.getName()));
        player.sendMessage(CC.translate("&9Value: &f" + profile.getPingRange()));
        player.sendMessage(CC.CHAT_BAR);

        /*Player target = Bukkit.getPlayer(args[0]);

        if(target == null) {
            player.sendMessage(CC.translate("&9Player is not online."));
            return;
        }

        Profile targetProfile = Profile.get(target.getUniqueId());
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&7(*) &9&lPing Range &7(*)"));
        player.sendMessage(CC.translate("&cProfile: &f" + target.getName()));
        player.sendMessage(CC.translate("&cValue: &f" + targetProfile.getPingRange()));
        player.sendMessage(CC.CHAT_BAR);*/
    }
}
