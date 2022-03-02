package rip.crystal.practice.utilities;

public class TimeUtils {

    public static String formatIntoDetailedString(final int secs) {
        if (secs == 0) {
            return "0 seconds";
        }
        final int remainder = secs % 86400;
        final int days = secs / 86400;
        final int hours = remainder / 3600;
        final int minutes = remainder / 60 - hours * 60;
        final int seconds = remainder % 3600 - minutes * 60;
        final String fDays = (days > 0) ? (" " + days + " day" + ((days > 1) ? "s" : "")) : "";
        final String fHours = (hours > 0) ? (" " + hours + " hour" + ((hours > 1) ? "s" : "")) : "";
        final String fMinutes = (minutes > 0) ? (" " + minutes + " minute" + ((minutes > 1) ? "s" : "")) : "";
        final String fSeconds = (seconds > 0) ? (" " + seconds + " second" + ((seconds > 1) ? "s" : "")) : "";
        return (fDays + fHours + fMinutes + fSeconds).trim();
    }

    public static String formatLongIntoDetailedString(final long secs) {
        final int unconvertedSeconds = (int)secs;
        return formatIntoDetailedString(unconvertedSeconds);
    }

}
