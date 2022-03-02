package rip.crystal.practice.arena.command;

import rip.crystal.practice.arena.Arena;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ArenaTeleportCommand extends BaseCommand {

    @Command(name = "arena.teleport", permission = "hysteria.arena.admin")
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