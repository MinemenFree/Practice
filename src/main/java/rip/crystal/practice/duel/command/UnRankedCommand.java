package rip.crystal.practice.duel.command;

import rip.crystal.practice.queue.menus.QueueSelectKitMenu;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class UnRankedCommand extends BaseCommand {

    @Command(name = "unranked")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new QueueSelectKitMenu(false).openMenu(player);
    }
}
