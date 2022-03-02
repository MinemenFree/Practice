package rip.crystal.practice.leaderboard.entry;

import rip.crystal.practice.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardKitsEntry {

    private final Profile profile;
    private final int elo;

}
