package rip.crystal.practice.game.ffa.command.subcommands;
/* 
   Made by cpractice Development Team
   Created on 27.11.2021
*/

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;

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
