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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        Tournament<?> tournament = Tournament.getTournament();
        BasicTeamMatch match = profile.getBasicTeamMatch();
        MatchGamePlayer playerA = new MatchGamePlayer(player.getUniqueId(), player.getName());

        if(profile.isInTournament() && profile.getState() == ProfileState.TOURNAMENT) {
            // End match
            match.end();

            // Remove player from tournament
            Tournament.getTournament().getPlayers().remove(player.getUniqueId());

            // Reset players' state
            profile.setState(ProfileState.LOBBY);
            profile.setInTournament(false);

            PlayerUtil.reset(player);
            player.teleport(cPractice.get().getEssentials().getSpawn());
        }
    }
}
