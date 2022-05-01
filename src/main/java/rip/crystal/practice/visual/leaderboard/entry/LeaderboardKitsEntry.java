package rip.crystal.practice.visual.leaderboard.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rip.crystal.practice.player.profile.Profile;

@Getter
@AllArgsConstructor
public class LeaderboardKitsEntry {

    private final Profile profile;
    private final int elo;

}
