package rip.crystal.practice.ffa.command;
/* 
   Made by Hysteria Development Team
   Created on 27.11.2021
*/

import rip.crystal.practice.ffa.command.subcommands.FFAJoinCommand;
import rip.crystal.practice.ffa.command.subcommands.FFALeaveCommand;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class FFACommand extends BaseCommand {

    public FFACommand() {
        new FFALeaveCommand();
        new FFAJoinCommand();
    }

    @Command(name = "ffa")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if (args.length == 0) {
            player.sendMessage(CC.translate(("&9/ffa leave &7- &fLeave FFA")));
            player.sendMessage(CC.translate(("&9/ffa join &7- &fJoin FFA")));
        }
    }
}
