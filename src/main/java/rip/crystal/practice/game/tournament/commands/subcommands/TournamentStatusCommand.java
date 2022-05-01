package rip.crystal.practice.game.tournament.commands.subcommands;
/* 
   Made by cpractice Development Team
   Created on 10.10.2021
*/

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.game.tournament.TournamentState;
import rip.crystal.practice.utilities.chat.CC;

public class TournamentStatusCommand extends BaseCommand {

    @Command(name = "tournament.status")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Tournament<?> tournament = Tournament.getTournament();

        if (tournament == null || tournament.getState() == TournamentState.ENDED) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(ChatColor.RED + "No tournament found.");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate( "&7(*) &9&lTournament &7(*)"));
        player.sendMessage(ChatColor.RED + "Current Matches: " + ChatColor.WHITE + tournament.getMatches().size());
        player.sendMessage(ChatColor.RED + "Players Limit: " + ChatColor.WHITE + tournament.getLimit());
        player.sendMessage(ChatColor.RED + "Players: " + ChatColor.WHITE + tournament.getPlayers().size());
        player.sendMessage(ChatColor.RED + "Stage: " + ChatColor.WHITE + tournament.getState());
        player.sendMessage(ChatColor.RED + "Kit: " + ChatColor.WHITE + tournament.getKit().getName());
        player.sendMessage(CC.CHAT_BAR);
    }
}
