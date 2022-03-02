package rip.crystal.practice.tournament.commands;

import rip.crystal.practice.tournament.commands.subcommands.*;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TournamentCommand extends BaseCommand {

    public TournamentCommand() {
        new TournamentStatusCommand();
        new TournamentForceStartCommand();
        new TournamentJoinCommand();
        new TournamentStartCommand();
        new TournamentStopCommand();
        new TournamentLeaveCommand();
    }

    @Command(name = "tournament")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        if(player.hasPermission("tournament.admin")) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(ChatColor.RED + "Usage: /tournament start <kit>");
            player.sendMessage(ChatColor.RED + "Usage: /tournament forcestart");
            player.sendMessage(ChatColor.RED + "Usage: /tournament status");
            player.sendMessage(ChatColor.RED + "Usage: /tournament leave");
            player.sendMessage(ChatColor.RED + "Usage: /tournament join");
            player.sendMessage(ChatColor.RED + "Usage: /tournament stop");
            player.sendMessage(CC.CHAT_BAR);
        } else {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(ChatColor.RED + "Usage: /tournament status");
            player.sendMessage(ChatColor.RED + "Usage: /tournament leave");
            player.sendMessage(ChatColor.RED + "Usage: /tournament join");
            player.sendMessage(CC.CHAT_BAR);
        }
    }
}