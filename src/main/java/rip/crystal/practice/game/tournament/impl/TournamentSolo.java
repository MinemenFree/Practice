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
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.countdown.Countdown;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter @Setter
public class TournamentSolo extends Tournament<Player> {

    @Override
    public void join(Player player){
        Profile profile = Profile.get(player.getUniqueId());
        MatchGamePlayer playerA = new MatchGamePlayer(player.getUniqueId(), player.getName());
        getTeams().add(new GameParticipant<>(playerA));

        broadcast(player.getName() + "&7 has joined the tournament.");

        profile.setInTournament(true);
        profile.setState(ProfileState.TOURNAMENT);
        Hotbar.giveHotbarItems(player);

        getPlayers().add(player.getUniqueId());

        if(getTeams().size() == getLimit()) {
            Countdown.of(1, TimeUnit.MINUTES)
                    .players(getOnlinePlayers())
                    .broadcastAt(1, TimeUnit.MINUTES)
                    .broadcastAt(30, TimeUnit.SECONDS)
                    .broadcastAt(10, TimeUnit.SECONDS)
                    .broadcastAt(5, TimeUnit.SECONDS)
                    .broadcastAt(4, TimeUnit.SECONDS)
                    .broadcastAt(3, TimeUnit.SECONDS)
                    .broadcastAt(2, TimeUnit.SECONDS)
                    .broadcastAt(1, TimeUnit.SECONDS)
                    .withMessage("&7Tournament start in&6 {time}")
                    .onFinish(this::start).start();

            setState(TournamentState.STARTING);
        }
    }

    public void start() {
        nextRound();
    }

    public void nextRound() {
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
                        if (teamsShuffle.isEmpty()) {
                            cancel();
                            return;
                        }
                        GameParticipant<MatchGamePlayer> teamA = teamsShuffle.poll();
                        if (teamsShuffle.isEmpty()) {
                            teamA.getPlayers().forEach(matchGamePlayer ->
                                matchGamePlayer.getPlayer().sendMessage(CC.translate("&9No other player found," + " you should wait in this round.")));
                            return;
                        }
                        GameParticipant<MatchGamePlayer> teamB = teamsShuffle.poll();

                        Arena arena = Arena.getRandomArena(getKit());

                        if (arena == null) {
                            teamA.getPlayers().forEach(matchGamePlayer -> matchGamePlayer.getPlayer()
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
                }, 1L, 1L);
            }).start();
    }

    public void eliminatedTeam(GameParticipant<MatchGamePlayer> teamEliminated) {
        getTeams().remove(teamEliminated);
        getTeams().forEach(team -> team.getPlayers().forEach(matchGamePlayer -> {
            Profile.get(matchGamePlayer.getPlayer().getUniqueId()).setInTournament(false);
            getPlayers().remove(matchGamePlayer.getUuid());
            Player player = matchGamePlayer.getPlayer();
            if(player != null) player.sendMessage(CC.translate("&c" + teamEliminated.getConjoinedNames() + "&f has been &celiminated."));
        }));
    }

    public void end(GameParticipant<MatchGamePlayer> winner){
        setState(TournamentState.ENDED);
        getTeams().forEach(team -> team.getPlayers().forEach(matchGamePlayer ->
            Profile.get(matchGamePlayer.getPlayer().getUniqueId()).setInTournament(false)
        ));
        getTeams().clear();
        setStarted(false);
        setWinner(winner);
        if(winner != null) {
            new TournamentEndEvent(winner, true, false).call();
            Bukkit.broadcastMessage(CC.CHAT_BAR);
            Bukkit.broadcastMessage(CC.translate("&7(*) &cTournament Ended &7(*)"));
            Bukkit.broadcastMessage(CC.translate("&fWinner: &c" + winner.getConjoinedNames()));
            Bukkit.broadcastMessage(CC.CHAT_BAR);
        }else {
            Bukkit.broadcastMessage(CC.CHAT_BAR);
            Bukkit.broadcastMessage(CC.translate("&cTournament has been cancelled."));
            Bukkit.broadcastMessage(CC.CHAT_BAR);
        }
        TaskUtil.runLater(() -> setTournament(null), 20 * 10L);
    }

    @Override
    public List<String> getTournamentScoreboard() {
        List<String> lines = Lists.newArrayList();
        BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
        String bars = config.getString("LINES.BARS");

        config.getStringList("TOURNAMENTS.SOLO.LINES").forEach(line -> {
            lines.add(line.replace("{kit}", getKit().getName())
                    .replace("{bars}", bars)
                    .replace("{size}", String.valueOf(getPlayers().size()))
                    .replace("{limit}", String.valueOf(getLimit()))
                    .replace("{state}", getState().getName()));
        });


        if (this.getState() == TournamentState.IN_FIGHT) {
            config.getStringList("TOURNAMENTS.SOLO.IN-FIGHT").forEach(line -> {
                lines.add(line.replace("{round}", String.valueOf(getRound())));
            });
        }
        if (this.getState() == TournamentState.ENDED && getWinner() != null) {
            MatchGamePlayer leader = getWinner().getLeader();
            config.getStringList("TOURNAMENTS.SOLO.ON-END").forEach(line -> {
                lines.add(line.replace("{color}", Profile.get(leader.getPlayer().getUniqueId()).getColor())
                        .replace("{player}", leader.getPlayer().getName()));
            });
        }

        return lines;
    }
}