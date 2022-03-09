package rip.crystal.practice.cosmetics.impl.trails.command;
/* 
   Made by Hysteria Development Team
   Created on 04.11.2021
*/

import org.bukkit.entity.Player;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import rip.crystal.practice.cosmetics.impl.trails.menu.TrailsEffectsMenu;

public class TrailEffectCommand extends BaseCommand {
    @Command(name = "traileffects")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new TrailsEffectsMenu().openMenu(player);
    }
}
