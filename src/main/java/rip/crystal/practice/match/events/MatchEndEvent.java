package rip.crystal.practice.match.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.utilities.event.CustomEvent;

@AllArgsConstructor
@Getter
public class MatchEndEvent extends CustomEvent {

    private Match match;
}