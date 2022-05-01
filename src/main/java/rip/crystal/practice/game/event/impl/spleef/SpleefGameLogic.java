package rip.crystal.practice.game.event.impl.spleef;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.EventGameLogic;
import rip.crystal.practice.game.event.game.EventGameLogicTask;
import rip.crystal.practice.game.event.game.EventGameState;
import rip.crystal.practice.game.event.game.map.EventGameMap;
import rip.crystal.practice.game.event.game.map.vote.EventGameMapVoteData;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.player.profile.hotbar.impl.HotbarItem;
import rip.crystal.practice.player.profile.participant.GamePlayer;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.profile.visibility.VisibilityLogic;
import rip.crystal.practice.utilities.BlockUtil;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpleefGameLogic implements EventGameLogic {

    private final EventGame game;
    @Getter private final List<GameParticipant<GamePlayer>> participants;
    @Getter private final EventGameLogicTask logicTask;
    private GameParticipant winningParticipant;

    SpleefGameLogic(EventGame game) {
        this.game = game;
        participants = game.getParticipants();
        this.logicTask = new EventGameLogicTask(game);
        this.logicTask.runTaskTimer(cPractice.get(), 0, 20L);
    }

    @Override
    public EventGameLogicTask getGameLogicTask() {
        return logicTask;
    }

    @Override
    public void startEvent() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Profile profile = Profile.get(player.getUniqueId());
            new MessageFormat(Locale.EVENT_START.format(profile.getLocale()))
                    .add("{event_name}", game.getEvent().getName())
                    .add("{event_displayname}", game.getEvent().getDisplayName())
                    .add("{size}", String.valueOf(game.getParticipants().size()))
                    .add("{maximum}", String.valueOf(game.getMaximumPlayers()))
                    .send(player);
        });

        int chosenMapVotes = 0;

        for (Map.Entry<EventGameMap, EventGameMapVoteData> entry : game.getVotesData().entrySet()) {
            if (game.getGameMap() == null) {
                game.setGameMap(entry.getKey());
                chosenMapVotes = entry.getValue().getPlayers().size();
            } else {
                if (entry.getValue().getPlayers().size() >= chosenMapVotes) {
                    game.setGameMap(entry.getKey());
                    chosenMapVotes = entry.getValue().getPlayers().size();
                }
            }
        }

        for (GameParticipant<GamePlayer> participant : game.getParticipants()) {
            for (GamePlayer gamePlayer : participant.getPlayers()) {
                Player player = gamePlayer.getPlayer();

                if (player != null) {
                    PlayerUtil.reset(player);
                    player.teleport(game.getGameMap().getSpectatorPoint());
                    Hotbar.giveHotbarItems(player);
                }
            }
        }
    }

    @Override
    public boolean canStartEvent() {
        return game.getRemainingParticipants() >= 2;
    }

    @Override
    public void preEndEvent() {
        for (GameParticipant participant : game.getParticipants()) {
            if (!participant.isEliminated()) {
                winningParticipant = participant;
                break;
            }
        }

        if (winningParticipant != null) {
            /*List<String> lines = Locale.EVENT_FINISH.formatLines(game.getEvent().getDisplayName(),
                winningParticipant.getConjoinedNames(),
                (winningParticipant.getPlayers().size() == 1 ? "has" : "have"),
                game.getEvent().getDisplayName());

            for (String line : lines) {
                Bukkit.broadcastMessage(line);
            }*/

            Bukkit.getOnlinePlayers().forEach(player -> {
                Profile profile = Profile.get(player.getUniqueId());
                new MessageFormat(Locale.EVENT_FINISH.format(profile.getLocale()))
                    .add("{event_name}", game.getEvent().getName())
                        .add("{event_displayname}", game.getEvent().getDisplayName())
                    .add("{winner}", winningParticipant.getConjoinedNames())
                    .add("{context}", (winningParticipant.getPlayers().size() == 1 ? "has" : "have"))
                    .send(player);
            });
        }
    }

    @Override
    public void endEvent() {
        EventGame.setActiveGame(null);
        EventGame.setCooldown(new Cooldown(30_000L));

        for (GameParticipant<GamePlayer> participant : game.getParticipants()) {
            for (GamePlayer gamePlayer : participant.getPlayers()) {
                Player player = gamePlayer.getPlayer();

                if (player != null) {
                    Profile profile = Profile.get(player.getUniqueId());
                    profile.setState(ProfileState.LOBBY);

                    Hotbar.giveHotbarItems(player);
                    cPractice.get().getEssentials().teleportToSpawn(player);
                    VisibilityLogic.handle(player);
                }
            }
        }
        SpleefEvent spleefEvent = (SpleefEvent) game.getEvent();
        spleefEvent.getChangedBlocks().forEach((blockState) -> blockState.getLocation().getBlock().setType(blockState.getType()));
        participants.clear();
        Profile.getProfiles().values().stream()
                .filter(profile -> profile.getPlayer() != null && profile.getPlayer().isOnline())
                .filter(profile -> profile.getState() == ProfileState.LOBBY)
            .forEach(profile -> Hotbar.giveHotbarItems(profile.getPlayer()));
    }

    @Override
    public boolean canEndEvent() {
        return game.getRemainingParticipants() <= 1;
    }

    @Override
    public void cancelEvent() {
        game.sendMessage(ChatColor.DARK_RED + "The event has been cancelled by an administrator!");

        EventGame.setActiveGame(null);
        EventGame.setCooldown(new Cooldown(30_000L));

        for (GameParticipant<GamePlayer> participant : game.getParticipants()) {
            for (GamePlayer gamePlayer : participant.getPlayers()) {
                Player player = gamePlayer.getPlayer();

                if (player != null) {
                    Profile profile = Profile.get(player.getUniqueId());
                    profile.setState(ProfileState.LOBBY);

                    Hotbar.giveHotbarItems(player);

                    cPractice.get().getEssentials().teleportToSpawn(player);
                }
            }
        }
        SpleefEvent spleefEvent = (SpleefEvent) game.getEvent();
        spleefEvent.getChangedBlocks().forEach((blockState) -> blockState.getLocation().getBlock().setType(blockState.getType()));
        participants.clear();
        Profile.getProfiles().values().stream()
                .filter(profile -> profile.getPlayer() != null && profile.getPlayer().isOnline())
                .filter(profile -> profile.getState() == ProfileState.LOBBY)
            .forEach(profile -> Hotbar.giveHotbarItems(profile.getPlayer()));
    }

    @Override
    public void preStartRound() {

    }

    @Override
    public void startRound() {
        game.sendSound(Sound.ORB_PICKUP, 1.0F, 15F);

        game.getGameMap().teleportFighters(game);

        for (GameParticipant<GamePlayer> participant : participants) {
            for (GamePlayer gamePlayer : participant.getPlayers()) {
                Player player = gamePlayer.getPlayer();

                if (player != null) {
                    player.getInventory().setArmorContents(new ItemStack[4]);
                    player.getInventory().clear();
                    Kit kit = Kit.getByName("Spleef");
                    player.getInventory().setArmorContents(kit.getKitLoadout().getArmor());
                    player.getInventory().setContents(kit.getKitLoadout().getContents());
                    player.updateInventory();
                }
            }
        }
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public void endRound() { }

    @Override
    public boolean canEndRound() {
        return false;
    }

    @Override
    public void onVote(Player player, EventGameMap gameMap) {
        if (game.getGameState() == EventGameState.WAITING_FOR_PLAYERS ||
            game.getGameState() == EventGameState.STARTING_EVENT) {
            EventGameMapVoteData voteData = game.getVotesData().get(gameMap);

            if (voteData != null) {
                if (voteData.hasVote(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You have already voted for that map!");
                } else {
                    for (EventGameMapVoteData otherVoteData : game.getVotesData().values()) {
                        if (otherVoteData.hasVote(player.getUniqueId())) {
                            otherVoteData.getPlayers().remove(player.getUniqueId());
                        }
                    }

                    voteData.addVote(player.getUniqueId());

                    game.sendMessage(Locale.EVENT_PLAYER_VOTE, new MessageFormat()
                        .add("{player_name}", cPractice.get().getRankManager().getRank().getPrefix(player.getUniqueId()) + player.getName())
                        .add("{map_name}", gameMap.getMapName())
                        .add("{votes}", String.valueOf(voteData.getPlayers().size()))
                    );
                }
            } else {
                player.sendMessage(ChatColor.RED + "A map with that name does not exist.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "The event has already started.");
        }
    }

    @Override
    public void onJoin(Player player) {
        game.getParticipants().add(new GameParticipant<>(new GamePlayer(player.getUniqueId(), player.getName())));

        game.sendMessage(Locale.EVENT_PLAYER_JOIN, new MessageFormat()
            .add("{player_name}", cPractice.get().getRankManager().getRank().getPrefix(player.getUniqueId()) + player.getName())
            .add("{size}", String.valueOf(game.getParticipants().size()))
            .add("{maximum}", String.valueOf(game.getMaximumPlayers()))
        );

        Profile profile = Profile.get(player.getUniqueId());
        profile.setState(ProfileState.EVENT);

        Hotbar.giveHotbarItems(player);

        for (Map.Entry<EventGameMap, EventGameMapVoteData> entry : game.getVotesData().entrySet()) {
            ItemStack itemStack = Hotbar.getItems().get(HotbarItem.MAP_SELECTION).getItemStack().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(itemMeta.getDisplayName().replace("%MAP%", entry.getKey().getMapName()));
            itemStack.setItemMeta(itemMeta);

            player.getInventory().addItem(itemStack);
        }

        player.updateInventory();
        player.teleport(game.getEvent().getLobbyLocation());

        VisibilityLogic.handle(player);

        for (GameParticipant<GamePlayer> gameParticipant : game.getParticipants()) {
            for (GamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player bukkitPlayer = gamePlayer.getPlayer();

                    if (bukkitPlayer != null) {
                        VisibilityLogic.handle(bukkitPlayer, player);
                    }
                }
            }
        }
    }

    @Override
    public void onLeave(Player player) {
        if (isPlaying(player)) {
            onDeath(player, null);
        }

        participants.remove(getGameParticipant(player));

        Iterator<GameParticipant<GamePlayer>> iterator = game.getParticipants().iterator();

        while (iterator.hasNext()) {
            GameParticipant<GamePlayer> participant = iterator.next();

            if (participant.containsPlayer(player.getUniqueId())) {
                iterator.remove();

                for (GamePlayer gamePlayer : participant.getPlayers()) {
                    if (!gamePlayer.isDisconnected()) {
                        Player bukkitPlayer = gamePlayer.getPlayer();

                        if (bukkitPlayer != null) {
                            if (game.getGameState() == EventGameState.WAITING_FOR_PLAYERS ||
                                game.getGameState() == EventGameState.STARTING_EVENT) {

                                game.sendMessage(Locale.EVENT_PLAYER_LEAVE, new MessageFormat()
                                    .add("{player_name}", cPractice.get().getRankManager().getRank().getPrefix(player.getUniqueId()) + player.getName())
                                    .add("{remaining}", String.valueOf(game.getRemainingPlayers()))
                                    .add("{maximum}", String.valueOf(game.getMaximumPlayers()))
                                );
                            }

                            Profile profile = Profile.get(bukkitPlayer.getUniqueId());
                            profile.setState(ProfileState.LOBBY);

                            Hotbar.giveHotbarItems(bukkitPlayer);
                            VisibilityLogic.handle(bukkitPlayer, player);

                            cPractice.get().getEssentials().teleportToSpawn(bukkitPlayer);
                        }
                    }
                }
            }
        }

        VisibilityLogic.handle(player);
    }

    @Override
    public void onMove(Player player) {
        if (isPlaying(player)) {
            GamePlayer gamePlayer = game.getGamePlayer(player);

            if (gamePlayer != null) {
                if (BlockUtil.isOnLiquid(player.getLocation(), 0)) {
                    if (!gamePlayer.isDead()) {
                        onDeath(player, null);
                    }
                }
            }
        }
    }

    @Override
    public void onDeath(Player player, Player killer) {
        GamePlayer deadGamePlayer = game.getGamePlayer(player);

        if (deadGamePlayer != null) {
            deadGamePlayer.setDead(true);
        }

        GameParticipant<GamePlayer>  gameParticipant = getGameParticipant(player);
        gameParticipant.setEliminated(true);
        if (canEndEvent()) {
            preEndEvent();
            game.setGameState(EventGameState.ENDING_EVENT);
            logicTask.setNextAction(3);
        }
    }

    @Override
    public void onInteract(PlayerInteractEvent event, Player player, ItemStack target) {

    }

    @Override
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event, Player player, Player target) {
        event.setCancelled(true);
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event, Player player) {
        event.setDamage(0);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public boolean isPlaying(Player player) {
        return getGameParticipant(player) != null;
    }

    @Override
    public List<String> getScoreboardEntries() {
        List<String> lines = new ArrayList<>();
        BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
        config.getStringList("EVENTS.SPLEEF.LINES").forEach(s -> {
            lines.add(s.replace("{event-name}", game.getEvent().getName())
                    .replace("{event-displayname}", game.getEvent().getDisplayName())
                    .replace("{players}", String.valueOf(game.getRemainingPlayers()))
                    .replace("{max-players}", String.valueOf(game.getMaximumPlayers()))
                    .replace("{bars}", CC.SB_BAR));
        });

        switch (game.getGameState()) {
            case WAITING_FOR_PLAYERS: {
                lines.addAll(config.getStringList("EVENTS.SPLEEF.WAITING-FOR-PLAYERS"));
            }
            break;
            case STARTING_EVENT: {
                config.getStringList("EVENTS.SPLEEF.STARTING-EVENT").forEach(s -> {
                    lines.add(s.replace("{time}", String.valueOf(game.getGameLogic().getGameLogicTask().getNextActionTime()))
                            .replace("{bars}", CC.SB_BAR));
                });
            }
            break;
            case STARTING_ROUND:
            case ENDING_ROUND: {
                config.getStringList("EVENTS.SPLEEF.ENDING-ROUND").forEach(s -> {
                    lines.add(s.replace("{bars}", CC.SB_BAR));
                });
            }
            break;
            case ENDING_EVENT: {
                if (winningParticipant != null) {
                    config.getStringList("EVENTS.SPLEEF.ENDING-EVENT").forEach(s -> {
                        lines.add(s.replace("{bars}", CC.SB_BAR)
                                .replace("{winner}", winningParticipant.getConjoinedNames()));
                    });
                }
            }
            break;
        }

        if (game.getGameState() == EventGameState.WAITING_FOR_PLAYERS ||
                game.getGameState() == EventGameState.STARTING_EVENT) {
            config.getStringList("EVENTS.SPLEEF.MAP-VOTES").forEach(s -> {
                if (s.contains("{votes-format}")) {
                    game.getVotesData().forEach((map, voteData) -> {
                        lines.add(config.getString("EVENTS.SPLEEF.VOTES-FORMAT")
                                .replace("{map-name}", map.getMapName())
                                .replace("{size}", String.valueOf(voteData.getPlayers().size())));
                    });
                    return;
                }
                lines.add(s.replace("{bars}", CC.SB_BAR));
            });
        }

        return lines;
    }

    @Override
    public int getRoundNumber() {
        return 1;
    }

    private GameParticipant<GamePlayer> getGameParticipant(Player player){
        return participants.stream().filter(gameParticipant -> gameParticipant.getLeader().getUuid() == player.getUniqueId()).findFirst().orElse(null);
    }
}