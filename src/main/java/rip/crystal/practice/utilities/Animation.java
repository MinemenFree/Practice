package rip.crystal.practice.utilities;

import rip.crystal.practice.cPractice;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Animation {

    public static String title, footer;

    public static void init() {
        List<String> titles = cPractice.get().getScoreboardConfig().getStringList("TITLE");
        AtomicInteger p = new AtomicInteger();
        TaskUtil.runTimerAsync(() -> {
            if (p.get() == titles.size()) p.set(0);
            title = titles.get(p.getAndIncrement());
        }, 0L, (long) (cPractice.get().getScoreboardConfig().getDouble("TITLE-TASK") * 20L));

        if (cPractice.get().getScoreboardConfig().getBoolean("FOOTER-ENABLED")) {
            List<String> footers = cPractice.get().getScoreboardConfig().getStringList("FOOTER");
            AtomicInteger b = new AtomicInteger();
            TaskUtil.runTimerAsync(() -> {
                if (b.get() == footers.size()) b.set(0);
                footer = footers.get(b.getAndIncrement());
            }, 0L, (long) (cPractice.get().getScoreboardConfig().getDouble("FOOTER-TASK") * 20L));
        }
    }

    public static String getScoreboardTitle() {
        return title;
    }

    public static String getScoreboardFooter() {
        return footer;
    }

}
