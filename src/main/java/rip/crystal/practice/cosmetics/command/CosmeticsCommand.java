package rip.crystal.practice.cosmetics.command;
/* 
   Made by Hysteria Development Team
   Created on 30.11.2021
*/

import rip.crystal.practice.cosmetics.menu.CosmeticsMenu;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class CosmeticsCommand extends BaseCommand {

    @Command(name="cosmetic", aliases = {"cosmetics", "cosm"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new CosmeticsMenu().openMenu(player);
    }
}
