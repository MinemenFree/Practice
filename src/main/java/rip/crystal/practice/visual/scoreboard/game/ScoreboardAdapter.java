package rip.crystal.practice.visual.scoreboard.game;
/* 
   Made by cpractice Development Team
   Created on 27.10.2021
*/

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;

import java.util.List;

public class ScoreboardAdapter {

    public static List<String> getScoreboardLinesSpecator(Player player) {
        List<String> lines = Lists.newArrayList();
        Profile profile = Profile.get(player.getUniqueId());
        BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();

        if (teamMatch != null) {
            cPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING").forEach(s ->
                    lines.add(s
                            .replace("{playerA}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantA().getLeader().getPlayer().getName()))
                            .replace("{playerB}", String.valueOf(((BasicTeamMatch) profile.getMatch()).getParticipantB().getLeader().getPlayer().getName()))
                            .replace("{duration}", profile.getMatch().getDuration())
                            .replace("{kit}", profile.getMatch().getKit().getName())
                            .replace("{arena}", profile.getMatch().getArena().getName())));
                            //.replace("{ranked}", (profile.getMatch().getQueue().isRanked() ? "&aTrue" : "&cFalse"))
        }

        return lines;
    }

    public static List<String> getScoreboardLinesFFA(Player player) {
        List<String> lines = Lists.newArrayList();
        //Profile profile = Profile.get(player.getUniqueId());

        cPractice.get().getScoreboardConfig().getStringList("FIGHTS.FFA").forEach(s ->
                lines.add(s
                        .replace("{players}", String.valueOf(cPractice.get().getFfaManager().getFFAPlayers().size()))
                        .replace("{ping}", String.valueOf(PlayerUtil.getPing(player)))
                        .replace("{kit}", cPractice.get().getFfaManager().getKit().getName())));

        return lines;
    }

}
