package rip.crystal.practice.visual.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import lombok.AllArgsConstructor;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.visual.leaderboard.Leaderboard;

import java.util.List;

@AllArgsConstructor
public class TopGlobalName implements PlaceholderReplacer {

    public int pos;

    @Override
    public String update() {
        try {
            if (Leaderboard.getLeaderboards().isEmpty()) return " ";
            List<Profile> test = Leaderboard.getLeaderboards();
            if (test.get(pos) == null) return " ";
            Profile profile = test.get(pos);
            return CC.translate(profile.getName()); //profile.getColor())
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }
}
