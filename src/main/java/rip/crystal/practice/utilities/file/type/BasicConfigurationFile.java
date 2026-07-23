package rip.crystal.practice.utilities.file.type;

import com.google.common.io.Files;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.file.AbstractConfigurationFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BasicConfigurationFile extends AbstractConfigurationFile {

    /** Stands in for a cached miss, because ConcurrentHashMap rejects null values. */
    private static final Object NULL = new Object();

    private final File file;
    private final YamlConfiguration configuration;

    /**
     * Read caching. Only safe for files that are never written through
     * {@link #getConfiguration()} at runtime, so it is opt-in per file.
     * The scoreboard adapter re-reads its whole layout for every player 10x a second,
     * which is what this exists for. Caches are dropped on {@link #reload()}.
     */
    private final boolean cached;
    private final Map<String, Object> stringCache = new ConcurrentHashMap<>();
    private final Map<String, Object> listCache = new ConcurrentHashMap<>();
    private final Map<String, Boolean> booleanCache = new ConcurrentHashMap<>();

    public BasicConfigurationFile(final JavaPlugin plugin, final String name, final boolean overwrite,
                                  final boolean cached) {
        super(plugin, name);
        this.cached = cached;
        this.file = new File(plugin.getDataFolder(), name + FILE_EXTENSION);
        plugin.saveResource(name + ".yml", overwrite);
        configuration = new YamlConfiguration ();
        try {
            configuration.loadFromString (Files.toString (file, StandardCharsets.UTF_8));
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public BasicConfigurationFile(final JavaPlugin plugin, final String name, final boolean overwrite) {
        this(plugin, name, overwrite, false);
    }

    public BasicConfigurationFile(final JavaPlugin plugin, final String name) {
        this(plugin, name, false, false);
    }

    private String readString(final String path) {
        if (this.configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return null;
    }

    @Override
    public String getString(final String path) {
        if (!this.cached) return readString(path);

        Object value = this.stringCache.get(path);
        if (value == null) {
            String read = readString(path);
            this.stringCache.put(path, read == null ? NULL : read);
            return read;
        }
        return value == NULL ? null : (String) value;
    }

    @Override
    public String getStringOrDefault(final String path, final String or) {
        final String toReturn = this.getString(path);
        return (toReturn == null) ? or : toReturn;
    }

    @Override
    public int getInteger(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getInt(path);
        }
        return 0;
    }

    public boolean getBoolean(final String path) {
        if (!this.cached) {
            return this.configuration.contains(path) && this.configuration.getBoolean(path);
        }

        Boolean value = this.booleanCache.get(path);
        if (value == null) {
            value = this.configuration.contains(path) && this.configuration.getBoolean(path);
            this.booleanCache.put(path, value);
        }
        return value;
    }

    @Override
    public double getDouble(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getDouble(path);
        }
        return 0.0;
    }

    @Override
    public long getLong(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getLong(path);
        }
        return 0;
    }

    @Override
    public Object get(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.get(path);
        }
        return null;
    }

    @Override
    public List<String> getStringList(final String path) {
        if (!this.cached) {
            if (this.configuration.contains(path)) {
                return this.configuration.getStringList(path);
            }
            return null;
        }

        Object value = this.listCache.get(path);
        if (value == null) {
            List<String> read = this.configuration.contains(path) ? this.configuration.getStringList(path) : null;
            this.listCache.put(path, read == null ? NULL : Collections.unmodifiableList(read));
            value = read == null ? NULL : this.listCache.get(path);
        }

        if (value == NULL) return null;

        // Callers are free to mutate what they get back, so hand out a copy.
        @SuppressWarnings("unchecked")
        List<String> hit = (List<String>) value;
        return new ArrayList<>(hit);
    }

    public void reload() {
        File file = new File(cPractice.get().getDataFolder(), getName() + ".yml");
        try {
            getConfiguration().load(file);
            getConfiguration().save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        this.stringCache.clear();
        this.listCache.clear();
        this.booleanCache.clear();
    }

    public void save() {
        File folder = cPractice.get().getDataFolder();
        try {
            getConfiguration().save(new File(folder, getName() + ".yml"));
        } catch (Exception ignored) {
        }
    }

    public File getFile() {
        return this.file;
    }

    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }
}