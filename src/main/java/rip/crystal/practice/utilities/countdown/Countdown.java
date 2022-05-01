package rip.crystal.practice.utilities.countdown;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.TimeUtils;
import rip.crystal.practice.utilities.chat.CC;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Countdown implements Runnable {
    private final String broadcastMessage;

    private final int[] broadcastAt;
    private final Runnable tickHandler;
    private final Runnable broadcastHandler;
    private final Runnable finishHandler;
    private final Predicate<Player> messageFilter;
    private int seconds;
    private boolean first;
    private final List<Player> playerList;
    // Our scheduled task's assigned id, needed for canceling
    private final Integer assignedTaskId;

    Countdown(int seconds, String broadcastMessage, Runnable tickHandler, Runnable broadcastHandler, Runnable finishHandler, Predicate<Player> messageFilter, List<Player> playerList, int... broadcastAt) {
        this.first = true;
        this.seconds = seconds;
        this.broadcastMessage = broadcastMessage;
        this.broadcastAt = broadcastAt;
        this.tickHandler = tickHandler;
        this.broadcastHandler = broadcastHandler;
        this.finishHandler = finishHandler;
        this.messageFilter = messageFilter;
        this.playerList = playerList;
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(cPractice.get(), this, 0L, 20L);
    }

    public static CountdownBuilder of(int amount, TimeUnit unit) {
        return new CountdownBuilder((int) unit.toSeconds(amount));
    }

    public void run() {
        if (!this.first) {
            --this.seconds;
        } else {
            this.first = false;
        }
        for (int index : this.broadcastAt) {
            if (this.seconds == index) {
                if(broadcastMessage != null){
                    String message = this.broadcastMessage.replace("{time}", TimeUtils.formatIntoDetailedString(this.seconds));
                    if (playerList == null) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (this.messageFilter == null || this.messageFilter.test(player)) {
                                player.sendMessage(CC.translate(message));
                            }
                        }
                    } else {
                        for (Player player : playerList) {
                            if (player != null && (player.isOnline() && this.messageFilter == null || this.messageFilter.test(player))) {
                                player.sendMessage(CC.translate(message));
                            }
                        }
                    }
                }
                if (this.broadcastHandler != null) {
                    this.broadcastHandler.run();
                }
            }
        }
        if (this.seconds == 0) {
            if (this.finishHandler != null) {
                this.finishHandler.run();
            }
            if (assignedTaskId != null) Bukkit.getScheduler().cancelTask(assignedTaskId);
        } else if (this.tickHandler != null) {
            this.tickHandler.run();
        }
    }

    public void stop(){
        Bukkit.getScheduler().cancelTask(assignedTaskId);
    }

    public int getSecondsRemaining() {
        return this.seconds;
    }
}
