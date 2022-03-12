package rip.crystal.practice.game.tournament;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TournamentState {

    WAITING("Waiting"),
    STARTING("Starting"),
    IN_FIGHT("Fighting"),
    SELECTING_DUELS("Selecting duels"),
    ENDED("Ended");

    private final String name;

}