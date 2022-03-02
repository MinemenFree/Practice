package rip.crystal.practice.tournament.events;

import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.profile.participant.GameParticipant;
import rip.crystal.practice.utilities.event.CustomEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TournamentEndEvent extends CustomEvent {

    private final GameParticipant<MatchGamePlayer> winner;
    private final boolean team;
    private final boolean clan;

}