package rip.crystal.practice.game.tournament;
/* 
   Made by cpractice Development Team
   Created on 12.11.2021
*/

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;

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
