package rip.crystal.practice.match.events;

import rip.crystal.practice.match.Match;
import rip.crystal.practice.utilities.event.CustomEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchEndEvent extends CustomEvent {

    private Match match;
}