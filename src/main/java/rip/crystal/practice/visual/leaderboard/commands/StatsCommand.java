package rip.crystal.practice.visual.leaderboard.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.visual.leaderboard.menu.StatisticsMenu;

public class StatsCommand extends BaseCommand {

    @Command(name = "stats")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length > 0) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null || !target.hasPlayedBefore()) {
                new MessageFormat(Locale.PLAYER_NOT_FOUND
                        .format(Profile.get(player.getUniqueId()).getLocale()))
                        .send(player);
                return;
            }
            new StatisticsMenu(target).openMenu(player);
            return;
        }

        new StatisticsMenu(player).openMenu(player);
    }
}