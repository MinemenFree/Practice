package rip.crystal.practice.essentials.command.player;

import rip.crystal.practice.profile.menu.LangMenu;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class LangCommand extends BaseCommand {

    @Command(name = "lang")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new LangMenu().openMenu(player);
    }
}