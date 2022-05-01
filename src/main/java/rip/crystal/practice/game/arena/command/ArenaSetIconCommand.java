package rip.crystal.practice.game.arena.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.utilities.chat.CC;

public class ArenaSetIconCommand extends BaseCommand {

    @Command(name = "arena.seticon", permission = "cpractice.arena.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please usage: /arena seticon (arena)");
            return;
        }

        Arena arena = Arena.getByName(args[0]);
        if (arena == null) {
            player.sendMessage(CC.RED + "This arena doesn't exist.");
            return;
        }

        arena.setDisplayIcon(player.getItemInHand());
        arena.save();
        player.sendMessage(ChatColor.GREEN + "Kit icon update");
    }
}