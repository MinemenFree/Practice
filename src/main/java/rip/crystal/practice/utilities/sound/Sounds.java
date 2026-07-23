package rip.crystal.practice.utilities.sound;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads {@code settings/sounds.yml} once at startup and resolves every entry to a real
 * {@link Sound} up front. Playing a sound is then a map lookup instead of a config read,
 * which matters because these fire per player on match start/end.
 */
public final class Sounds {

    private static final Map<String, Entry> ENTRIES = new HashMap<>();

    private Sounds() {
    }

    public static void init() {
        ENTRIES.clear();

        BasicConfigurationFile config = cPractice.get().getSoundsConfig();
        if (config == null) return;

        ConfigurationSection root = config.getConfiguration().getRoot();
        if (root == null) return;

        for (String category : root.getKeys(false)) {
            ConfigurationSection categorySection = root.getConfigurationSection(category);
            if (categorySection == null) continue;

            for (String name : categorySection.getKeys(false)) {
                ConfigurationSection section = categorySection.getConfigurationSection(name);
                if (section == null) continue;

                String key = category + "." + name;

                if (!section.getBoolean("enabled", true)) continue;

                String soundName = section.getString("sound");
                if (soundName == null) continue;

                Sound sound;
                try {
                    sound = Sound.valueOf(soundName.toUpperCase());
                } catch (IllegalArgumentException exception) {
                    cPractice.get().getLogger().warning(
                        "sounds.yml: '" + soundName + "' (at " + key + ") is not a valid 1.8 sound name - skipping it.");
                    continue;
                }

                ENTRIES.put(key, new Entry(
                    sound,
                    (float) section.getDouble("volume", 1.0D),
                    (float) section.getDouble("pitch", 1.0D)));
            }
        }
    }

    /**
     * Plays a configured sound at the player's own location. A missing, disabled or invalid
     * entry is a no-op.
     *
     * @param key category and name joined by a dot, e.g. {@code match-result.match-win}
     */
    public static void play(Player player, String key) {
        if (player == null) return;

        Entry entry = ENTRIES.get(key);
        if (entry == null) return;

        player.playSound(player.getLocation(), entry.sound, entry.volume, entry.pitch);
    }

    public static void play(Iterable<Player> players, String key) {
        Entry entry = ENTRIES.get(key);
        if (entry == null) return;

        for (Player player : players) {
            if (player == null) continue;
            player.playSound(player.getLocation(), entry.sound, entry.volume, entry.pitch);
        }
    }

    /** Exposed for {@code /cpractice reload}. */
    public static Map<String, Entry> getEntries() {
        return Collections.unmodifiableMap(ENTRIES);
    }

    public static final class Entry {
        private final Sound sound;
        private final float volume;
        private final float pitch;

        Entry(Sound sound, float volume, float pitch) {
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
        }

        public Sound getSound() {
            return sound;
        }

        public float getVolume() {
            return volume;
        }

        public float getPitch() {
            return pitch;
        }
    }
}
