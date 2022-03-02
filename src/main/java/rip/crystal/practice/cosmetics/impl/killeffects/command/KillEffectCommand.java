package rip.crystal.practice.cosmetics.impl.killeffects.command;
/* 
   Made by Hysteria Development Team
   Created on 04.11.2021
*/

import rip.crystal.practice.cosmetics.impl.killeffects.menu.KillEffectsMenu;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KillEffectCommand extends BaseCommand {
    @Command(name = "killeffect")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new KillEffectsMenu().openMenu(player);
    }
}
