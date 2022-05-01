package rip.crystal.practice.game.tournament.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.var;
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
import rip.crystal.practice.player.clan.Clan;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
public class TournamentClans extends Tournament<Clan> {

    private final Map<Clan, TeamGameParticipant<MatchGamePlayer>> clans = Maps.newHashMap();
    Countdown countdown;

    public void start(){
        nextRound();
    }

    public void join(Clan clan){
        Player clanLeader = Bukkit.getPlayer(clan.getLeader());

        MatchGamePlayer leader = new MatchGamePlayer(clanLeader.getUniqueId(), clanLeader.getName());

        TeamGameParticipant<MatchGamePlayer> teamGameParticipant = new TeamGameParticipant<>(leader);

        clan.getOnPlayers().forEach(player -> {
            getPlayers().add(player.getUniqueId());
            Profile.get(player.getPlayer().getUniqueId()).setInTournament(true);
            if (!player.getPlayer().equals(clanLeader)) {
                MatchGamePlayer gamePlayer = new MatchGamePlayer(player.getUniqueId(), player.getName());
                teamGameParticipant.getPlayers().add(gamePlayer);
            }
        });
        getTeams().add(teamGameParticipant);

        clans.put(clan, teamGameParticipant);

        broadcast("&7Clan &f&l" + clan.getName() + "&7 has join to tournament.");
        if (getTeams().size() == getLimit()) {
            countdown = Countdown.of(15, TimeUnit.SECONDS)
                .players(getOnlinePlayers())
                .broadcastAt(15, TimeUnit.SECONDS)
                .broadcastAt(10, TimeUnit.SECONDS)
                .broadcastAt(5, TimeUnit.SECONDS)
                .broadcastAt(4, TimeUnit.SECONDS)
                .broadcastAt(3, TimeUnit.SECONDS)
                .broadcastAt(2, TimeUnit.SECONDS)
                .broadcastAt(1, TimeUnit.SECONDS)
                .withMessage("&7Tournament start in&9 {time}")
                .onFinish(this::start).start();
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
        if (getRound() == 1) round = "&7Starting in &9{time}";
        countdown = Countdown.of(10, TimeUnit.SECONDS)
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
                        if (teamsShuffle.isEmpty()) {
                            cancel();
                            return;
                        }
                        GameParticipant<MatchGamePlayer> teamA = teamsShuffle.poll();
                        if (teamsShuffle.isEmpty()) {
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
        var leader = teamEliminated.getLeader();
        var profile = Profile.get(leader.getPlayer().getUniqueId());
        var clan = profile.getClan();
        getTeams().forEach(team -> team.getPlayers().forEach(matchGamePlayer -> {
            Profile.get(matchGamePlayer.getUuid()).setInTournament(false);
            Player player = matchGamePlayer.getPlayer();
            getPlayers().remove(matchGamePlayer.getUuid());
            if(player != null)
                player.sendMessage(CC.translate("&fClan&7 &c" + clan.getColoredName() + "&f has been &celiminated."));
        }));
    }

    public void end(GameParticipant<MatchGamePlayer> winner){
        getTeams().forEach(team -> team.getPlayers().forEach(matchGamePlayer ->
            Profile.get(matchGamePlayer.getPlayer().getUniqueId()).setInTournament(false)));
        getTeams().clear();
        setStarted(false);
        setTournament(null);
        setWinner(winner);
        setState(TournamentState.ENDED);
        if (winner != null) {
            new TournamentEndEvent(winner, false, true).call();
            MatchGamePlayer leader = winner.getLeader();
            Profile profile = Profile.get(leader.getPlayer().getUniqueId());
            Clan clan = profile.getClan();
            Bukkit.broadcastMessage(CC.translate("&c" + clan.getColoredName() + "&f has won the tournament."));
            clan.setTournamentWins(clan.getTournamentWins() + 1);
        } else {
            Bukkit.broadcastMessage(CC.translate("Tournament has been stopped."));
        }
        TaskUtil.runLater(() -> setTournament(null), 20 * 15L);
        if (countdown != null) countdown.stop();
    }

    @Override
    public List<String> getTournamentScoreboard() {
        List<String> lines = Lists.newArrayList();
        BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
        String bars = config.getString("LINES.BARS");

        config.getStringList("TOURNAMENTS.CLANS.LINES").forEach(line -> {
            lines.add(line.replace("{kit}", getKit().getName())
                    .replace("{bars}", bars)
                    .replace("{size}", String.valueOf(getTeams().size()))
                    .replace("{limit}", String.valueOf(getLimit()))
                    .replace("{state}", getState().getName()));
        });

//        lines.add(CC.SB_BAR);
//        lines.add("&6&lClans Tournament");
//        lines.add("&aKit&7: &f" + getKit().getName());
//        lines.add("&aTeams&7: &f" + getTeams().size() + "&7/&f" + getLimit());
//        lines.add("&aState&7: " + getState().getName());
        if (this.getState() == TournamentState.IN_FIGHT) {
//            lines.add("&aRound&7: &f#" + getRound());
            config.getStringList("TOURNAMENTS.CLANS.IN-FIGHT").forEach(line -> {
                lines.add(line.replace("{round}", String.valueOf(getRound())));
            });
        }
        if (this.getState() == TournamentState.ENDED && getWinner() != null) {
            MatchGamePlayer leader = getWinner().getLeader();
//            lines.add("&2&lClan Winner: " + Profile.getColorPlayer(leader.getPlayer()) + leader.getPlayer().getName());
            config.getStringList("TOURNAMENTS.CLANS.ON-END").forEach(line -> {
                lines.add(line.replace("{color}", Profile.get(leader.getPlayer().getUniqueId()).getColor())
                        .replace("{player}", leader.getPlayer().getName()));
            });
        }

        return lines;
    }
}