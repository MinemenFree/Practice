package rip.crystal.practice.player.cosmetics.impl.killeffects.command;
/* 
   Made by cpractice Development Team
   Created on 04.11.2021
*/

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.cosmetics.impl.killeffects.menu.KillEffectsMenu;

public class KillEffectCommand extends BaseCommand {
    @Command(name = "killeffect")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new KillEffectsMenu().openMenu(player);
    }
}
