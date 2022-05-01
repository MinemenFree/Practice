package rip.crystal.practice.player.profile.modmode.commands;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.modmode.Modmode;

public class StaffModeCommand extends BaseCommand {

    @Command(name = "staffmode", aliases = {"staff", "mod", "h"}, permission = "cpractice.staffmode")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getState() == ProfileState.EVENT || profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.SPECTATING || profile.getState() == ProfileState.QUEUEING || profile.getState() == ProfileState.FFA) {
            return;
        }

        if (profile.getState() == ProfileState.STAFF_MODE) Modmode.remove(player);
        else Modmode.add(player);
    }
}
