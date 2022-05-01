package rip.crystal.practice.game.arena;

import org.bukkit.Difficulty;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.game.arena.selection.Selection;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchState;
import rip.crystal.practice.utilities.chat.CC;

public class ArenaListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (!(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		ItemStack item = event.getItem();

		if (item != null && item.equals(Selection.SELECTION_WAND)) {
			Player player = event.getPlayer();
			Block clicked = event.getClickedBlock();
			int location = 0;

			Selection selection = Selection.createOrGetSelection(player);

			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				selection.setPoint2(clicked.getLocation());
				location = 2;
			} else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				selection.setPoint1(clicked.getLocation());
				location = 1;
			}

			event.setCancelled(true);
			event.setUseItemInHand(Event.Result.DENY);
			event.setUseInteractedBlock(Event.Result.DENY);

			String message = CC.AQUA + (location == 1 ? "First" : "Second") +
			                 " location " + CC.YELLOW + "(" + CC.GREEN +
			                 clicked.getX() + CC.YELLOW + ", " + CC.GREEN +
			                 clicked.getY() + CC.YELLOW + ", " + CC.GREEN +
			                 clicked.getZ() + CC.YELLOW + ")" + CC.AQUA + " has been set!";

			if (selection.isFullObject()) {
				message += CC.RED + " (" + CC.YELLOW + selection.getCuboid().volume() + CC.AQUA + " blocks" +
				           CC.RED + ")";
			}

			player.sendMessage(message);
		}
	}

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		if (event.getToBlock() == null) {
			return;
		}

		final int x = event.getBlock().getX();
		final int y = event.getBlock().getY();
		final int z = event.getBlock().getZ();

		Arena foundArena = null;

		for (Arena arena : Arena.getArenas()) {
			if (!(arena.getType() == ArenaType.STANDALONE || arena.getType() == ArenaType.DUPLICATE)) {
				continue;
			}

			if (!arena.isActive()) continue;

			if (x >= arena.getX1() && x <= arena.getX2() && y >= arena.getY1() && y <= arena.getY2() &&
			    z >= arena.getZ1() && z <= arena.getZ2()) {
			//if (event.getToBlock().getX() >= minX && event.getToBlock().getZ() >= minZ
					//&& event.getToBlock().getX() <= maxX && event.getToBlock().getZ() <= maxZ) {

				foundArena = arena;
				break;
			}
		}

		if (foundArena == null) return;

		for (Match match : Match.getMatches()) {
			if (match.getArena().equals(foundArena)) {
				if (match.getState() == MatchState.PLAYING_ROUND) {
					match.getPlacedBlocks().add(event.getToBlock().getLocation());
					match.getChangedBlocks().add(event.getBlock().getState());
				}
				break;
			}
		}
	}

	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		event.getWorld().getEntities().clear();
		event.getWorld().setDifficulty(Difficulty.HARD);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState()) event.setCancelled(true);
	}

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onHangingBreak(HangingBreakEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPrime(ExplosionPrimeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onUnloadChunk(ChunkUnloadEvent event) {
		event.setCancelled(true);
	}

}
