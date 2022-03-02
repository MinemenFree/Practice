package rip.crystal.practice.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import rip.crystal.practice.leaderboard.Leaderboard;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TopKitElo implements PlaceholderReplacer {

    public String kit;

    @Override
    public String update() {
        if (Leaderboard.getKitLeaderboards().get(kit) == null) return " ";
        else if (!Leaderboard.getKitLeaderboards().get(kit).stream().findFirst().isPresent()) return " ";

        return String.valueOf(Leaderboard.getKitLeaderboards().get(kit).stream().findFirst().get().getElo());
    }
}
