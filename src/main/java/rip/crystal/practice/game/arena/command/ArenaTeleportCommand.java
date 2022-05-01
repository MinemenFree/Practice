package rip.crystal.practice.game.arena.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.utilities.chat.CC;

public class ArenaTeleportCommand extends BaseCommand {

    @Command(name = "arena.teleport", permission = "cpractice.arena.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Arena arena = Arena.getByName(args[0]);
        if (arena == null) {
            player.sendMessage(CC.RED + "An arena with that name does not exist.");
            return;
        }

        if(arena.getSpawnA() == null) {
            player.sendMessage(CC.translate("&cYou must set Arena Spawn (A) first."));
            return;
        }
        player.teleport(arena.getSpawnA());
        player.sendMessage(CC.translate("&aYou have been teleported to Arena " + arena.getName()));
    }
}