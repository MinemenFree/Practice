package rip.crystal.practice.game.tournament.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.utilities.event.CustomEvent;

@Getter @Setter
@AllArgsConstructor
public class TournamentEndEvent extends CustomEvent {

    private final GameParticipant<MatchGamePlayer> winner;
    private final boolean team;
    private final boolean clan;

}