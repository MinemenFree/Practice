package rip.crystal.practice.tournament;
/* 
   Made by Hysteria Development Team
   Created on 12.11.2021
*/

import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.profile.participant.GameParticipant;
import rip.crystal.practice.tournament.impl.TournamentSolo;
import rip.crystal.practice.utilities.PlayerUtil;

public class TournamentListener implements Listener {

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        if (Tournament.getTournament() != null && (Tournament.getTournament().getState() == TournamentState.STARTING || Tournament.getTournament().getState() == TournamentState.SELECTING_DUELS)) {
            Player player = playerQuitEvent.getPlayer();
            Tournament.getTournament().getPlayers().remove(player.getUniqueId());
            Tournament.getTournament().getTeams().remove(Tournament.getTournament().getParticipant(player));
            Tournament.getTournament().setState(TournamentState.STARTING);
            Profile.get(player.getUniqueId()).setState(ProfileState.LOBBY);
            Profile.get(player.getUniqueId()).setInTournament(false);
        }
    }
}
