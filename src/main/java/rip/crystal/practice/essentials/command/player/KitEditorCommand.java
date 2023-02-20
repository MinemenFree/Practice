package rip.crystal.practice.essentials.command.player;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.kit.menu.KitEditorSelectKitMenu;

public class KitEditorCommand extends BaseCommand {

    @Command(name = "kiteditor")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new KitEditorSelectKitMenu().openMenu(player);
    }
}
