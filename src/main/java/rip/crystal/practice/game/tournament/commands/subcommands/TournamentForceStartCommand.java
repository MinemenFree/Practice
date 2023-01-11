package rip.crystal.practice.game.tournament.commands.subcommands;
/*
   Made by cpractice Development Team
   Created on 10.10.2021
*/

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.game.tournament.TournamentState;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

public class TournamentForceStartCommand extends BaseCommand {

    @Command(name = "tournament.forcestart", permission = "tournament.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        Tournament<?> tournament = Tournament.getTournament();
        if (tournament == null || tournament.getState() == TournamentState.ENDED) {
            new MessageFormat(Locale.TOURNAMENT_NO_TOURNAMENT_FOUND.format(profile.getLocale()));
            return;
        }

        if(!tournament.isStarted()) {
            tournament.start();
            new MessageFormat(Locale.TOURNAMENT_FORCE_STARTED.format(profile.getLocale()));
        }
    }
}
