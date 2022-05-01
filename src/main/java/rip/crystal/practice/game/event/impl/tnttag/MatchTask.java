package rip.crystal.practice.game.event.impl.tnttag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.Locale;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

import java.util.Arrays;

@AllArgsConstructor
public class MatchTask extends BukkitRunnable {

    public final TNTTagGameLogic tntTagGameLogic;
    @Getter public int seconds;

    @Override
    public void run() {
        if (Arrays.asList(30, 15, 10, 5, 4, 3, 2, 1).contains(seconds)) {
            tntTagGameLogic.getParticipants().forEach(gamePlayerGameParticipant -> {
                new MessageFormat(Locale.EVENT_MATCH_REMAINING.format(Profile.get(gamePlayerGameParticipant.getLeader().getUuid()).getLocale()))
                        .add("{seconds}", String.valueOf(seconds))
                        .add("{context}", seconds == 1 ? "" : "s")
                        .send(gamePlayerGameParticipant.getLeader().getPlayer());
            });
        }
        if (seconds < 1) {
            tntTagGameLogic.endRound();
            cancel();
        }

        seconds--;
    }
}
