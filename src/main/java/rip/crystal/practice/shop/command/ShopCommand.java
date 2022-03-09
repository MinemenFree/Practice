package rip.crystal.practice.shop.command;

import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;
import rip.crystal.practice.shop.menu.ShopMenu;

public class ShopCommand extends BaseCommand {

    @Command(name="shop", aliases = {"store"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new ShopMenu().openMenu(player);
    }
}
