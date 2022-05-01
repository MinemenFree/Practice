package rip.crystal.practice.player.clan;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.tournament.events.TournamentEndEvent;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.Profile;

public class ClanListener implements Listener {

    @EventHandler
    public void onClanWinTournament(TournamentEndEvent event){
        if(!event.isClan()) return;
        MatchGamePlayer leader = event.getWinner().getLeader();
        Player player = leader.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        clan.addPoints(cPractice.get().getMainConfig().getInteger("WINNING-POINTS-CLAN-TOURNAMENT"));
    }
}