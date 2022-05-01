package rip.crystal.practice.match.duel.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.queue.menus.QueueSelectKitMenu;

public class RankedCommand extends BaseCommand {

    @Command(name = "ranked")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new QueueSelectKitMenu(true).openMenu(player);
    }
}
