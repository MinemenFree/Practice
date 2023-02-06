package rip.crystal.practice.utilities.file.language;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.file.AbstractConfigurationFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LanguageConfigurationFile extends AbstractConfigurationFile {

    private static final Lang DEFAULT_LOCALE;
    private final Map<Lang, YamlConfiguration> configurations;
    
    public LanguageConfigurationFile(JavaPlugin plugin, String name, boolean overwrite) {
        super(plugin, name);
        this.configurations = new HashMap<>();
        for (Lang locale : Lang.values()) {
            File file = new File(plugin.getDataFolder(), name + "_" + locale.getAbbreviation() + ".yml");
            String path = name + "_" + locale.getAbbreviation() + ".yml";
            if (plugin.getResource(path) != null) {
                plugin.saveResource(path, overwrite);
                this.configurations.put(locale, YamlConfiguration.loadConfiguration(file));
            }
        }
    }
    
    public LanguageConfigurationFile(JavaPlugin plugin, String name) {
        this(plugin, name, false);
    }
    
    public List<String> replace(List<String> list, int position, Object argument) {
        List<String> toReturn = new ArrayList<>();
        for (String string : list) {
            toReturn.add(string.replace("{" + position + "}", argument.toString()));
        }
        return toReturn;
    }
    
    public List<String> replace(List<String> list, int position, Object... arguments) {
        return this.replace(list, 0, position, arguments);
    }
    
    public List<String> replace(List<String> list, int index, int position, Object... arguments) {
        List<String> toReturn = new ArrayList<>();
        for (String string : list) {
            for (int i = 0; i < arguments.length; ++i) {
                toReturn.add(string.replace("{" + position + "}", arguments[index + i].toString()));
            }
        }
        return toReturn;
    }
    
    public List<String> getStringListWithArgumentsOrRemove(String path, Lang locale, Object... arguments) {
        List<String> toReturn = new ArrayList<>();
        Label_0022:
        for (String string : this.getStringList(path, locale)) {
            for (int i = 0; i < arguments.length; ++i) {
                if (string.contains("{" + i + "}")) {
                    Object object = arguments[i];
                    if (object == null) {
                        continue Label_0022;
                    }
                    if (object instanceof List) {
                        for (Object obj : (List)object) {
                            if (obj instanceof String) {
                                toReturn.add((String)obj);
                            }
                        }
                        continue Label_0022;
                    }
                    string = string.replace("{" + i + "}", object.toString());
                }
            }
            toReturn.add(string);
        }
        return toReturn;
    }
    
    public int indexOf(List<String> list, int position) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).contains("{" + position + "}")) {
                return i;
            }
        }
        return -1;
    }
    
    public String getString(String path, Lang locale) {
        if (!this.configurations.containsKey(locale)) {
            return (locale == LanguageConfigurationFile.DEFAULT_LOCALE) ? null : this.getString(path, LanguageConfigurationFile.DEFAULT_LOCALE);
        }
        YamlConfiguration configuration = this.configurations.get(locale);
        if (configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', configuration.getString(path));
        }
        return null;
    }
    
    public String getString(String path, Lang locale, Object... arguments) {
        String toReturn = this.getString(path, locale);
        if (toReturn != null) {
            for (int i = 0; i < arguments.length; ++i) {
                toReturn = toReturn.replace("{" + i + "}", arguments[i].toString());
            }
            return toReturn;
        }
        return null;
    }
    
    @Override
    public String getString(String path) {
        return this.getString(path, LanguageConfigurationFile.DEFAULT_LOCALE);
    }
    
    public String getStringOrDefault(String path, String or, Lang locale) {
        String toReturn = this.getString(path, locale);
        if (toReturn == null) {
            return or;
        }
        return toReturn;
    }
    
    @Override
    public String getStringOrDefault(String path, String or) {
        return this.getStringOrDefault(path, or, LanguageConfigurationFile.DEFAULT_LOCALE);
    }

    @Override
    public int getInteger(String path) {
        throw new UnsupportedOperationException("");
    }

    @Deprecated
    @Override
    public double getDouble(String path) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public long getLong(String path) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public Object get(String path) {
        return get(path, Lang.ENGLISH);
    }

    public Object get(String path, Lang locale) {
        if (!this.configurations.containsKey(locale)) {
            return (locale == LanguageConfigurationFile.DEFAULT_LOCALE) ? null :
                this.get(path, LanguageConfigurationFile.DEFAULT_LOCALE);
        }
        YamlConfiguration configuration = this.configurations.get(locale);
        if (configuration.contains(path)) {
            return configuration.get(path);
        }
        return null;
    }

    public List<String> getStringList(String path, Lang locale, Object... arguments) {
        List<String> toReturn = new ArrayList<>();
        Label_0022:
        for (String line : this.getStringList(path, locale)) {
            for (int i = 0; i < arguments.length; ++i) {
                Object object = arguments[i];
                if (object instanceof List && line.contains("{" + i + "}")) {
                    for (Object obj : (List)object) {
                        if (obj instanceof String) {
                            toReturn.add(line.replace("{" + i + "}", "") + obj);
                        }
                    }
                    continue Label_0022;
                }
                line = line.replace("{" + i + "}", arguments[i].toString());
            }
            toReturn.add(line);
        }
        return toReturn;
    }
    
    public List<String> getStringList(String path, Lang locale) {
        if (!this.configurations.containsKey(locale)) {
            return (locale == LanguageConfigurationFile.DEFAULT_LOCALE) ? null : this.getStringList(path, LanguageConfigurationFile.DEFAULT_LOCALE);
        }
        YamlConfiguration configuration = this.configurations.get(locale);
        if (configuration.contains(path)) {
            List<String> toReturn = new ArrayList<>();
            for (String string : configuration.getStringList(path)) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return toReturn;
        }
        return Collections.emptyList();
    }


    public void reload() {
        getConfigurations().forEach((type, configuration) -> {
            File file = new File(cPractice.get().getDataFolder(),
                "lang/lang" + "_" + type.getAbbreviation() + ".yml");
            try {
                configuration.load(file);
                configuration.save(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public List<String> getStringList(String path) {
        return this.getStringList(path, LanguageConfigurationFile.DEFAULT_LOCALE);
    }
    
    public Map<Lang, YamlConfiguration> getConfigurations() {
        return this.configurations;
    }
    
    static {
        DEFAULT_LOCALE = Lang.ENGLISH;
    }
}
