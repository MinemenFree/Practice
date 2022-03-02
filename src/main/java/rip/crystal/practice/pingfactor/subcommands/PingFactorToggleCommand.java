package rip.crystal.practice.pingfactor.subcommands;
/* 
   Made by Hysteria Development Team
   Created on 13.11.2021
*/

import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class PingFactorToggleCommand extends BaseCommand {

    @Command(name = "pingfactor.toggle", aliases = "pf.toggle")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        //String[] args = commandArgs.getArgs();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().isUsingPingFactor(!profile.getOptions().isUsingPingFactor());

        if (profile.getOptions().isUsingPingFactor()) {
            player.sendMessage(CC.CHAT_BAR);
            profile.setPingRange(50);
            player.sendMessage(CC.translate("&aYou have enabled ping factor."));
            player.sendMessage(CC.CHAT_BAR);
        } else {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&cYou have disabled ping factor."));
            player.sendMessage(CC.CHAT_BAR);
        }
    }
}
