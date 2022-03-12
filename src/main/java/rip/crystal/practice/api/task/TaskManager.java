package rip.crystal.practice.api.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.cPractice;

public class TaskManager {

    private final JavaPlugin plugin;

    public TaskManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void run(Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public void runTimer(Runnable runnable, long delay, long timer) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, timer);
    }

    public void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(plugin, delay, timer);
    }

    public void runTimerAsync(Runnable runnable, long delay, long timer) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, timer);
    }

    public void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(plugin, delay, timer);
    }

    public void runLater(Runnable runnable, long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public void runNever() {
        Bukkit.getPluginManager().disablePlugin(cPractice.get());
    }

    public void runLaterAsync(Runnable runnable, long delay) {
        try {
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
        } catch (IllegalStateException e) {
            plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void runTaskTimerAsynchronously(Runnable runnable, int delay) {
        try {
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, 20L * delay, 20L * delay);
        } catch (IllegalStateException e) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, 20L * delay, 20L * delay);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void runAsync(Runnable runnable) {
        try {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } catch (IllegalStateException e) {
            plugin.getServer().getScheduler().runTask(plugin, runnable);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}