package rip.crystal.practice.game.tournament.impl;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.game.tournament.TournamentState;
import rip.crystal.practice.game.tournament.events.TournamentEndEvent;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.profile.participant.team.TeamGameParticipant;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.countdown.Countdown;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter @Setter
public class TournamentTeams extends Tournament<Party> {

    public void start(){
        setState(TournamentState.IN_FIGHT);
        nextRound();
    }

    public void join(Party party){
        Player partyLeader = party.getLeader();

        MatchGamePlayer leader = new MatchGamePlayer(partyLeader.getUniqueId(), partyLeader.getName());

        TeamGameParticipant<MatchGamePlayer> teamGameParticipant = new TeamGameParticipant<>(leader);

        party.getListOfPlayers().forEach(player -> {
            getPlayers().add(player.getUniqueId());
            Profile.get(player.getPlayer().getUniqueId()).setInTournament(true);
            if (!player.getPlayer().equals(partyLeader)) {
                MatchGamePlayer gamePlayer = new MatchGamePlayer(player.getUniqueId(), player.getName());
                teamGameParticipant.getPlayers().add(gamePlayer);
            }
        });
        getTeams().add(teamGameParticipant);

        broadcast("&7Party of " + partyLeader.getDisplayName() + "&7 has join to tournament.");
        if(getTeams().size() == getLimit()){
            Countdown.of(15, TimeUnit.SECONDS)
                .players(getOnlinePlayers())
                .broadcastAt(15, TimeUnit.SECONDS)
                .broadcastAt(10, TimeUnit.SECONDS)
                .broadcastAt(5, TimeUnit.SECONDS)
                .broadcastAt(4, TimeUnit.SECONDS)
                .broadcastAt(3, TimeUnit.SECONDS)
                .broadcastAt(2, TimeUnit.SECONDS)
                .broadcastAt(1, TimeUnit.SECONDS)
                .withMessage("&7Tournament start in&6 {time}")
                .onFinish(this::start)
                .start();
        }
    }

    public void nextRound(){
        setState(TournamentState.SELECTING_DUELS);
        setRound(getRound() + 1);
        //Shuffle list to randomize
        Collections.shuffle(getTeams());
        //New team LinkedList to remove usedTeams
        LinkedList<GameParticipant<MatchGamePlayer>> teamsShuffle = new LinkedList<>(getTeams());
        //Count down
        String round = "&7Next round in&9 {time}";
        if(getRound() == 1){
            round = "&7Starting in &9{time}";
        }
        Countdown.of(10, TimeUnit.SECONDS)
            .players(getOnlinePlayers())
            .broadcastAt(10, TimeUnit.SECONDS)
            .broadcastAt(5, TimeUnit.SECONDS)
            .broadcastAt(4, TimeUnit.SECONDS)
            .broadcastAt(3, TimeUnit.SECONDS)
            .broadcastAt(2, TimeUnit.SECONDS)
            .broadcastAt(1, TimeUnit.SECONDS)
            .withMessage(round)
            .onFinish(() -> {
                setState(TournamentState.IN_FIGHT);
                //Logic to start match
                TaskUtil.runTimer(new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(teamsShuffle.isEmpty()) {
                            cancel();
                            return;
                        }
                        GameParticipant<MatchGamePlayer> teamA = teamsShuffle.poll();
                        if(teamsShuffle.isEmpty()) {
                            teamA.getPlayers().forEach(matchGamePlayer ->
                                matchGamePlayer.getPlayer().sendMessage(CC.translate("&cNo other player found," +
                                    " you should wait in this round.")));
                            return;
                        }
                        GameParticipant<MatchGamePlayer> teamB = teamsShuffle.poll();

                        Arena arena = Arena.getRandomArena(getKit());

                        if (arena == null) {
                            teamA.getPlayers().forEach(matchGamePlayer ->
                                matchGamePlayer.getPlayer()
                                    .sendMessage(CC.translate("&cTried to start a match but there are no available arenas.")));
                            teamB.getPlayers().forEach(matchGamePlayer ->
                                matchGamePlayer.getPlayer()
                                    .sendMessage(CC.translate("&cTried to start a match but there are no available arenas.")));
                            return;
                        }
                        arena.setActive(true);
                        Match match = new BasicTeamMatch(null, getKit(), arena, false, teamA, teamB);
                        match.start();
                        getMatches().add(match);
                    }
                }, 1, 1);
            }).start();
    }

    public void eliminatedTeam(GameParticipant<MatchGamePlayer> teamEliminated) {
        getTeams().remove(teamEliminated);
        getTeams().forEach(team -> team.getPlayers().forEach(matchGamePlayer -> {
            Profile.get(matchGamePlayer.getPlayer().getUniqueId()).setInTournament(false);
            getPlayers().remove(matchGamePlayer.getUuid());
            Player player = matchGamePlayer.getPlayer();
            if(player != null)
                player.sendMessage(CC.translate("&fThe team of&c " + teamEliminated.getConjoinedNames() + "&f has been &celiminated."));
        }));
    }

    public void end(GameParticipant<MatchGamePlayer> winner) {
        getTeams().forEach(team -> team.getPlayers().forEach(matchGamePlayer ->
            Profile.get(matchGamePlayer.getPlayer().getUniqueId()).setInTournament(false)
        ));
        getTeams().clear();
        setStarted(false);
        setTournament(null);
        setWinner(winner);
        setState(TournamentState.ENDED);
        if(winner != null){
            new TournamentEndEvent(winner, false, false).call();
            Bukkit.broadcastMessage(CC.translate("&c" + winner.getConjoinedNames() + "&f has won the tournament."));
        }else {
            Bukkit.broadcastMessage(CC.translate("Tournament has been stopped."));
        }
    }

    @Override
    public List<String> getTournamentScoreboard() {
        List<String> lines = Lists.newArrayList();
        BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
        String bars = config.getString("LINES.BARS");

        config.getStringList("TOURNAMENTS.TEAMS.LINES").forEach(line -> {
            lines.add(line.replace("{kit}", getKit().getName())
                    .replace("{bars}", bars)
                    .replace("{size}", String.valueOf(getTeams().size()))
                    .replace("{limit}", String.valueOf(getLimit()))
                    .replace("{state}", getState().getName()));
        });

//        lines.add(CC.SB_BAR);
//        lines.add("&6&lTeams Tournament");
//        lines.add("&aKit&7: &f" + getKit().getName());
//        lines.add("&aTeams&7: &f" + getTeams().size() + "&7/&f" + getLimit());
//        lines.add("&aState&7: " + getState().getName());
        if (this.getState() == TournamentState.IN_FIGHT) {
//            lines.add("&aRound&7: &f#" + getRound());
            config.getStringList("TOURNAMENTS.TEAMS.IN-FIGHT").forEach(line -> {
                lines.add(line.replace("{round}", String.valueOf(getRound())));
            });
        }
        if (this.getState() == TournamentState.ENDED && getWinner() != null) {
            MatchGamePlayer leader = getWinner().getLeader();
//            lines.add("&2&lWinner: " + Profile.getColorPlayer(leader.getPlayer()) + leader.getPlayer().getName() + " &2team");
            config.getStringList("TOURNAMENTS.TEAMS.ON-END").forEach(line -> {
                lines.add(line.replace("{color}", Profile.get(leader.getPlayer().getUniqueId()).getColor())
                        .replace("{player}", leader.getPlayer().getName()));
            });
        }

        return lines;
    }
}