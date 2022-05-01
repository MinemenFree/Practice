package rip.crystal.practice.player.profile.file.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.configuration.ConfigurationSection;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.kit.KitLoadout;
import rip.crystal.practice.match.mongo.MatchInfo;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.file.IProfile;
import rip.crystal.practice.player.profile.meta.ProfileKitData;
import rip.crystal.practice.utilities.InventoryUtil;
import rip.crystal.practice.utilities.file.language.Lang;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.io.IOException;
import java.util.Map;

public class FlatFileIProfile implements IProfile {

    private final BasicConfigurationFile config = cPractice.get().getPlayersConfig();

    @Override
    public void save(Profile profile) {
        ConfigurationSection section = config.getConfiguration().getConfigurationSection("players");
        ConfigurationSection uuidSection = section.createSection(profile.getUuid().toString());

        if (profile.isOnline()) uuidSection.set("name", profile.getPlayer().getName());
        else uuidSection.set("name", profile.getName());

        uuidSection.set("lang", profile.getLocale().getAbbreviation());
        uuidSection.set("color", profile.getColor());
        uuidSection.set("coins", profile.getCoins());

        ConfigurationSection optionsSection = uuidSection.createSection("options");
        optionsSection.set("showScoreboard", profile.getOptions().showScoreboard());
        optionsSection.set("allowSpectators", profile.getOptions().allowSpectators());
        optionsSection.set("receiveDuelRequests", profile.getOptions().receiveDuelRequests());
        optionsSection.set("receivingNewConversations", profile.getOptions().receivingNewConversations());

        ConfigurationSection kitStatisticsSection = uuidSection.createSection("kitStatistics");
        for (Map.Entry<Kit, ProfileKitData> entry : profile.getKitData().entrySet()) {
            ConfigurationSection kitSection = kitStatisticsSection.createSection(entry.getKey().getName());
            kitSection.set("elo", entry.getValue().getElo());
            kitSection.set("won", entry.getValue().getWon());
            kitSection.set("lost", entry.getValue().getLost());
            kitSection.set("winstreak", entry.getValue().getKillstreak());
        }

        if (profile.getClan() != null) uuidSection.set("clan", profile.getClan().getName());

        if (!profile.getMatches().isEmpty()) {
            ConfigurationSection matchesSection = uuidSection.createSection("matches");

            for (int i = 0; i < profile.getMatches().size(); i++) {
                MatchInfo match = profile.getMatches().get(i);

                ConfigurationSection matchSection = matchesSection.createSection(String.valueOf(i));

                matchSection.set("winningParticipant", match.getWinningParticipant());
                matchSection.set("losingParticipant", match.getLosingParticipant());
                matchSection.set("kit", match.getKit().getName());
                matchSection.set("newWinnerElo", match.getNewWinnerElo());
                matchSection.set("newLoserElo", match.getNewLoserElo());
                matchSection.set("date", match.getDate());
                matchSection.set("duration", match.getDuration());
            }
        }

        ConfigurationSection kitsSection = uuidSection.createSection("loadouts");

        for (Map.Entry<Kit, ProfileKitData> entry : profile.getKitData().entrySet()) {
            JsonArray kitsArray = new JsonArray();

            for (int i = 0; i < 4; i++) {
                KitLoadout loadout = entry.getValue().getLoadout(i);

                if (loadout != null) {
                    JsonObject kitObject = new JsonObject();
                    kitObject.addProperty("index", i);
                    kitObject.addProperty("name", loadout.getCustomName());
                    kitObject.addProperty("armor", InventoryUtil.itemStackArrayToBase64(loadout.getArmor()));
                    kitObject.addProperty("contents", InventoryUtil.itemStackArrayToBase64(loadout.getContents()));
                    kitsArray.add(kitObject);
                }
            }

            kitsSection.set(entry.getKey().getName(), kitsArray.toString());
        }

        config.save();
        config.reload();
    }

    @Override
    public void load(Profile profile) {
        ConfigurationSection section = config.getConfiguration().getConfigurationSection("players." + profile.getUuid().toString());

        if (section == null) {
            this.save(profile);
            return;
        }

        if (section.contains("lang")) profile.setLocale(Lang.getByAbbreviation(section.getString("lang")));

        if (section.contains("name")) profile.setName(section.getString("name"));

        if (section.contains("color")) profile.setColor(section.getString("color"));

        if (section.contains("coins")) profile.setCoins(section.getInt("coins"));

        ConfigurationSection optionsSection = section.getConfigurationSection("options");

        profile.getOptions().showScoreboard(optionsSection.getBoolean("showScoreboard"));
        profile.getOptions().allowSpectators(optionsSection.getBoolean("allowSpectators"));
        profile.getOptions().receiveDuelRequests(optionsSection.getBoolean("receiveDuelRequests"));
        profile.getOptions().receivingNewConversations(optionsSection.getBoolean("receivingNewConversations"));

        if (section.contains("clan")) profile.setClan(Clan.getByName(section.getString("clan")));

        ConfigurationSection kitStatisticsSection = section.getConfigurationSection("kitStatistics");

        for (String key : kitStatisticsSection.getKeys(false)) {
            ConfigurationSection kitSection = kitStatisticsSection.getConfigurationSection(key);
            Kit kit = Kit.getByName(key);

            if (kit != null) {
                ProfileKitData profileKitData = new ProfileKitData();
                profileKitData.setElo(kitSection.getInt("elo"));
                profileKitData.setWon(kitSection.getInt("won"));
                profileKitData.setLost(kitSection.getInt("lost"));
                profileKitData.setKillstreak(kitSection.getInt("winstreak"));

                profile.getKitData().put(kit, profileKitData);
            }
        }

        ConfigurationSection matchesSection = section.getConfigurationSection("matches");

        if (section.contains("matches")) {
            for (String key : matchesSection.getKeys(false)) {
                ConfigurationSection matchSection = matchesSection.getConfigurationSection(key);

                String winningParticipant = matchSection.getString("winningParticipant");
                String losingParticipant = matchSection.getString("losingParticipant");
                Kit kit = Kit.getByName(matchSection.getString("kit"));
                int newWinnerElo = matchSection.getInt("newWinnerElo");
                int newLoserElo = matchSection.getInt("newLoserElo");
                String date = matchSection.getString("date");
                String duration = matchSection.getString("duration");
                profile.getMatches().add(new MatchInfo(winningParticipant, losingParticipant, kit, newWinnerElo, newLoserElo, date, duration));
            }
        }

        ConfigurationSection kitsSection = section.getConfigurationSection("loadouts");

        if (section.contains("loadouts")) {
            for (String key : kitsSection.getKeys(false)) {
                Kit kit = Kit.getByName(key);

                if (kit != null) {
                    JsonArray kitsArray = new JsonParser().parse(kitsSection.getString(key)).getAsJsonArray();
                    KitLoadout[] loadouts = new KitLoadout[4];

                    for (JsonElement kitElement : kitsArray) {
                        JsonObject kitObject = kitElement.getAsJsonObject();

                        KitLoadout loadout = new KitLoadout(kitObject.get("name").getAsString());
                        try {
                            loadout.setArmor(InventoryUtil.itemStackArrayFromBase64(kitObject.get("armor").getAsString()));
                            loadout.setContents(InventoryUtil.itemStackArrayFromBase64(kitObject.get("contents").getAsString()));
                        } catch (IOException ignore) {
                            System.out.print("Player Kit Edited failed!");
                            return;
                        }

                        loadouts[kitObject.get("index").getAsInt()] = loadout;
                    }

                    profile.getKitData().get(kit).setLoadouts(loadouts);
                }
            }
        }
    }
}
