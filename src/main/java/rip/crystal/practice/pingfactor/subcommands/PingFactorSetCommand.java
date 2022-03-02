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
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PingFactorSetCommand extends BaseCommand {

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Command(name = "pf.set", aliases = "pingfactorset")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (Arrays.asList(0).contains(args.length)) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&cPlease use /pf set (value)"));
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        //int integer = Integer.getInteger(args[2]);

        Profile profile = Profile.get(player.getUniqueId());

        if (!StringUtils.isNumeric(args[0])) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&cPlease insert a valid value."));
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        profile.setPingRange(Integer.parseInt(args[0]));
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&cPing Range has been set to &f" + Integer.parseInt(args[0])));
        player.sendMessage(CC.CHAT_BAR);
    }
}
