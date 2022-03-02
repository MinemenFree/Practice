package rip.crystal.practice.arena.command;

import rip.crystal.practice.arena.menu.ArenaManagementMenu;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;


public class ArenaManageCommand extends BaseCommand {

    @Command(name = "arena.manage", permission = "hysteria.arena.admin", inGameOnly = true)
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        new ArenaManagementMenu().openMenu(player);
    }
}
