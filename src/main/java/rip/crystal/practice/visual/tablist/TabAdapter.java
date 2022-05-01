package rip.crystal.practice.visual.tablist;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.EventGameState;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.impl.BasicFreeForAllMatch;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.profile.weight.Weight;
import rip.crystal.practice.utilities.BukkitReflection;
import rip.crystal.practice.utilities.TimeUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.elo.EloUtil;
import rip.crystal.practice.utilities.lag.LagRunnable;
import rip.crystal.practice.visual.tablist.impl.GhostlyAdapter;
import rip.crystal.practice.visual.tablist.impl.TabListCommons;
import rip.crystal.practice.visual.tablist.impl.utils.BufferedTabObject;
import rip.crystal.practice.visual.tablist.impl.utils.SkinTexture;
import rip.crystal.practice.visual.tablist.impl.utils.TabColumn;

import java.util.*;
import java.util.stream.Collectors;

public class TabAdapter implements GhostlyAdapter {

    private final cPractice plugin = cPractice.get();
    @Getter private static final LinkedList<Weight> ranks = Lists.newLinkedList();

    @Override
    public String getFooter() {
        return plugin.getLangConfig().getString("TABLIST.FOOTER");
    }

    @Override
    public String getHeader() {
        return plugin.getLangConfig().getString("TABLIST.HEADER");
    }

