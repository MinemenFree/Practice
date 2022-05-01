package rip.crystal.practice.game.event.game;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.game.event.game.map.EventGameMap;

import java.util.List;

public interface EventGameLogic {

	EventGameLogicTask getGameLogicTask();

	void startEvent();

	boolean canStartEvent();

	void preEndEvent();

	void endEvent();

	boolean canEndEvent();

	void cancelEvent();

	void preStartRound();

	void startRound();

	boolean canStartRound();

	void endRound();

	boolean canEndRound();

	void onVote(Player player, EventGameMap gameMap);

	void onJoin(Player player);

	void onLeave(Player player);

	void onMove(Player player);

	void onDeath(Player player, Player killer);

	void onInteract(PlayerInteractEvent event, Player player, ItemStack target);

	void onEntityDamageByPlayer(EntityDamageByEntityEvent event, Player player, Player target);

	void onEntityDamage(EntityDamageEvent event, Player player);

	void onInventoryClick(InventoryClickEvent event, Player player);

	boolean isPlaying(Player player);

	List<String> getScoreboardEntries();

	int getRoundNumber();

}
