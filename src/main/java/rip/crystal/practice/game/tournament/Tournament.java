package rip.crystal.practice.game.tournament;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.utilities.chat.CC;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @Setter
public abstract class Tournament<T>{

    @Getter @Setter private static Tournament<?> tournament;

    private boolean started = false;
    private TournamentState state = TournamentState.WAITING;
    private final List<UUID> players = Lists.newArrayList();
    private int size, limit = 5;
    private List<GameParticipant<MatchGamePlayer>> teams = Lists.newArrayList();
    private Kit kit;
    private final List<Match> matches = Lists.newArrayList();
    private boolean clans;
    private int round = 0;
    private GameParticipant<MatchGamePlayer> winner;

    public abstract void join(T type);

    public abstract void start();

    public abstract void nextRound();

    public abstract void eliminatedTeam(GameParticipant<MatchGamePlayer> teamEliminated);

    public abstract void end(GameParticipant<MatchGamePlayer> winner);

    public void removePlayer(UUID uuid) {
        this.players.remove(uuid);
    }

    public void broadcast(String msg){
        teams.forEach(team -> team.getPlayers().forEach(matchGamePlayer -> matchGamePlayer.getPlayer().sendMessage(CC.translate(msg))));
    }

    public List<Player> getOnlinePlayers(){
        return players.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public GameParticipant<MatchGamePlayer> getParticipant(Player player) {
        for (GameParticipant<MatchGamePlayer> gameParticipant : this.teams) {
            for (MatchGamePlayer matchGamePlayer : gameParticipant.getPlayers()) {
                if (!matchGamePlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) continue;
                return gameParticipant;
            }
        }
        return null;
    }

    public abstract List<String> getTournamentScoreboard();
}