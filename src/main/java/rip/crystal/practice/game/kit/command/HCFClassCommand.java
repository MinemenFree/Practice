package rip.crystal.practice.game.kit.command;
/* 
   Made by cpractice Development Team
   Created on 11.10.2021
*/

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.party.menu.HCFClassMenu;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;

public class HCFClassCommand extends BaseCommand {

    @Command(name = "hcfclass")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getParty().getLeader().getUniqueId().equals(player.getUniqueId())) {
            new HCFClassMenu().openMenu(player);
        } else {
            player.sendMessage(CC.translate("&cYou must be in a party and be the leader to execute this command!"));
        }
    }
}
