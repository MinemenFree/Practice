package rip.crystal.practice.duel.command;

import rip.crystal.practice.queue.menus.QueueSelectKitMenu;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class RankedCommand extends BaseCommand {

    @Command(name = "ranked")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new QueueSelectKitMenu(true).openMenu(player);
    }
}
