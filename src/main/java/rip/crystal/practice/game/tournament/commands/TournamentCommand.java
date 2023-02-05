package rip.crystal.practice.game.tournament.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.game.tournament.commands.subcommands.*;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

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
		Profile profile = Profile.get(player.getUniqueId());

        if(player.hasPermission("tournament.admin")) {
            new MessageFormat(Locale.TOURNAMENT_ADMIN_COMMAND_USAGE.format(profile.getLocale()));
        } else {
            new MessageFormat(Locale.TOURNAMENT_COMMAND_USAGE.format(profile.getLocale()));
        }
    }
}
