package rip.crystal.practice.visual.tablist.impl;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.playerversion.PlayerVersion;
import rip.crystal.practice.utilities.playerversion.PlayerVersionHandler;
import rip.crystal.practice.visual.tablist.impl.utils.*;
import rip.crystal.practice.visual.tablist.impl.utils.impl.ProtocolLibTabImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GhostlyTablist {

    private final Player player;
    private final Scoreboard scoreboard;

    private final Set<TabEntry> currentEntries = ConcurrentHashMap.newKeySet();
    private final TabList instance;

    public GhostlyTablist(Player player, TabList instance) {
        this.player = player;
        this.instance = instance;

        if (instance.isHook() || !(player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard())) {
            this.scoreboard = player.getScoreboard();
        } else {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        player.setScoreboard(this.scoreboard);

        GhostlyTablist localTab = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                setup();
                if (PlayerUtil.getPlayerVersion(player) == PlayerVersion.v1_7) {
                    for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                        getInstance().getImplementation().destoryFakePlayer(localTab, new TabEntry(
                                loopPlayer.getName(),
                                loopPlayer.getUniqueId(),
                                loopPlayer.getName(),
                                localTab,
                                TabListCommons.defaultTexture,
                                TabColumn.LEFT,
                                1,
                                1,
                                1
                        ), loopPlayer.getName());
                        if (player.canSee(loopPlayer)) {
                            instance.getImplementation().recreatePlayer(localTab, loopPlayer);
                        }
                    }
                }
            }
        }.runTaskAsynchronously(instance.getPlugin());
    }

    private void setup() {
        final int possibleSlots = (PlayerUtil.getPlayerVersion(player) == PlayerVersion.v1_7 ? 60 : 80);
        for (int i = 1; i <= possibleSlots; i++) {
            if (this.scoreboard == null || this.scoreboard != player.getScoreboard()) continue;
            final TabColumn tabColumn = TabColumn.getFromSlot(player, i);
            if (tabColumn == null) continue;
            TabEntry tabEntry = instance.getImplementation().createFakePlayer(
                    this,
                    "0" + (i > 9 ? i : "0" + i) + "|Tab",
                    tabColumn,
                    tabColumn.getNumb(player, i),
                    i
            );
            if (PlayerVersionHandler.version.getPlayerVersion(player) == PlayerVersion.v1_7) {
                Team team = player.getScoreboard().getTeam(LegacyClientUtils.teamNames.get(i - 1));
                if (team != null) {
                    team.unregister();
                }
                team = player.getScoreboard().registerNewTeam(LegacyClientUtils.teamNames.get(i - 1));
                team.setPrefix("");
                team.setSuffix("");

                team.addEntry(LegacyClientUtils.tabEntrys.get(i - 1));
            }
            currentEntries.add(tabEntry);
        }
    }

    public void cleanup() {
        for (TabEntry tabEntry : getCurrentEntries()) {
            if (PlayerVersionHandler.version.getPlayerVersion(player) == PlayerVersion.v1_7) {
                Team team = player.getScoreboard().getTeam(LegacyClientUtils.teamNames.get(tabEntry.getRawSlot() - 1));
                if (team != null) {
                    team.unregister();
                }
            }
            boolean skip = false;
            if (instance.getImplementation() instanceof ProtocolLibTabImpl) {
                if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null
                        || !Bukkit.getPluginManager().getPlugin("ProtocolLib").isEnabled()
                        || ProtocolLibrary.getProtocolManager() == null) {
                    skip = true;
                }
            }
            if (!skip) {
                getInstance().getImplementation().destoryFakePlayer(this, tabEntry, null);
            }
        }
        if (!getInstance().isHook()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    public void update() {
        Set<TabEntry> previous = new HashSet<>(currentEntries);
        Set<BufferedTabObject> processedObjects = instance.getAdapter().getSlots(player);

        if (instance.getAdapter().getSlots(player) == null) {
            processedObjects = new HashSet<>();
        }

        for (BufferedTabObject scoreObject : processedObjects) {
            TabEntry tabEntry = getEntry(scoreObject.getColumn(), scoreObject.getSlot());
            if (tabEntry != null) {
                previous.remove(tabEntry);
                updateTabEntry(tabEntry, scoreObject);
            }
        }

        for (TabEntry tabEntry : previous) {
            updateTabEntry(tabEntry, null);
        }

        previous.clear();

        //Footer and Header
        if ((PlayerUtil.getPlayerVersion(player) != PlayerVersion.v1_7) && (instance.getAdapter().getHeader() != null || instance.getAdapter().getFooter() != null)) {
            String header = instance.getAdapter().getHeader() == null ? "" : instance.getAdapter().getHeader();
            String footer = instance.getAdapter().getFooter() == null ? "" : instance.getAdapter().getFooter();
            instance.getImplementation().updateHeaderAndFooter(this, header, footer);
        }

        if (player.getScoreboard() != scoreboard && !getInstance().isHook()) {
            player.setScoreboard(scoreboard);
        }
    }

    private void updateTabEntry(TabEntry tabEntry, BufferedTabObject tabObject) {
        if (PlayerUtil.getPlayerVersion(player) != PlayerVersion.v1_7) {
            updateFakeSkin(tabEntry, tabObject == null ?
                    TabListCommons.defaultTexture :
                    tabObject.getSkinTexture());
        }
        updateName(tabEntry, tabObject == null ? "" : tabObject.getText());
        updateLatency(tabEntry, tabObject == null || tabObject.getPing() == null ? getInstance().getPingProvider().getDefaultPing(player) : tabObject.getPing());
    }

    private void updateFakeSkin(TabEntry tabEntry, SkinTexture skinTexture) {
        if (skinTexture == null || tabEntry.getTexture().toString().equals(skinTexture.toString())) return;
        instance.getImplementation().updateFakeSkin(this, tabEntry, skinTexture);
    }

    private void updateLatency(TabEntry tabEntry, int latency) {
        if (tabEntry.getLatency() == latency) {
            return;
        }
        instance.getImplementation().updateFakeLatency(this, tabEntry, latency);
    }

    private void updateName(TabEntry tabEntry, String string) {
        if (tabEntry.getText().equals(string)) return;
        instance.getImplementation().updateFakeName(this, tabEntry, string);
    }

    public TabEntry getEntry(TabColumn column, Integer slot) {
        for (TabEntry entry : currentEntries) {
            if (entry.getColumn().name().equalsIgnoreCase(column.name()) && entry.getSlot() == slot) {
                return entry;
            }
        }
        return null;
    }

    public static String[] splitStrings(String text) {
        if (text.length() <= 16) {
            return new String[]{text, ""};
        }

        String prefix = text.substring(0, 16);
        String suffix;

        if (prefix.charAt(15) == ChatColor.COLOR_CHAR || prefix.charAt(15) == '&') {
            prefix = prefix.substring(0, 15);
            suffix = text.substring(15);
        } else if (prefix.charAt(14) == ChatColor.COLOR_CHAR || prefix.charAt(14) == '&') {
            prefix = prefix.substring(0, 14);
            suffix = text.substring(14);
        } else {
            suffix = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', prefix)) + text.substring(16);
        }

        if (suffix.length() > 16) {
            suffix = suffix.substring(0, 16);
        }

        return new String[]{prefix, suffix};
    }
}
