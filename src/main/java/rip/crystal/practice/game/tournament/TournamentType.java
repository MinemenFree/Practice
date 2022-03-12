package rip.crystal.practice.game.tournament;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TournamentType {

    SOLO("Solo"),
    PARTY("Party"),
    CLANS("Clans");

    private final String name;

}