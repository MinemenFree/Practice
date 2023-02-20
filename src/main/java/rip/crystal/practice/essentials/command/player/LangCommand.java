package rip.crystal.practice.essentials.command.player;

import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.menu.LangMenu;

public class LangCommand extends BaseCommand {

    @Command(name = "lang")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        
        if (cPractice.get().getMainConfig().getBoolean("TOGGLE_LANGUAGE_MENU")) {
            new LangMenu().openMenu(player);
        }
    }
}
