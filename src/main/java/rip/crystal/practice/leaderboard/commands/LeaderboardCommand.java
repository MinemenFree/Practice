package rip.crystal.practice.leaderboard.commands;

import rip.crystal.practice.leaderboard.menu.LeaderBoardMenu;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class LeaderboardCommand extends BaseCommand {

    @Command(name = "topelo", aliases = {"leaderboard"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new LeaderBoardMenu(player).openMenu(player);
    }
}
