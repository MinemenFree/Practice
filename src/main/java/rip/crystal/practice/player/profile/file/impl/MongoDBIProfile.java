package rip.crystal.practice.player.profile.file.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.kit.KitLoadout;
import rip.crystal.practice.match.mongo.MatchInfo;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.cosmetics.impl.killeffects.KillEffectType;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.file.IProfile;
import rip.crystal.practice.player.profile.meta.ProfileKitData;
import rip.crystal.practice.utilities.InventoryUtil;
import rip.crystal.practice.utilities.file.language.Lang;

import java.io.IOException;
import java.util.Map;

public class MongoDBIProfile implements IProfile {

    @Override
    public void save(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());

        if (profile.isOnline()) document.put("name", profile.getPlayer().getName());
        else document.put("name", profile.getName());

        document.put("lang", profile.getLocale().getAbbreviation());
        document.put("color", profile.getColor());
        document.put("coins", cPractice.get().getShopSystem().getCoins(profile.getUuid()));

        Document optionsDocument = new Document();
        optionsDocument.put("showScoreboard", profile.getOptions().showScoreboard());
        optionsDocument.put("allowSpectators", profile.getOptions().allowSpectators());
        optionsDocument.put("receiveDuelRequests", profile.getOptions().receiveDuelRequests());
        optionsDocument.put("receivingNewConversations", profile.getOptions().receivingNewConversations());
        optionsDocument.put("playingMessageSounds", profile.getOptions().playingMessageSounds());
        optionsDocument.put("publicChatEnabled", profile.getOptions().publicChatEnabled());
        optionsDocument.put("vanillaTab", profile.getOptions().vanillaTab());

        if(profile.getKillEffectType() != null) {
            optionsDocument.put("killEffect", profile.getKillEffectType().getName());
        }

        document.put("options", optionsDocument);

        Document kitStatisticsDocument = new Document();

        for (Map.Entry<Kit, ProfileKitData> entry : profile.getKitData().entrySet()) {
            Document kitDocument = new Document();
            kitDocument.put("elo", entry.getValue().getElo());
            kitDocument.put("won", entry.getValue().getWon());
            kitDocument.put("lost", entry.getValue().getLost());
            kitDocument.put("winstreak", entry.getValue().getKillstreak());
            kitStatisticsDocument.put(entry.getKey().getName(), kitDocument);
        }

        if (profile.getClan() != null) document.put("clan", profile.getClan().getName());

        document.put("kitStatistics", kitStatisticsDocument);

        if (!profile.getMatches().isEmpty()) {
            Document matchesDocument = new Document();

            for (int i = 0; i < profile.getMatches().size(); i++) {
                MatchInfo match = profile.getMatches().get(i);
                Document matchDocument = new Document();
                matchDocument.put("winningParticipant", match.getWinningParticipant());
                matchDocument.put("losingParticipant", match.getLosingParticipant());
                matchDocument.put("kit", match.getKit().getName());
                matchDocument.put("newWinnerElo", match.getNewWinnerElo());
                matchDocument.put("newLoserElo", match.getNewLoserElo());
                matchDocument.put("date", match.getDate());
                matchDocument.put("duration", match.getDuration());

                matchesDocument.put(String.valueOf(i), matchDocument);
            }

            document.put("matches", matchesDocument);
        }

        Document kitsDocument = new Document();

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

            kitsDocument.put(entry.getKey().getName(), kitsArray.toString());
        }
        document.put("loadouts", kitsDocument);

        Profile.getCollection().replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public void load(Profile profile) {
        if(profile.getUuid() == null) {
            return;
        }

        if(Profile.getCollection() == null) {
            return;
        }

        Document document = Profile.getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();

        if (document == null) {
            this.save(profile);
            return;
        }

        if (document.containsKey("lang")) profile.setLocale(Lang.getByAbbreviation(document.getString("lang")));

        if (document.containsKey("name")) profile.setName(document.getString("name"));

        if (document.containsKey("color")) profile.setColor(document.getString("color"));

        if (document.containsKey("coins")) cPractice.get().getShopSystem().setCoins(profile.getUuid(), document.getInteger("coins"));

        Document options = (Document) document.get("options");

        profile.getOptions().showScoreboard(options.getBoolean("showScoreboard"));
        profile.getOptions().allowSpectators(options.getBoolean("allowSpectators"));
        profile.getOptions().receiveDuelRequests(options.getBoolean("receiveDuelRequests"));
        profile.getOptions().receivingNewConversations(options.getBoolean("receivingNewConversations"));
        profile.getOptions().playingMessageSounds(options.getBoolean("playingMessageSounds"));
        profile.getOptions().publicChatEnabled(options.getBoolean("publicChatEnabled"));
        profile.getOptions().vanillaTab(options.getBoolean("vanillaTab"));

        if (options.containsKey("killEffect")) profile.setKillEffectType(KillEffectType.getByName(options.getString("killEffect")));

        if (document.containsKey("clan")) profile.setClan(Clan.getByName(document.getString("clan")));

        Document kitStatistics = (Document) document.get("kitStatistics");

        for (String key : kitStatistics.keySet()) {
            Document kitDocument = (Document) kitStatistics.get(key);
            Kit kit = Kit.getByName(key);

            if (kit != null) {
                ProfileKitData profileKitData = new ProfileKitData();
                profileKitData.setElo(kitDocument.getInteger("elo"));
                profileKitData.setWon(kitDocument.getInteger("won"));
                profileKitData.setLost(kitDocument.getInteger("lost"));
                profileKitData.setKillstreak(kitDocument.getInteger("winstreak"));

                profile.getKitData().put(kit, profileKitData);
            }
        }

        if (document.containsKey("matches")) {
            ((Document)document.get("matches")).forEach((s, o) -> {
                Document matchesDocument = (Document) o;
                String winningParticipant = matchesDocument.getString("winningParticipant");
                String losingParticipant = matchesDocument.getString("losingParticipant");
                Kit kit = Kit.getByName(matchesDocument.getString("kit"));
                int newWinnerElo = matchesDocument.getInteger("newWinnerElo");
                int newLoserElo = matchesDocument.getInteger("newLoserElo");
                String date = matchesDocument.getString("date");
                String duration = matchesDocument.getString("duration");
                profile.getMatches().add(new MatchInfo(winningParticipant, losingParticipant, kit, newWinnerElo, newLoserElo, date, duration));
            });
        }

        Document kitsDocument = (Document) document.get("loadouts");

        if (document.containsKey("loadouts")) {
            for (String key : kitsDocument.keySet()) {
                Kit kit = Kit.getByName(key);

                if (kit != null) {
                    JsonArray kitsArray = new JsonParser().parse(kitsDocument.getString(key)).getAsJsonArray();
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
