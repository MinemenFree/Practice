package rip.crystal.practice.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import rip.crystal.practice.clan.Clan;
import rip.crystal.practice.leaderboard.Leaderboard;
import rip.crystal.practice.utilities.chat.CC;
import lombok.AllArgsConstructor;

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
            return CC.translate(Clan.getByName(names.get(pos)).getCategory().getPrefix());
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }
}
