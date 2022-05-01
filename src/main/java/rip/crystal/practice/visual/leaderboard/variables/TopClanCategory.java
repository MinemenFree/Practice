package rip.crystal.practice.visual.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import lombok.AllArgsConstructor;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.visual.leaderboard.Leaderboard;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TopClanCategory implements PlaceholderReplacer {

    public int pos;

    @Override
    public String update() {
        try {
            if (Leaderboard.getClanLeaderboards().keySet().isEmpty()) return " ";
            List<String> names = new ArrayList<>(Leaderboard.getClanLeaderboards().keySet());
            if (names.get(pos) == null) return " ";
            if (Clan.getByName(names.get(pos)) == null) return " ";
            return CC.translate(Clan.getByName(names.get(pos)).getName());
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }
}
