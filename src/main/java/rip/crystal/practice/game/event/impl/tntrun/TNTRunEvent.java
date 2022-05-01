package rip.crystal.practice.game.event.impl.tntrun;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.event.Event;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.EventGameLogic;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.LocationUtil;
import rip.crystal.practice.utilities.Test;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TNTRunEvent implements Event {

    @Setter private Location lobbyLocation;
    @Getter private final List<String> allowedMaps;
    @Getter private final Map<Location, Test> changedBlocks;

    public TNTRunEvent() {
        BasicConfigurationFile config = cPractice.get().getEventsConfig();

        lobbyLocation = LocationUtil.deserialize(config.getString("EVENTS.TNTRUN.LOBBY_LOCATION"));

        allowedMaps = new ArrayList<>();
        allowedMaps.addAll(config.getStringList("EVENTS.TNTRUN.ALLOWED_MAPS"));
        this.changedBlocks = Maps.newHashMap();
    }

    @Override
    public String getName() {
        return "TNTRun";
    }

    @Override
    public String getDisplayName() {
        return CC.translate(cPractice.get().getEventsConfig().getString("EVENTS.TNTRUN.DISPLAYNAME"));
    }

    @Override
    public List<String> getDescription() {
        return cPractice.get().getEventsConfig().getStringList("EVENTS.TNTRUN.DESCRIPTION");
    }

    @Override
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.valueOf(cPractice.get().getEventsConfig().getString("EVENTS.TNTRUN.ICON"))).build();
    }

    @Override
    public boolean canHost(Player player) {
        return player.hasPermission("cpractice.event.host.tntrun");
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.emptyList();
    }

    @Override
    public List<Object> getCommands() {
        return Collections.emptyList();
    }

    @Override
    public EventGameLogic start(EventGame game) {
        return new TNTRunGameLogic(game);
    }

    @Override
    public void save() {
        FileConfiguration config = cPractice.get().getEventsConfig().getConfiguration();
        config.set("EVENTS.TNTRUN.LOBBY_LOCATION", LocationUtil.serialize(lobbyLocation));
        config.set("EVENTS.TNTRUN.ALLOWED_MAPS", allowedMaps);

        try {
            config.save(cPractice.get().getEventsConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}