    @Override
    public Set<BufferedTabObject> getSlots(Player player) {
        LinkedList<Weight> ranksSorted = ranks.stream().sorted(Comparator.comparing(Weight::getInteger).reversed()).limit(80L).collect(Collectors.toCollection(LinkedList::new)); //ranks.stream().limit(80).collect(Collectors.toCollection(LinkedList::new));

        Set<BufferedTabObject> tabObjects = Sets.newHashSet();
        Profile profile = Profile.get(player.getUniqueId());

        if (profile.getTabType() == TabType.DEFAULT) {
            for (int i = 1; i < 21; i++) {
                switch (profile.getState()) {
                    case SPECTATING:
                    case FFA:
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceFFA(plugin.getTabFFAConfig().getString("TABLIST.LEFT." + i + ".text"), player))
                                .skin(skin(plugin.getTabFFAConfig().getString("TABLIST.LEFT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.LEFT));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceFFA(plugin.getTabFFAConfig().getString("TABLIST.CENTER." + i + ".text"), player))
                                .skin(skin(plugin.getTabFFAConfig().getString("TABLIST.CENTER." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.MIDDLE));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceFFA(plugin.getTabFFAConfig().getString("TABLIST.RIGHT." + i + ".text"), player))
                                .skin(skin(plugin.getTabFFAConfig().getString("TABLIST.RIGHT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.RIGHT));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceFFA(plugin.getTabFFAConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".text"), player))
                                .skin(skin(plugin.getTabFFAConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.FAR_RIGHT));
                        break;
                    case FIGHTING:
                        Match match = profile.getMatch();
                        Party party = profile.getParty();
                        if (party != null) {
                            if (match instanceof BasicFreeForAllMatch) {
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceFFAFighting(plugin.getTabPartyFFAFightConfig().getString("TABLIST.LEFT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabPartyFFAFightConfig().getString("TABLIST.LEFT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.LEFT));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceFFAFighting(plugin.getTabPartyFFAFightConfig().getString("TABLIST.CENTER." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabPartyFFAFightConfig().getString("TABLIST.CENTER." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.MIDDLE));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceFFAFighting(plugin.getTabPartyFFAFightConfig().getString("TABLIST.RIGHT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabPartyFFAFightConfig().getString("TABLIST.RIGHT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.RIGHT));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceFFAFighting(plugin.getTabPartyFFAFightConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabPartyFFAFightConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.FAR_RIGHT));
                            } else if (match instanceof BasicTeamMatch) {
                                tabObjects.add(new BufferedTabObject()
                                        .text(replacePartyTeamFighting(plugin.getTabPartyTeamFightConfig().getString("TABLIST.LEFT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabPartyTeamFightConfig().getString("TABLIST.LEFT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.LEFT));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replacePartyTeamFighting(plugin.getTabPartyTeamFightConfig().getString("TABLIST.CENTER." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabPartyTeamFightConfig().getString("TABLIST.CENTER." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.MIDDLE));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replacePartyTeamFighting(plugin.getTabPartyTeamFightConfig().getString("TABLIST.RIGHT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabPartyTeamFightConfig().getString("TABLIST.RIGHT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.RIGHT));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replacePartyTeamFighting(plugin.getTabPartyTeamFightConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabPartyTeamFightConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.FAR_RIGHT));
                            }
                        } else {
                            if (match instanceof BasicFreeForAllMatch) {
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceFFAFighting(plugin.getTabSingleFFAFightConfig().getString("TABLIST.LEFT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabSingleFFAFightConfig().getString("TABLIST.LEFT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.LEFT));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceFFAFighting(plugin.getTabSingleFFAFightConfig().getString("TABLIST.CENTER." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabSingleFFAFightConfig().getString("TABLIST.CENTER." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.MIDDLE));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceFFAFighting(plugin.getTabSingleFFAFightConfig().getString("TABLIST.RIGHT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabSingleFFAFightConfig().getString("TABLIST.RIGHT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.RIGHT));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceFFAFighting(plugin.getTabSingleFFAFightConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabSingleFFAFightConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.FAR_RIGHT));
                            } else if (match instanceof BasicTeamMatch) {
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceSingleTeamFighting(plugin.getTabSingleTeamFightConfig().getString("TABLIST.LEFT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabSingleTeamFightConfig().getString("TABLIST.LEFT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.LEFT));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceSingleTeamFighting(plugin.getTabSingleTeamFightConfig().getString("TABLIST.CENTER." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabSingleTeamFightConfig().getString("TABLIST.CENTER." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.MIDDLE));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceSingleTeamFighting(plugin.getTabSingleTeamFightConfig().getString("TABLIST.RIGHT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabSingleTeamFightConfig().getString("TABLIST.RIGHT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.RIGHT));
                                tabObjects.add(new BufferedTabObject()
                                        .text(replaceSingleTeamFighting(plugin.getTabSingleTeamFightConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".text"), match, player))
                                        .skin(skin(plugin.getTabSingleTeamFightConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".head"), player))
                                        .slot(i)
                                        .ping(0)
                                        .column(TabColumn.FAR_RIGHT));
                            }
                        }
                        break;
                    case EVENT:
                        EventGame event = EventGame.getActiveGame();
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceEvent(plugin.getTabEventConfig().getString("TABLIST.LEFT." + i + ".text"), event, player))
                                .skin(skin(plugin.getTabEventConfig().getString("TABLIST.LEFT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.LEFT));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceEvent(plugin.getTabEventConfig().getString("TABLIST.CENTER." + i + ".text"), event, player))
                                .skin(skin(plugin.getTabEventConfig().getString("TABLIST.CENTER." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.MIDDLE));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceEvent(plugin.getTabEventConfig().getString("TABLIST.RIGHT." + i + ".text"), event, player))
                                .skin(skin(plugin.getTabEventConfig().getString("TABLIST.RIGHT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.RIGHT));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceEvent(plugin.getTabEventConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".text"), event, player))
                                .skin(skin(plugin.getTabEventConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.FAR_RIGHT));
                        break;
                    default:
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceLobby(plugin.getTabLobbyConfig().getString("TABLIST.LEFT." + i + ".text"), player))
                                .skin(skin(plugin.getTabLobbyConfig().getString("TABLIST.LEFT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.LEFT));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceLobby(plugin.getTabLobbyConfig().getString("TABLIST.CENTER." + i + ".text"), player))
                                .skin(skin(plugin.getTabLobbyConfig().getString("TABLIST.CENTER." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.MIDDLE));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceLobby(plugin.getTabLobbyConfig().getString("TABLIST.RIGHT." + i + ".text"), player))
                                .skin(skin(plugin.getTabLobbyConfig().getString("TABLIST.RIGHT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.RIGHT));
                        tabObjects.add(new BufferedTabObject()
                                .text(replaceLobby(plugin.getTabLobbyConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".text"), player))
                                .skin(skin(plugin.getTabLobbyConfig().getString("TABLIST.EXTERNAL-RIGHT." + i + ".head"), player))
                                .slot(i)
                                .ping(0)
                                .column(TabColumn.FAR_RIGHT));
                        break;
                }
                if (profile.getTabType() == TabType.WEIGHT) {
                    break;
                }
            }
        } else if (profile.getTabType() == TabType.WEIGHT) {
            for (int i = 0; i < ranksSorted.size(); ++i) {
                List<TabColumn> list = Arrays.asList(TabColumn.LEFT, TabColumn.MIDDLE, TabColumn.RIGHT, TabColumn.FAR_RIGHT);
                int n = i % 4;
                int n2 = i / 4 + 1;
                if (ranksSorted.get(i) == null) continue;
                Weight weight = ranksSorted.get(i);
                tabObjects.add(new BufferedTabObject().text(weight.getFormat()).slot(n2).ping(-1).column(list.get(n)));
            }
        }
        return tabObjects;
    }

    public String replaceLobby(String string, Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        String event = EventGame.getActiveGame() != null ? EventGame.getActiveGame().getEvent().getDisplayName() :
                EventGame.getCooldown().hasExpired() ? "None" : TimeUtil.millisToTimer(EventGame.getCooldown().getRemaining());
        List<Kit> kit = Kit.getKits().stream().filter(Kit::isEnabled).filter(kits -> kits.getGameRules().isRanked()).collect(Collectors.toList());
        string = string
                .replace("{name}", player.getName())
                .replace("{rank}", plugin.getRankManager().getRank().getName(player.getUniqueId()))
                .replace("{player-color}", profile.getColor())
                .replace("{rank-prefix}", plugin.getRankManager().getRank().getPrefix(player.getUniqueId()))
                .replace("{rank-suffix}", plugin.getRankManager().getRank().getSuffix(player.getUniqueId()))
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{slots}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{discord}", plugin.getLangConfig().getString("DISCORD"))
                .replace("{store}", plugin.getLangConfig().getString("STORE"))
                .replace("{teamspeak}", plugin.getLangConfig().getString("TEAMSPEAK"))
                .replace("{twitter}", plugin.getLangConfig().getString("TWITTER"))
                .replace("{website}", plugin.getLangConfig().getString("WEBSITE"))
                .replace("{ping}", String.valueOf(BukkitReflection.getPing(player)))
                .replace("{bars}", CC.TAB_BAR)
                .replace("{inqueue}", String.valueOf(plugin.getInQueues()))
                .replace("{infight}", String.valueOf(plugin.getInFights()))
                .replace("{event}", event)
                .replace("{globalelo}", String.valueOf(EloUtil.getGlobalElo(profile)))
                .replace("{tps}", format(LagRunnable.getTPS()));
                //.replace("{tps}", format(Bukkit.spigot().getTPS()[0]));
        for (Kit kits : kit)
            string = string.replace("{" + kits.getName().toLowerCase() + "_elo}", String.valueOf(profile.getKitData().get(kits).getElo()));
        return string;
    }

    public String replaceFFA(String string, Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        //String event = EventGame.getActiveGame() != null ? EventGame.getActiveGame().getEvent().getDisplayName() :
                //EventGame.getCooldown().hasExpired() ? "None" : TimeUtil.millisToTimer(EventGame.getCooldown().getRemaining());
        List<Kit> kit = Kit.getKits().stream().filter(Kit::isEnabled).filter(kits -> kits.getGameRules().isRanked()).collect(Collectors.toList());
        string = string
                .replace("{name}", player.getName())
                .replace("{rank}", plugin.getRankManager().getRank().getName(player.getUniqueId()))
                .replace("{player-color}", profile.getColor())
                .replace("{rank-prefix}", plugin.getRankManager().getRank().getPrefix(player.getUniqueId()))
                .replace("{rank-suffix}", plugin.getRankManager().getRank().getSuffix(player.getUniqueId()))
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{slots}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{discord}", plugin.getLangConfig().getString("DISCORD"))
                .replace("{store}", plugin.getLangConfig().getString("STORE"))
                .replace("{teamspeak}", plugin.getLangConfig().getString("TEAMSPEAK"))
                .replace("{twitter}", plugin.getLangConfig().getString("TWITTER"))
                .replace("{website}", plugin.getLangConfig().getString("WEBSITE"))
                .replace("{ping}", String.valueOf(BukkitReflection.getPing(player)))
                .replace("{bars}", CC.TAB_BAR)
                .replace("{players}", String.valueOf(cPractice.get().getFfaManager().getFFAPlayers().size()))
                .replace("{kit}", cPractice.get().getFfaManager().getKit().getName())
                .replace("{tps}", format(LagRunnable.getTPS()));
                //.replace("{tps}", format(Bukkit.spigot().getTPS()[0]));
        for (Kit kits : kit)
            string = string.replace("{" + kits.getName().toLowerCase() + "_elo}", String.valueOf(profile.getKitData().get(kits).getElo()));
        return string;
    }


    private String format(double tps) {
        int max = 20;
        return ((tps > 18.0D) ? ChatColor.GREEN : ((tps > 16.0D) ? ChatColor.YELLOW : ChatColor.RED)) + ((tps > max) ? "*" : "") + Math.min(Math.round(tps * 100.0D) / 100.0D, max);
    }

    public String replaceEvent(String string, EventGame event, Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        string = string
                .replace("{name}", player.getName())
                .replace("{rank}", plugin.getRankManager().getRank().getName(player.getUniqueId()))
                .replace("{player-color}", profile.getColor())
                .replace("{rank-prefix}", plugin.getRankManager().getRank().getPrefix(player.getUniqueId()))
                .replace("{rank-suffix}", plugin.getRankManager().getRank().getSuffix(player.getUniqueId()))
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{slots}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{discord}", plugin.getLangConfig().getString("DISCORD"))
                .replace("{store}", plugin.getLangConfig().getString("STORE"))
                .replace("{teamspeak}", plugin.getLangConfig().getString("TEAMSPEAK"))
                .replace("{twitter}", plugin.getLangConfig().getString("TWITTER"))
                .replace("{website}", plugin.getLangConfig().getString("WEBSITE"))
                .replace("{ping}", String.valueOf(BukkitReflection.getPing(player)))
                .replace("{bars}", CC.TAB_BAR)
                .replace("{event}", event.getEvent().getDisplayName())
                .replace("{remaining-players}", String.valueOf(event.getRemainingPlayers()))
                .replace("{max-players}", String.valueOf(event.getMaximumPlayers()))
                .replace("{map-name}", event.getGameState() == EventGameState.PLAYING_ROUND ? event.getGameMap().getMapName() : "None")
                .replace("{hoster}", plugin.getRankManager().getRank().getPrefix(event.getGameHost().getUuid()) + event.getGameHost().getUsername());
        return string;
    }

    public String replaceFFAFighting(String string, Match match, Player player) {
        BasicFreeForAllMatch match1 = (BasicFreeForAllMatch) match;
        Profile profile = Profile.get(player.getUniqueId());
        string = string
                .replace("{name}", player.getName())
                .replace("{rank}", plugin.getRankManager().getRank().getName(player.getUniqueId()))
                .replace("{player-color}", profile.getColor())
                .replace("{rank-prefix}", plugin.getRankManager().getRank().getPrefix(player.getUniqueId()))
                .replace("{rank-suffix}", plugin.getRankManager().getRank().getSuffix(player.getUniqueId()))
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{slots}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{discord}", plugin.getLangConfig().getString("DISCORD"))
                .replace("{store}", plugin.getLangConfig().getString("STORE"))
                .replace("{teamspeak}", plugin.getLangConfig().getString("TEAMSPEAK"))
                .replace("{twitter}", plugin.getLangConfig().getString("TWITTER"))
                .replace("{website}", plugin.getLangConfig().getString("WEBSITE"))
                .replace("{ping}", String.valueOf(BukkitReflection.getPing(player)))
                .replace("{bars}", CC.TAB_BAR)
                .replace("{duration}", match.getDuration())
                .replace("{kit}", match.getKit().getName())
                .replace("{arena-name}", match.getArena().getName())
                .replace("{arena-author}", match.getArena().getAuthor());

        for (int b = 0; b < match1.getParticipants().size(); b++) {
            string = string.replace("{match-left-player-" + (b + 1) + "}", ChatColor.GRAY + match1.getParticipants().get(b).getConjoinedNames());
        }

        for (int b = 0; b < 31; b++) {
            string = string.replace("{match-left-player-" + (b + 1) + "}", "");
        }
        return string;
    }

    public String replaceSingleTeamFighting(String string, Match match, Player player) {
        BasicTeamMatch match1 = (BasicTeamMatch) match;
        GameParticipant<MatchGamePlayer> opponent;
        Profile profile = Profile.get(player.getUniqueId());
        if (match1.getParticipantA().containsPlayer(player.getUniqueId())) opponent = match1.getParticipantB();
        else opponent = match1.getParticipantA();
        string = string
                .replace("{name}", player.getName())
                .replace("{rank}", plugin.getRankManager().getRank().getName(player.getUniqueId()))
                .replace("{player-color}", profile.getColor())
                .replace("{rank-prefix}", plugin.getRankManager().getRank().getPrefix(player.getUniqueId()))
                .replace("{rank-suffix}", plugin.getRankManager().getRank().getSuffix(player.getUniqueId()))
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{slots}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{discord}", plugin.getLangConfig().getString("DISCORD"))
                .replace("{store}", plugin.getLangConfig().getString("STORE"))
                .replace("{teamspeak}", plugin.getLangConfig().getString("TEAMSPEAK"))
                .replace("{twitter}", plugin.getLangConfig().getString("TWITTER"))
                .replace("{website}", plugin.getLangConfig().getString("WEBSITE"))
                .replace("{ping}", String.valueOf(BukkitReflection.getPing(player)))
                .replace("{bars}", CC.TAB_BAR)
                .replace("{duration}", match.getDuration())
                .replace("{kit}", match.getKit().getName())
                .replace("{arena-name}", match.getArena().getName())
                .replace("{arena-author}", match.getArena().getAuthor())
                .replace("{player-fight}", CC.GREEN + player.getName())
                .replace("{opponent-player}", opponent.getLeader().getPlayer() != null ? CC.RED + opponent.getLeader().getPlayer().getName() : "");
        return string;
    }

    public String replacePartyTeamFighting(String string, Match match, Player player) {
        BasicTeamMatch match1 = (BasicTeamMatch) match;
        GameParticipant<MatchGamePlayer> team;
        GameParticipant<MatchGamePlayer> opponent;
        Profile profile = Profile.get(player.getUniqueId());
        if (match1.getParticipantA().containsPlayer(player.getUniqueId())) {
            opponent = match1.getParticipantB();
            team = match1.getParticipantA();
        }
        else {
            opponent = match1.getParticipantA();
            team = match1.getParticipantB();
        }
        string = string
                .replace("{name}", player.getName())
                .replace("{rank}", plugin.getRankManager().getRank().getName(player.getUniqueId()))
                .replace("{player-color}", profile.getColor())
                .replace("{rank-prefix}", plugin.getRankManager().getRank().getPrefix(player.getUniqueId()))
                .replace("{rank-suffix}", plugin.getRankManager().getRank().getSuffix(player.getUniqueId()))
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{slots}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{discord}", plugin.getLangConfig().getString("DISCORD"))
                .replace("{store}", plugin.getLangConfig().getString("STORE"))
                .replace("{teamspeak}", plugin.getLangConfig().getString("TEAMSPEAK"))
                .replace("{twitter}", plugin.getLangConfig().getString("TWITTER"))
                .replace("{website}", plugin.getLangConfig().getString("WEBSITE"))
                .replace("{ping}", String.valueOf(BukkitReflection.getPing(player)))
                .replace("{bars}", CC.TAB_BAR)
                .replace("{duration}", match.getDuration())
                .replace("{kit}", match.getKit().getName())
                .replace("{arena-name}", match.getArena().getName())
                .replace("{arena-author}", match.getArena().getAuthor());
        for (int b = 0; b < team.getPlayers().size(); b++) {
            if (team.getPlayers().get(b) != null) {
                string = string.replace("{team-player-" + (b + 1) + "}", team.getPlayers().get(b).getUsername());
            }
        }

        for (int b = 0; b < opponent.getPlayers().size(); b++) {
            if (opponent.getPlayers().get(b) != null) {
                string = string.replace("{opponent-player-" + (b + 1) + "}", opponent.getPlayers().get(b).getUsername());
            }
        }

        for (int b = 0; b < 11; b++) {
            string = string.replace("{team-player-" + (b + 1) + "}", "")
                    .replace("{opponent-player-" + (b + 1) + "}", "");
        }
        return string;
    }

    @SneakyThrows
    public SkinTexture skin(String string, Player player) {
        SkinTexture skin = TabListCommons.defaultTexture;
        switch (string) {
            case "{player}":
                skin = TabListCommons.getSkinData(player.getUniqueId());
                break;
            case "{discord}":
                skin = TabListCommons.DISCORD_TEXTURE;
                break;
            case "{twitter}":
                skin = TabListCommons.TWITTER_TEXTURE;
                break;
            case "{brayding}":
                skin = TabListCommons.getSkinData(UUID.fromString("d58ef82d-16e9-45e0-b08a-fb73ab62feaf"));
                break;
            case "{ziue}":
                skin = TabListCommons.getSkinData(UUID.fromString("148f1abc-6352-41fa-9c91-f666c3b04082"));
                break;
            case "{green}":
                skin = TabListCommons.GREEN_DOT;
                break;
            case "{blue}":
                skin = TabListCommons.BLUE_DOT;
                break;
            case "{dark_blue}":
                skin = TabListCommons.DARK_BLUE_DOT;
                break;
            case "{dark_aqua}":
                skin = TabListCommons.DARK_AQUA_DOT;
                break;
            case "{purple}":
                skin = TabListCommons.DARK_PURPLE_DOT;
                break;
            case "{pink}":
                skin = TabListCommons.LIGHT_PURPLE_DOT;
                break;
            case "{gray}":
                skin = TabListCommons.GRAY_DOT;
                break;
            case "{red}":
                skin = TabListCommons.RED_DOT;
                break;
            case "{yellow}":
                skin = TabListCommons.YELLOW_DOT;
                break;
            case "{dark_green}":
                skin = TabListCommons.DARK_GREEN_DOT;
                break;
            case "{dark_red}":
                skin = TabListCommons.DARK_RED_DOT;
                break;
            case "{gold}":
                skin = TabListCommons.GOLD_DOT;
                break;
            case "{aqua}":
                skin = TabListCommons.AQUA_DOT;
                break;
            case "{white}":
                skin = TabListCommons.WHITE_DOT;
                break;
            case "{dark_gray}":
                skin = TabListCommons.DARK_GRAY;
                break;
            case "{black}":
                skin = TabListCommons.BLACK_DOT;
                break;
        }
        return skin;
    }

    public static Weight getByUuid(UUID uuid) {
        return ranks.stream()
                .filter(weight -> weight.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }
}