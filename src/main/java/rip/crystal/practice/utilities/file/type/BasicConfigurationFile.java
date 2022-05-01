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
import java.util.List;

public class BasicConfigurationFile extends AbstractConfigurationFile {

    private final File file;
    private final YamlConfiguration configuration;

    public BasicConfigurationFile(final JavaPlugin plugin, final String name, final boolean overwrite) {
        super(plugin, name);
        this.file = new File(plugin.getDataFolder(), name + FILE_EXTENSION);
        plugin.saveResource(name + ".yml", overwrite);
        configuration = new YamlConfiguration ();
        try {
            configuration.loadFromString (Files.toString (file, StandardCharsets.UTF_8));
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public BasicConfigurationFile(final JavaPlugin plugin, final String name) {
        this(plugin, name, false);
    }

    @Override
    public String getString(final String path) {
        if (this.configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return null;
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
        return this.configuration.contains(path) && this.configuration.getBoolean(path);
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
        if (this.configuration.contains(path)) {
            return this.configuration.getStringList(path);
        }
        return null;
    }

    public void reload() {
        File file = new File(cPractice.get().getDataFolder(), getName() + ".yml");
        try {
            getConfiguration().load(file);
            getConfiguration().save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
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