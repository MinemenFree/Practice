package rip.crystal.practice.utilities.file;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class AbstractConfigurationFile{
    public static final String FILE_EXTENSION = ".yml";
    private final JavaPlugin plugin;
    private final String name;
    
    public AbstractConfigurationFile(final JavaPlugin plugin, final String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public abstract String getString(final String p0);

    public abstract String getStringOrDefault(final String p0, final String p1);

    public abstract int getInteger(final String p0);

    public abstract double getDouble(final String p0);

    public abstract long getLong(final String p0);

    public abstract Object get(final String p0);

    public abstract List<String> getStringList(final String p0);

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public String getName() {
        return this.name;
    }
}
