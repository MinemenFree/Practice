package rip.crystal.practice.essentials.abilities.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

public class DurationFormatter {

    private static final long MINUTE;
    private static final long HOUR;
    
    static {
        MINUTE = TimeUnit.MINUTES.toMillis(1L);
        HOUR = TimeUnit.HOURS.toMillis(1L);
    }
    
    public static String getRemaining(long millis, boolean milliseconds) {
        return getRemaining(millis, milliseconds, true);
    }
    
    public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < DurationFormatter.MINUTE) {
            return String.valueOf((trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(duration * 0.001)) + 's';
        }
        return DurationFormatUtils.formatDuration(duration, ((duration >= DurationFormatter.HOUR) ? "HH:" : "") + "mm:ss");
    }
}
