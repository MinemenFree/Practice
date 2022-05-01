package rip.crystal.practice.match.listeners.impl;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.Locale;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchState;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.MessageFormat;

public class MatchBuildListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockFromEvent(BlockFromToEvent event) {
        Match.getMatches().forEach(match -> {
            Arena arena = match.getArena();
            int x = (int) event.getToBlock().getLocation().getX();
            int y = (int) event.getToBlock().getLocation().getY();
            int z = (int) event.getToBlock().getLocation().getZ();

            if (y > arena.getMaxBuildHeight()) {
                event.setCancelled(true);
                return;
            }

            if (arena instanceof StandaloneArena) {
                StandaloneArena standaloneArena = (StandaloneArena) arena;
                if (standaloneArena.getSpawnBlue() != null && standaloneArena.getSpawnBlue().contains(event.getToBlock())) {
                    event.setCancelled(true);
                    return;
                }
                if (standaloneArena.getSpawnRed() != null && standaloneArena.getSpawnRed().contains(event.getToBlock())) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (x >= arena.getX1() && x <= arena.getX2() && y >= arena.getY1() && y <= arena.getY2() &&
                    z >= arena.getZ1() && z <= arena.getZ2()) {
                match.getPlacedBlocks().add(event.getBlock().getLocation());
                Location down = event.getBlock().getLocation().subtract(0, 1, 0);
                if (down.getBlock().getType() == Material.GRASS) {
                    match.getChangedBlocks().add(down.getBlock().getState());
                }
            } else {
                event.setCancelled(true);
            }
        });
    }


    @EventHandler(ignoreCancelled=true)
    public void onBlockPlaceEvent(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();
            if ((match.getKit().getGameRules().isBuild() || match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())) && match.getState() == MatchState.PLAYING_ROUND || match.getState() == MatchState.STARTING_ROUND) {
                if (match.getKit().getGameRules().isSpleef()) {
                    blockPlaceEvent.setCancelled(true);
                    return;
                }
                Arena arena = match.getArena();
                int n = (int)blockPlaceEvent.getBlockPlaced().getLocation().getX();
                int n2 = (int)blockPlaceEvent.getBlockPlaced().getLocation().getY();
                int n3 = (int)blockPlaceEvent.getBlockPlaced().getLocation().getZ();

                if (n2 > arena.getMaxBuildHeight()) {
                    new MessageFormat(Locale.ARENA_REACHED_MAXIMUM.format(profile.getLocale())).send(player);
                    blockPlaceEvent.setCancelled(true);
                    return;
                }
                if (arena instanceof StandaloneArena) {
                    StandaloneArena standaloneArena = (StandaloneArena)arena;
                    if (standaloneArena.getSpawnBlue() != null && standaloneArena.getSpawnBlue().contains(blockPlaceEvent.getBlockPlaced())) {
                        blockPlaceEvent.setCancelled(true);
                        return;
                    }
                    if (standaloneArena.getSpawnRed() != null && standaloneArena.getSpawnRed().contains(blockPlaceEvent.getBlockPlaced())) {
                        blockPlaceEvent.setCancelled(true);
                        return;
                    }
                }
                if (arena.contains(n, n2, n3)) {
                    if (blockPlaceEvent.getBlockReplacedState() == null || blockPlaceEvent.getBlockReplacedState().getType() == Material.AIR) {
                        match.getPlacedBlocks().add(blockPlaceEvent.getBlock().getLocation());
                    } else {
                        match.getChangedBlocks().add(blockPlaceEvent.getBlockReplacedState());
                    }
                } else {
                    new MessageFormat(Locale.ARENA_BUILD_OUTSIDE.format(profile.getLocale())).send(player);
                    blockPlaceEvent.setCancelled(true);
                }
            } else {
                blockPlaceEvent.setCancelled(true);
            }
        } else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
            blockPlaceEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onBlockBreakEvent(BlockBreakEvent blockBreakEvent) {
        Player player = blockBreakEvent.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();
            if(match.getKit().getGameRules().isBedFight()) {
                return;
            }
            if ((match.getKit().getGameRules().isBuild() || match.getKit().getGameRules().isSpleef() || match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())) && match.getState() == MatchState.PLAYING_ROUND) {
                if (match.getKit().getGameRules().isSpleef()) {
                    if (blockBreakEvent.getBlock().getType() == Material.SNOW_BLOCK || blockBreakEvent.getBlock().getType() == Material.SNOW) {
                        match.getChangedBlocks().add(blockBreakEvent.getBlock().getState());
                        blockBreakEvent.getBlock().setType(Material.AIR);
                        player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 4));
                        player.updateInventory();
                    } else {
                        blockBreakEvent.setCancelled(true);
                    }
                } else if (match.getKit().getGameRules().isHcftrap()) {
                    if (match.getPlacedBlocks().contains(blockBreakEvent.getBlock().getLocation())) {
                        match.getPlacedBlocks().remove(blockBreakEvent.getBlock().getLocation());
                    } else if (match.getChangedBlocks().stream().noneMatch(blockState -> blockState.getLocation().equals(blockBreakEvent.getBlock().getLocation()))) {
                        match.getChangedBlocks().add(blockBreakEvent.getBlock().getState());
                    }
                    if (!blockBreakEvent.getBlock().getDrops().isEmpty()) {
                        match.getDroppedItems().add(blockBreakEvent.getBlock().getLocation().getWorld().dropItemNaturally(blockBreakEvent.getBlock().getLocation(), blockBreakEvent.getBlock().getDrops().stream().findFirst().orElse(null)));
                    }
                    blockBreakEvent.getBlock().setType(Material.AIR);
                    blockBreakEvent.setCancelled(true);
                } else if (!match.getPlacedBlocks().remove(blockBreakEvent.getBlock().getLocation())) {
                    blockBreakEvent.setCancelled(true);
                }
            } else {
                blockBreakEvent.setCancelled(true);
            }
        } else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
            blockBreakEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent playerBucketEmptyEvent) {
        Player player = playerBucketEmptyEvent.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();
            if ((match.getKit().getGameRules().isBuild() || match.getKit().getGameRules().isHcftrap() && ((BasicTeamMatch)match).getParticipantA().containsPlayer(player.getUniqueId())) && match.getState() == MatchState.PLAYING_ROUND) {
                Arena arena = match.getArena();
                Block block = playerBucketEmptyEvent.getBlockClicked().getRelative(playerBucketEmptyEvent.getBlockFace());
                int n = (int)block.getLocation().getX();
                int n2 = (int)block.getLocation().getY();
                int n3 = (int)block.getLocation().getZ();
                if (n2 > arena.getMaxBuildHeight()) {
                    new MessageFormat(Locale.ARENA_REACHED_MAXIMUM.format(profile.getLocale())).send(player);
                    playerBucketEmptyEvent.setCancelled(true);
                    return;
                }
                if (n >= arena.getX1() && n <= arena.getX2() && n2 >= arena.getY1() && n2 <= arena.getY2() && n3 >= arena.getZ1() && n3 <= arena.getZ2()) {
                    match.getPlacedBlocks().add(block.getLocation());
                } else {
                    playerBucketEmptyEvent.setCancelled(true);
                }
            } else {
                playerBucketEmptyEvent.setCancelled(true);
            }
        } else if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE || profile.getState() == ProfileState.SPECTATING) {
            playerBucketEmptyEvent.setCancelled(true);
        }
    }
}
