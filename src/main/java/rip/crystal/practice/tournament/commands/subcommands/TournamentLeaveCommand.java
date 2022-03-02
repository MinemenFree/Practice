package rip.crystal.practice.tournament.commands.subcommands;
/*
   Made by Hysteria Development Team
   Created on 10.10.2021
*/

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.profile.hotbar.Hotbar;
import rip.crystal.practice.profile.participant.GameParticipant;
import rip.crystal.practice.tournament.Tournament;
import rip.crystal.practice.tournament.TournamentState;
import rip.crystal.practice.utilities.PlayerUtil;

public class TournamentLeaveCommand extends BaseCommand {

    @Command(name = "tournament.leave")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        Tournament<?> tournament = Tournament.getTournament();
        if (tournament == null || tournament.getState() == TournamentState.ENDED || !profile.isInTournament()) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }

        tournament.removePlayer(player.getUniqueId());
        tournament.getPlayers().remove(player.getUniqueId());

        // Reset players' state
        profile.setState(ProfileState.LOBBY);
        profile.setInTournament(false);

        PlayerUtil.reset(player);
        player.teleport(cPractice.get().getEssentials().getSpawn());
        Hotbar.giveHotbarItems(player);
    }
}
