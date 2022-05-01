package rip.crystal.practice.player.clan;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.file.impl.FlatFileIProfile;
import rip.crystal.practice.player.profile.file.impl.MongoDBIProfile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Clan {

    private static ConfigurationSection config;
    @Getter private static final Map<String, Clan> clans = Maps.newHashMap();
    @Getter public static MongoCollection<Document> collection;

    @Setter private String name;
    @Setter private ChatColor color = ChatColor.WHITE;
    private final UUID leader;
    private final List<UUID> members = Lists.newArrayList();
    @Setter private int points = 0, tournamentWins = 0;

    public Clan(String name, UUID leader){
        this.name = name;
        this.leader = leader;
    }

    public String getColoredName(){
        return color + name;
    }

    public static Clan getByName(String name) {
        return clans.values().stream().filter(clan -> clan.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Clan getByPlayer(Player player){
        return clans.values().stream().filter(clan -> clan.getMembers().contains(player.getUniqueId())).findFirst().orElse(null);
    }

    public List<String> getOffPlayers(){
        return members.stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline())
                .map(UUID::toString)
                .collect(Collectors.toList());
    }

    public List<Player> getOnPlayers(){
        return members.stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());
    }

    public void show(Player player) {
        new MessageFormat(Locale.CLAN_SHOW.format(Profile.get(player.getUniqueId()).getLocale()))
            .add("{name}", name)
            .add("{members}", members())
            .add("{points}", String.valueOf(points))
            .add("{leader}", Objects.requireNonNull(Bukkit.getOfflinePlayer(leader)).getName())
            .add("{tournament_wins}", String.valueOf(tournamentWins))
            .send(player);
    }


    private String members() {
        List<String> names = Lists.newArrayList();
        names.addAll(getOnPlayers().stream().map(player -> ChatColor.GREEN + player.getName()).collect(Collectors.toList()));
        names.addAll(getOffPlayers().stream().map(name -> ChatColor.GRAY + name).collect(Collectors.toList()));
        return String.join("&7, ", names);
    }

    public void join(Player player) {
        broadcast(Locale.CLAN_JOIN_BROADCAST, new MessageFormat().add("{player_name}", player.getName()));
        new MessageFormat(Locale.CLAN_JOIN
                .format(Profile.get(player.getUniqueId()).getLocale()))
                .send(player);
        Profile profile = Profile.get(player.getUniqueId());
        profile.setClan(this);
        members.add(player.getUniqueId());
    }

    public void disband(Player player) {
        if(!player.getUniqueId().equals(leader) && !player.hasPermission("cpractice.clan.disband")){
            new MessageFormat(Locale.CLAN_ERROR_ONLY_OWNER
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }
        Profile leader = Profile.get(this.leader);
        leader.setClan(null);
        TaskUtil.runAsync(leader::save);
        TaskUtil.runAsync(() ->
            members.forEach(uuid -> {
            Profile profileMember = Profile.get(uuid);
            profileMember.setClan(null);
            profileMember.save();
        }));
        broadcast(Locale.CLAN_DISBAND, new MessageFormat().add("{player_name}", player.getName()));
        members.clear();

        delete();
        clans.remove(this.name);
    }

    private void delete() {
        if (Profile.getIProfile() instanceof MongoDBIProfile) {
            collection.deleteOne(Filters.eq("name", this.name));
        }
        else if (Profile.getIProfile() instanceof FlatFileIProfile) {
            config.set("clans." + this.name, null);
            cPractice.get().getClansConfig().save();
            cPractice.get().getClansConfig().reload();
        }
    }

    public void addPoints(int points){
        this.points += points;
    }

    public void removePoints(int points){
        this.points -= points;
    }

    public void chat(Player sender, String message) {
        getOnPlayers().forEach(player ->
                player.sendMessage(CC.translate(cPractice.get().getMainConfig().getString("CHAT.CLAN_MESSAGE_FORMAT")
                        .replace("{clan}", name)
                        .replace("{prefix}", cPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                        .replace("{suffix}", cPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                        .replace("{player}", sender.getName()))
                        .replace("{message}", message)));
    }

    public void broadcast(String msg){
        getOnPlayers().forEach(online -> online.sendMessage(CC.translate(msg)));
    }

    public void broadcast(Locale locale, MessageFormat messageFormat){
        getOnPlayers().forEach(online -> {
            messageFormat.setMessage(locale.format(Profile.get(online.getUniqueId()).getLocale()));
            messageFormat.send(online);
        });
    }

    public List<String> getClanScoreboard(){
        List<String> lines = Lists.newArrayList();
        BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
        String bars = config.getString("LINES.BARS");

        config.getStringList("CLAN.LINES").forEach(line -> {
            lines.add(line.replace("{bars}", bars)
                    .replace("{color}", getColor().toString())
                    .replace("{name}", getName())
                    .replace("{size}", String.valueOf(getOnPlayers().size())));
        });
        return lines;
    }

    public static void init(){
        if (Profile.getIProfile() instanceof MongoDBIProfile) {
            collection = cPractice.get().getMongoDatabase().getCollection("clans");
            collection.find().forEach((Block<Document>) document -> {
                Clan clan = new Clan(document.getString("name"), UUID.fromString(document.getString("owner")));
                if (document.containsKey("points")) clan.setPoints(document.getInteger("points"));
                if (document.containsKey("color")) clan.setColor(ChatColor.valueOf(document.getString("color")));
                if (document.containsKey("players")) {
                    if (document.get("players") instanceof String) {
                        JsonArray playersArray = new JsonParser().parse(document.getString("players")).getAsJsonArray();
                        for (JsonElement jsonElement : playersArray) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
                            clan.getMembers().add(uuid);
                        }
                    }
                }
                clans.put(clan.getName(), clan);
            });
        }
        else if (Profile.getIProfile() instanceof FlatFileIProfile) {
            config = cPractice.get().getClansConfig().getConfiguration().getConfigurationSection("clans");
            for (String key : config.getKeys(false)) {
                ConfigurationSection section = config.getConfigurationSection(key);
                Clan clan = new Clan(key, UUID.fromString(section.getString("owner")));
                if (section.contains("points")) clan.setPoints(section.getInt("points"));
                if (section.contains("color")) clan.setColor(ChatColor.valueOf(section.getString("color")));
                if (section.contains("players")) {
                    if (section.get("players") instanceof String) {
                        JsonArray playersArray = new JsonParser().parse(section.getString("players")).getAsJsonArray();
                        for (JsonElement jsonElement : playersArray) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
                            clan.getMembers().add(uuid);
                        }
                    }
                }
                clans.put(clan.getName(), clan);
            }
        }

        TaskUtil.runTimerAsync(() -> clans.values().forEach(Clan::save), 3600, 3600);
    }

    public void save(){
        save(true);
    }

    public void save(boolean async){
        if (Profile.getIProfile() instanceof MongoDBIProfile) {
            Document document = new Document();
            document.put("name", this.name);
            document.put("owner", this.leader.toString());
            document.put("points", this.points);
            document.put("color", this.color.name());
            JsonArray playersArray = new JsonArray();
            members.forEach(uuid -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("uuid", uuid.toString());
                playersArray.add(jsonObject);
            });
            if (playersArray.size() > 0) {
                document.put("players", playersArray.toString());
            }
            if(async){
                TaskUtil.runAsync(() ->
                        collection.replaceOne(Filters.eq("name", this.name), document, new ReplaceOptions().upsert(true)));
            }else {
                collection.replaceOne(Filters.eq("name", this.name), document, new ReplaceOptions().upsert(true));
            }
        }
        else if (Profile.getIProfile() instanceof FlatFileIProfile) {
            ConfigurationSection section;
            if (config.getConfigurationSection(this.name) != null) section = config.getConfigurationSection(this.name);
            else section = config.createSection(this.name);

            section.set("owner", this.leader.toString());
            section.set("points", this.points);
            section.set("color", this.color.name());
            JsonArray playersArray = new JsonArray();
            members.forEach(uuid -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("uuid", uuid.toString());
                playersArray.add(jsonObject);
            });
            if (playersArray.size() > 0) {
                section.set("players", playersArray.toString());
            }

            if (async) TaskUtil.runAsync(() -> {
                cPractice.get().getClansConfig().save();
                cPractice.get().getClansConfig().reload();
            });
            else {
                cPractice.get().getClansConfig().save();
                cPractice.get().getClansConfig().reload();
            }
        }
    }

    public void sendChat(Player player, String message) {
        sendMessage(Locale.CLAN_CHAT_REFIX.format(Profile.get(player.getUniqueId()).getLocale()) +
                player.getDisplayName() + ChatColor.RESET + ": " + message);
    }

    public void sendMessage(String message) {
        for (Player player : getOnPlayers()) {
            player.sendMessage(message);
        }
    }
}
