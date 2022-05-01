package rip.crystal.practice.game.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.EventGameLogic;
import rip.crystal.practice.game.event.impl.brackets.BracketsEvent;
import rip.crystal.practice.game.event.impl.gulag.GulagEvent;
import rip.crystal.practice.game.event.impl.spleef.SpleefEvent;
import rip.crystal.practice.game.event.impl.sumo.SumoEvent;
import rip.crystal.practice.game.event.impl.tntrun.TNTRunEvent;
import rip.crystal.practice.game.event.impl.tnttag.TNTTagEvent;

import java.util.ArrayList;
import java.util.List;

public interface Event {

	List<Event> events = new ArrayList<>();

	static void init() {
		add(new SumoEvent());
		add(new SpleefEvent());
		add(new TNTRunEvent());
		add(new GulagEvent());
		add(new BracketsEvent());
		add(new TNTTagEvent());
	}

	static void add(Event event) {
		events.add(event);
		for (Listener listener : event.getListeners()) {
			cPractice.get().getServer().getPluginManager().registerEvents(listener, cPractice.get());
		}
	}

	static <T extends Event> T getEvent(Class<? extends Event> clazz) {
		for (Event event : events) {
			if (event.getClass() == clazz) {
				return (T) clazz.cast(event);
			}
		}

		return null;
	}

	static Event getByName(String name) {
		return events.stream().filter(event1 -> event1.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	String getName();

	String getDisplayName();

	List<String> getDescription();

	Location getLobbyLocation();

	void setLobbyLocation(Location location);

	ItemStack getIcon();

	boolean canHost(Player player);

	List<String> getAllowedMaps();

	List<Listener> getListeners();

	default List<Object> getCommands() {
		return new ArrayList<>();
	}

	EventGameLogic start(EventGame game);

	void save();

}
