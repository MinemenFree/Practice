package rip.crystal.practice.visual.leaderboard.entry;

import rip.crystal.practice.player.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardKitsEntry {

    private final Profile profile;
    private final int elo;

}
