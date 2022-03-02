package rip.crystal.practice.tournament.commands.subcommands;

import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.profile.hotbar.Hotbar;
import rip.crystal.practice.tournament.Tournament;
import rip.crystal.practice.tournament.TournamentState;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TournamentStopCommand extends BaseCommand {

    @Command(name = "tournament.stop", permission = "tournament.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Tournament<?> tournament = Tournament.getTournament();
        if (tournament == null || tournament.getState() == TournamentState.ENDED) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(ChatColor.RED + "No tournament found.");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }
        if ((tournament.getState() == TournamentState.IN_FIGHT || tournament.getState() == TournamentState.SELECTING_DUELS)
                && tournament.getTeams().size() == 1) {
            tournament.end(tournament.getTeams().get(0));
            return;
        }
        tournament.end(null);

        Profile profile = Profile.get(player.getUniqueId());
        profile.setState(ProfileState.LOBBY);
        Hotbar.giveHotbarItems(player);
    }
}