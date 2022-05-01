package rip.crystal.practice.player.profile.menu.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends Menu {

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate(cPractice.get().getMenuConfig().getString("SETTINGS-INVENTORY.TITLE"));
    }

    @Override
    public int getSize() {
        return 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> hashMap = new HashMap<>();
        BasicConfigurationFile menuUtil = cPractice.get().getMenuConfig();
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.NEW-CONVERSATIONS.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.NEW-CONVERSATIONS.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.NEW-CONVERSATIONS.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.NEW-CONVERSATIONS.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.NEW-CONVERSATIONS.LORE"), "togglenewconversations", "receivingnewconversations"));
        }
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.ALLOW_SPECTATORS.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.ALLOW_SPECTATORS.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.ALLOW_SPECTATORS.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.ALLOW_SPECTATORS.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.ALLOW_SPECTATORS.LORE"), "togglespectators", "spectatorsAllowed"));
        }
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.TOGGLE_SCOREBOARD.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.TOGGLE_SCOREBOARD.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.TOGGLE_SCOREBOARD.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.TOGGLE_SCOREBOARD.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.TOGGLE_SCOREBOARD.LORE"), "togglescoreboard", "scoreboardToggled"));
        }
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.PUBLIC-CHAT.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.PUBLIC-CHAT.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.PUBLIC-CHAT.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.PUBLIC-CHAT.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.PUBLIC-CHAT.LORE"), "togglepublicchat", "publicChat"));
        }
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.VANILLA_TAB.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.VANILLA_TAB.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.VANILLA_TAB.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.VANILLA_TAB.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.VANILLA_TAB.LORE"), "togglevanillatab", "vanillaTab"));
        }
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.DEATH_EFFECT_SETTINGS.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.DEATH_EFFECT_SETTINGS.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.DEATH_EFFECT_SETTINGS.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.DEATH_EFFECT_SETTINGS.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.DEATH_EFFECT_SETTINGS.LORE"), "", "deathEffectsSettings"));
        }
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.MATCHMAKING_SETTINGS.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.MATCHMAKING_SETTINGS.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.MATCHMAKING_SETTINGS.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.MATCHMAKING_SETTINGS.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.MATCHMAKING_SETTINGS.LORE"), "", "matchmakingSettings"));
        }
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.MESSAGE-SOUNDS.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.MESSAGE-SOUNDS.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.MESSAGE-SOUNDS.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.MESSAGE-SOUNDS.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.MESSAGE-SOUNDS.LORE"), "togglemessagesounds", "playingmessagesounds"));
        }
        if (menuUtil.getBoolean("SETTINGS-INVENTORY.SETTINGS.DUEL_REQUEST.SHOW")) {
            hashMap.put(menuUtil.getInteger("SETTINGS-INVENTORY.SETTINGS.DUEL_REQUEST.SLOT"), new SettingUpdateButton(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.DUEL_REQUEST.NAME"), Material.valueOf(menuUtil.getString("SETTINGS-INVENTORY.SETTINGS.DUEL_REQUEST.ICON")), 0, menuUtil.getStringList("SETTINGS-INVENTORY.SETTINGS.DUEL_REQUEST.LORE"), "toggleduelrequest", "duelrequest"));
        }
        return hashMap;
    }
}

