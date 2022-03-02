package rip.crystal.practice.ffa.command.subcommands;
/* 
   Made by Hysteria Development Team
   Created on 27.11.2021
*/

import rip.crystal.practice.arena.Arena;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class FFAJoinCommand extends BaseCommand {

    @Command(name="ffa.join")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getState() != ProfileState.LOBBY) {
            return;
        }

        cPractice.get().getFfaManager().joinFFA(player, Arena.getByName("FFA"));
    }
}
