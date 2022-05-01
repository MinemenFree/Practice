package rip.crystal.practice.player.cosmetics.command;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.cosmetics.menu.CosmeticsMenu;

public class CosmeticsCommand extends BaseCommand {

    @Command(name="cosmetic", aliases = {"cosmetics", "cosm"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new CosmeticsMenu().openMenu(player);
    }
}
