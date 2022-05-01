package rip.crystal.practice.visual.leaderboard;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.elo.EloUtil;
import rip.crystal.practice.visual.leaderboard.entry.LeaderboardKitsEntry;
import rip.crystal.practice.visual.leaderboard.variables.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Leaderboard {

    @Getter public static Map<String, List<LeaderboardKitsEntry>> kitLeaderboards = Maps.newHashMap();
    @Getter public static List<Profile> leaderboards = Lists.newArrayList();
    @Getter public static Map<String, Integer> clanLeaderboards = Maps.newHashMap();

    public static void init() {
        Leaderboard.initHologramsVariables();
        TaskUtil.runTimerAsync(() -> {

            leaderboards.clear();
            leaderboards.addAll(Profile.getProfiles().values());

            kitLeaderboards.clear();
            Kit.getKits().stream().filter(kit -> kit.getGameRules().isRanked()).forEach(kit -> {
                List<LeaderboardKitsEntry> entry = Lists.newArrayList();
                Profile.getProfiles().values().stream()
                        .sorted(((Comparator<Profile>) (o1, o2) -> Integer.compare(o1.getKitData().get(kit).getElo(), o2.getKitData().get(kit).getElo())).reversed())
                        .limit(10)
                        .forEach(profile -> entry.add(new LeaderboardKitsEntry(profile, profile.getKitData().get(kit).getElo())));
                kitLeaderboards.put(kit.getName(), entry);
            });

            clanLeaderboards.clear();
            for (String s : Clan.getClans().keySet()) {
                Clan clan = Clan.getClans().get(s);
                clanLeaderboards.put(clan.getName(), clan.getPoints());
            }

            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();
            clanLeaderboards = clanLeaderboards.entrySet().stream()
                    .sorted(comparator.reversed())
                    .limit(10)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));

            leaderboards = Profile.getProfiles().values().stream()
                    .sorted(Comparator.comparingInt(EloUtil::getGlobalElo).reversed())
                    .limit(10)
                    .collect(Collectors.toList());

        }, 100L, 600L);
    }

    public static void initHologramsVariables() {
        // Register Leaderboards Hologram Placeholders
        HologramsAPI.registerPlaceholder(
                cPractice.get(), "{globaltop" + 0 + "_elo}", 30,
                new TopGlobalElo(0));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 0 + "_name}", 30,
                new TopGlobalName(0));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 1 + "_elo}", 30,
                new TopGlobalElo(1));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 1 + "_name}", 30,
                new TopGlobalName(1));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 2 + "_elo}", 30,
                new TopGlobalElo(2));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 2 + "_name}", 30,
                new TopGlobalName(2));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 3 + "_elo}", 30,
                new TopGlobalElo(3));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 3 + "_name}", 30,
                new TopGlobalName(3));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 4 + "_elo}", 30,
                new TopGlobalElo(4));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 4 + "_name}", 30,
                new TopGlobalName(4));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 5 + "_elo}", 30,
                new TopGlobalElo(5));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 5 + "_name}", 30,
                new TopGlobalName(5));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 6 + "_elo}", 30,
                new TopGlobalElo(6));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 6 + "_name}", 30,
                new TopGlobalName(6));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 7 + "_elo}", 30,
                new TopGlobalElo(7));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 7 + "_name}", 30,
                new TopGlobalName(7));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 8 + "_elo}", 30,
                new TopGlobalElo(8));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 8 + "_name}", 30,
                new TopGlobalName(8));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 9 + "_elo}", 30,
                new TopGlobalElo(9));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{globaltop" + 9 + "_name}", 30,
                new TopGlobalName(9));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 0 + "_points}", 30,
                new TopClanPoints(0));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 0 + "_name}", 30,
                new TopClanName(0));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 0 + "_category}", 30,
                new TopClanCategory(0));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 1 + "_points}", 30,
                new TopClanPoints(1));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 1 + "_name}", 30,
                new TopClanName(1));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 1 + "_category}", 30,
                new TopClanCategory(1));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 2 + "_points}", 30,
                new TopClanPoints(2));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 2 + "_name}", 30,
                new TopClanName(2));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 2 + "_category}", 30,
                new TopClanCategory(2));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 3 + "_points}", 30,
                new TopClanPoints(3));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 3 + "_name}", 30,
                new TopClanName(3));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 3 + "_category}", 30,
                new TopClanCategory(3));

        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 4 + "_points}", 30,
                new TopClanPoints(4));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 4 + "_name}", 30,
                new TopClanName(4));
        HologramsAPI.registerPlaceholder(cPractice.get(), "{clantop" + 4 + "_category}", 30,
                new TopClanCategory(4));

        Kit.getKits().stream().filter(kit -> kit.getGameRules().isRanked()).forEach(kit -> {
            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 0 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 0));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 1 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 1));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 2 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 2));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 3 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 3));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 4 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 4));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 5 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 5));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 6 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 6));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 7 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 7));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 8 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 8));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 9 + kit.getName().toLowerCase() + "_elo}",
                    30, new TopKitElo(kit.getName(), 9));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 0 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 0));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 1 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 1));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 2 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 2));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 3 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 3));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 4 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 4));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 5 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 5));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 6 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 6));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 7 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 7));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 8 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 8));

            HologramsAPI.registerPlaceholder(cPractice.get(), "{top" + 9 + kit.getName().toLowerCase() + "_name}",
                    30, new TopKitName(kit.getName(), 9));
        });
    }

}
