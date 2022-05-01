package rip.crystal.practice.utilities;

import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.cPractice;

public class TaskUtils {

    public static void run(Runnable runnable) {
        cPractice.get().getServer().getScheduler().runTask(cPractice.get(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        cPractice.get().getServer().getScheduler().runTaskTimer(cPractice.get(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(cPractice.get(), delay, timer);
    }

    public static void runTimerAsync(Runnable runnable, long delay, long timer) {
        cPractice.get().getServer().getScheduler().runTaskTimerAsynchronously(cPractice.get(), runnable, delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(cPractice.get(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        cPractice.get().getServer().getScheduler().runTaskLater(cPractice.get(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        try {
            cPractice.get().getServer().getScheduler().runTaskLaterAsynchronously(cPractice.get(), runnable, delay);
        }
        catch (IllegalStateException e) {
            cPractice.get().getServer().getScheduler().runTaskLater(cPractice.get(), runnable, delay);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runTaskTimerAsynchronously(Runnable runnable, int delay) {
        try {
            cPractice.get().getServer().getScheduler().runTaskTimerAsynchronously(cPractice.get(), runnable, 20L * delay, 20L * delay);
        }
        catch (IllegalStateException e) {
            cPractice.get().getServer().getScheduler().runTaskTimer(cPractice.get(), runnable, 20L * delay, 20L * delay);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runAsync(Runnable runnable) {
        try {
            cPractice.get().getServer().getScheduler().runTaskAsynchronously(cPractice.get(), runnable);
        }
        catch (IllegalStateException e) {
            cPractice.get().getServer().getScheduler().runTask(cPractice.get(), runnable);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}