package rip.crystal.practice.game.tournament.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.game.tournament.TournamentState;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.utilities.chat.CC;

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