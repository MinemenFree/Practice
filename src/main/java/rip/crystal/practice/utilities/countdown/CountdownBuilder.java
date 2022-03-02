package rip.crystal.practice.utilities.countdown;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class CountdownBuilder {
    private final int seconds;
    private String message;
    private final List<Integer> broadcastAt;
    private Runnable tickHandler;
    private Runnable broadcastHandler;
    private Runnable finishHandler;
    private Predicate<Player> messageFilter;
    private List<Player> playerList;

    CountdownBuilder(final int seconds) {
        this.broadcastAt = new ArrayList<>();
        Preconditions.checkArgument(seconds >= 0, "Seconds cannot must be greater than or equal to 0!");
        this.seconds = seconds;
    }

    public CountdownBuilder withMessage(final String message) {
        this.message = message;
        return this;
    }

    public CountdownBuilder broadcastAt(final int amount, final TimeUnit unit) {
        this.broadcastAt.add((int) unit.toSeconds(amount));
        return this;
    }

    public CountdownBuilder onTick(final Runnable tickHandler) {
        this.tickHandler = tickHandler;
        return this;
    }

    public CountdownBuilder onBroadcast(final Runnable broadcastHandler) {
        this.broadcastHandler = broadcastHandler;
        return this;
    }

    public CountdownBuilder onFinish(final Runnable finishHandler) {
        this.finishHandler = finishHandler;
        return this;
    }

    public CountdownBuilder withMessageFilter(final Predicate<Player> messageFilter) {
        this.messageFilter = messageFilter;
        return this;
    }

    public CountdownBuilder players(final List<Player> playerList) {
        this.playerList = playerList;
        return this;
    }

    public Countdown start() {
       // Preconditions.checkNotNull((Object) this.message, "Message cannot be null!");
        if (this.broadcastAt.isEmpty()) {
            this.broadcastAt(10, TimeUnit.MINUTES);
            this.broadcastAt(5, TimeUnit.MINUTES);
            this.broadcastAt(4, TimeUnit.MINUTES);
            this.broadcastAt(3, TimeUnit.MINUTES);
            this.broadcastAt(2, TimeUnit.MINUTES);
            this.broadcastAt(1, TimeUnit.MINUTES);
            this.broadcastAt(30, TimeUnit.SECONDS);
            this.broadcastAt(15, TimeUnit.SECONDS);
            this.broadcastAt(10, TimeUnit.SECONDS);
            this.broadcastAt(5, TimeUnit.SECONDS);
            this.broadcastAt(4, TimeUnit.SECONDS);
            this.broadcastAt(3, TimeUnit.SECONDS);
            this.broadcastAt(2, TimeUnit.SECONDS);
            this.broadcastAt(1, TimeUnit.SECONDS);
        }
        return new Countdown(this.seconds, this.message, this.tickHandler, this.broadcastHandler, this.finishHandler, this.messageFilter, playerList, this.convertIntegers(this.broadcastAt));
    }

    private int[] convertIntegers(final List<Integer> integers) {
        final int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = integers.get(i);
        }
        return ret;
    }
}
