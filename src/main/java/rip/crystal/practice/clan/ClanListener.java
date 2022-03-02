package rip.crystal.practice.clan;

import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.tournament.events.TournamentEndEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClanListener implements Listener {

    @EventHandler
    public void onClanWinTournament(TournamentEndEvent event){
        if(!event.isClan()) return;
        MatchGamePlayer leader = event.getWinner().getLeader();
        Player player = leader.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        clan.addPoints(cPractice.get().getMainConfig().getInteger("WINNING-POINTS-CLAN-TOURNAMENT"));
        clan.upgradeCategory();
    }
}