package rip.crystal.practice.tournament.commands.subcommands;
/*
   Made by Hysteria Development Team
   Created on 10.10.2021
*/

import rip.crystal.practice.tournament.Tournament;
import rip.crystal.practice.tournament.TournamentState;
import rip.crystal.practice.utilities.countdown.Countdown;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.utilities.Utils;

import java.util.concurrent.TimeUnit;

public class TournamentForceStartCommand extends BaseCommand {

    @Command(name = "tournament.forcestart", permission = "tournament.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Tournament<?> tournament = Tournament.getTournament();
        if (tournament == null || tournament.getState() == TournamentState.ENDED) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }
        if(!tournament.isStarted()) {
            tournament.start();
            player.sendMessage(ChatColor.RED + "You have force started the tournament!");
        }
    }
}
