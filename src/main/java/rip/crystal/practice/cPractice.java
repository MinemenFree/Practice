package rip.crystal.practice;

import rip.crystal.practice.abilities.AbilityManager;
import rip.crystal.practice.abilities.command.AbilityCommand;
import rip.crystal.practice.arena.Arena;
import rip.crystal.practice.arena.ArenaListener;
import rip.crystal.practice.arena.command.ArenaCommand;
import rip.crystal.practice.arena.command.ArenasCommand;
import rip.crystal.practice.category.Category;
import rip.crystal.practice.category.commands.CategoryCommand;
import rip.crystal.practice.chat.hPracticeChatFormat;
import rip.crystal.practice.chat.impl.Chat;
import rip.crystal.practice.chat.impl.ChatListener;
import rip.crystal.practice.chat.impl.command.ClearChatCommand;
import rip.crystal.practice.chat.impl.command.MuteChatCommand;
import rip.crystal.practice.chat.impl.command.SlowChatCommand;
import rip.crystal.practice.clan.Clan;
import rip.crystal.practice.clan.ClanListener;
import rip.crystal.practice.clan.commands.ClanCommand;
import rip.crystal.practice.cosmetics.command.CosmeticsCommand;
import rip.crystal.practice.cosmetics.impl.killeffects.command.KillEffectCommand;
import rip.crystal.practice.duel.command.*;
import rip.crystal.practice.essentials.Essentials;
import rip.crystal.practice.essentials.EssentialsListener;
import rip.crystal.practice.essentials.command.donator.RenameCommand;
import rip.crystal.practice.essentials.command.donator.ShowAllPlayersCommand;
import rip.crystal.practice.essentials.command.donator.ShowPlayerCommand;
import rip.crystal.practice.essentials.command.management.AdminInformationCommand;
import rip.crystal.practice.essentials.command.management.HysteriaReloadCommand;
import rip.crystal.practice.essentials.command.management.SetSlotsCommand;
import rip.crystal.practice.essentials.command.management.SetSpawnCommand;
import rip.crystal.practice.essentials.command.player.LangCommand;
import rip.crystal.practice.essentials.command.player.PingCommand;
import rip.crystal.practice.essentials.command.player.ResetCommand;
import rip.crystal.practice.essentials.command.player.SpawnCommand;
import rip.crystal.practice.essentials.command.staff.*;
import rip.crystal.practice.event.Event;
import rip.crystal.practice.event.command.EventCommand;
import rip.crystal.practice.event.command.EventsCommand;
import rip.crystal.practice.event.game.EventGame;
import rip.crystal.practice.event.game.EventGameListener;
import rip.crystal.practice.event.game.command.EventHostCommand;
import rip.crystal.practice.event.game.map.EventGameMap;
import rip.crystal.practice.event.game.map.vote.command.EventMapVoteCommand;
import rip.crystal.practice.event.impl.spleef.SpleefGameLogic;
import rip.crystal.practice.event.impl.tntrun.TNTRunGameLogic;
import rip.crystal.practice.ffa.FFAListener;
import rip.crystal.practice.ffa.FFAManager;
import rip.crystal.practice.ffa.command.FFACommand;
import rip.crystal.practice.kit.Kit;
import rip.crystal.practice.kit.KitEditorListener;
import rip.crystal.practice.kit.command.HCFClassCommand;
import rip.crystal.practice.kit.command.KitCommand;
import rip.crystal.practice.kit.command.KitsCommand;
import rip.crystal.practice.knockback.Knockback;
import rip.crystal.practice.leaderboard.Leaderboard;
import rip.crystal.practice.leaderboard.LeaderboardListener;
import rip.crystal.practice.leaderboard.PlaceholderAPI;
import rip.crystal.practice.leaderboard.commands.*;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchListener;
import rip.crystal.practice.match.command.CancelMatchCommand;
import rip.crystal.practice.match.command.SpectateCommand;
import rip.crystal.practice.match.command.StopSpectatingCommand;
import rip.crystal.practice.match.command.ViewInventoryCommand;
import rip.crystal.practice.nametags.GxNameTag;
import rip.crystal.practice.nametags.hPracticeTags;
import rip.crystal.practice.party.Party;
import rip.crystal.practice.party.PartyListener;
import rip.crystal.practice.party.classes.ClassTask;
import rip.crystal.practice.party.classes.archer.ArcherClass;
import rip.crystal.practice.party.classes.bard.BardEnergyTask;
import rip.crystal.practice.party.classes.bard.BardListener;
import rip.crystal.practice.party.classes.rogue.RogueClass;
import rip.crystal.practice.party.command.PartyCommand;
import rip.crystal.practice.pingfactor.PingFactorCommand;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileListener;
import rip.crystal.practice.profile.command.FlyCommand;
import rip.crystal.practice.profile.command.ViewMatchCommand;
import rip.crystal.practice.profile.conversation.command.Configurator;
import rip.crystal.practice.profile.conversation.command.MessageCommand;
import rip.crystal.practice.profile.conversation.command.ReplyCommand;
import rip.crystal.practice.profile.file.impl.FlatFileIProfile;
import rip.crystal.practice.profile.file.impl.MongoDBIProfile;
import rip.crystal.practice.profile.hotbar.Hotbar;
import rip.crystal.practice.profile.meta.option.command.*;
import rip.crystal.practice.profile.modmode.ModmodeListener;
import rip.crystal.practice.profile.modmode.commands.StaffModeCommand;
import rip.crystal.practice.queue.Queue;
import rip.crystal.practice.queue.QueueListener;
import rip.crystal.practice.scoreboard.BoardAdapter;
import rip.crystal.practice.tablist.TabAdapter;
import rip.crystal.practice.tablist.impl.TabList;
import rip.crystal.practice.tournament.TournamentListener;
import rip.crystal.practice.tournament.commands.TournamentCommand;
import rip.crystal.practice.utilities.Animation;
import rip.crystal.practice.utilities.InventoryUtil;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.language.LanguageConfigurationFile;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import rip.crystal.practice.utilities.menu.MenuListener;
import rip.crystal.practice.utilities.playerversion.PlayerVersionHandler;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import rip.crystal.api.command.CommandManager;
import rip.crystal.api.rank.RankManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter @Setter
public class cPractice extends JavaPlugin {

    private LanguageConfigurationFile lang;
    private BasicConfigurationFile mainConfig, databaseConfig, arenasConfig, kitsConfig, eventsConfig,
            scoreboardConfig, coloredRanksConfig, tabLobbyConfig, tabEventConfig, tabSingleFFAFightConfig,
            tabSingleTeamFightConfig, tabPartyFFAFightConfig, tabPartyTeamFightConfig, leaderboardConfig,
            langConfig, hotbarConfig, playersConfig, clansConfig, categoriesConfig, abilityConfig, kiteditorConfig,
            npcConfig, queueConfig, lunarConfig, tabFFAConfig, potionConfig;
    private Configurator configgg;
    private Essentials essentials;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private RankManager rankManager;
    private AbilityManager abilityManager;
    private FFAManager ffaManager;
    public boolean placeholderAPI = false;
    public boolean lunarClient = false;
    public int inQueues, inFights, bridgeRounds, rankedSumoRounds;

    @Override
    public void onEnable() {
        loadConfig();

        loadSaveMethod();
        loadEssentials();
        initManagers();

        registerNameTags();

        registerCommands();
        registerListeners();

        removeCrafting();

        setUpWorld();
        runTasks();

        CC.loadPlugin();

    }

    @Override
    public void onDisable() {
        Profile.getProfiles().values().stream().filter(Profile::isOnline).forEach(Profile::save);
        if (EventGame.getActiveGame() != null) {
            if (EventGame.getActiveGame().getGameLogic() instanceof SpleefGameLogic) {
                SpleefGameLogic event = (SpleefGameLogic) EventGame.getActiveGame().getGameLogic();
                event.endEvent();
            } else if (EventGame.getActiveGame().getGameLogic() instanceof TNTRunGameLogic) {
                TNTRunGameLogic event = (TNTRunGameLogic) EventGame.getActiveGame().getGameLogic();
                event.endEvent();
            }
        }
        Match.cleanup();
        Clan.getClans().values().forEach(clan -> clan.save(false));
        Kit.getKits().forEach(Kit::save);
        Arena.getArenas().forEach(Arena::save);
    }

    private void initManagers() {
        this.essentials = new Essentials(this);
        this.rankManager = new RankManager(this);
        this.rankManager.loadRank();

        this.abilityManager = new AbilityManager();
        this.abilityManager.load();

        this.ffaManager = new FFAManager();

        Hotbar.init();
        Kit.init();
        Arena.init();
        Profile.init();
        Match.init();
        Party.init();
        Knockback.init();
        Event.init();
        EventGameMap.init();
        Category.init();
        Clan.init();
        Queue.init();
        Animation.init();
        GxNameTag.hook();
        BoardAdapter.hook();
        Leaderboard.init();
        PlayerVersionHandler.init();
        Chat.setChatFormat(new hPracticeChatFormat());
        if (mainConfig.getBoolean("TABLIST_ENABLE")) new TabList(this, new TabAdapter());
        placeholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholderAPI) new PlaceholderAPI().register();
    }

    private void loadConfig() {
        this.mainConfig = new BasicConfigurationFile(this, "config");
        this.lang = new LanguageConfigurationFile(this, "lang");
        this.databaseConfig = new BasicConfigurationFile(this, "database");
        this.coloredRanksConfig = new BasicConfigurationFile(this, "colored-ranks");
        this.arenasConfig = new BasicConfigurationFile(this, "arenas");
        this.kitsConfig = new BasicConfigurationFile(this, "kits");
        this.eventsConfig = new BasicConfigurationFile(this, "events");
        this.scoreboardConfig = new BasicConfigurationFile(this, "scoreboard");
        this.leaderboardConfig = new BasicConfigurationFile(this, "leaderboard");
        this.langConfig = new BasicConfigurationFile(this, "global-lang");
        this.hotbarConfig = new BasicConfigurationFile(this, "hotbar");
        this.abilityConfig = new BasicConfigurationFile(this, "ability");
        this.npcConfig = new BasicConfigurationFile(this, "npc");
        this.kiteditorConfig = new BasicConfigurationFile(this, "kiteditor");
        this.potionConfig = new BasicConfigurationFile(this, "potion");
        this.tabEventConfig = new BasicConfigurationFile(this, "tablist/event");
        this.tabLobbyConfig = new BasicConfigurationFile(this, "tablist/lobby");
        this.tabFFAConfig = new BasicConfigurationFile(this, "tablist/ffa");
        this.tabSingleFFAFightConfig = new BasicConfigurationFile(this, "tablist/SingleFFAFight");
        this.tabSingleTeamFightConfig = new BasicConfigurationFile(this, "tablist/SingleTeamFight");
        this.tabPartyFFAFightConfig = new BasicConfigurationFile(this, "tablist/PartyFFAFight");
        this.tabPartyTeamFightConfig = new BasicConfigurationFile(this, "tablist/PartyTeamFight");
        this.configgg = new Configurator();
        this.configgg.loadConfig5();
        if (mainConfig.getString("SAVE_METHOD").equals("FILE") || mainConfig.getString("SAVE_METHOD").equals("FLATFILE")) { this.configgg.loadConfig();
            this.playersConfig = new BasicConfigurationFile(this, "players");
            this.clansConfig = new BasicConfigurationFile(this, "clans");
            this.categoriesConfig = new BasicConfigurationFile(this, "categories");
        }
    }

    private void registerNameTags() {
        GxNameTag.registerProvider(new hPracticeTags());
    }


    private void loadSaveMethod() {
        switch (mainConfig.getString("SAVE_METHOD")) {
            case "MONGO": case "MONGODB":
                Profile.iProfile = new MongoDBIProfile();
                break;
            case "FLATFILE": case "FILE":
                Profile.iProfile = new FlatFileIProfile();
                break;
        }

        if (Profile.getIProfile() instanceof MongoDBIProfile) {
            try {
                if (databaseConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
                    mongoDatabase = new MongoClient(new ServerAddress(databaseConfig.getString("MONGO.HOST"), databaseConfig.getInteger("MONGO.PORT")
                            ),
                            MongoCredential.createCredential(
                                    databaseConfig.getString("MONGO.AUTHENTICATION.USERNAME"),
                                    databaseConfig.getString("MONGO.AUTHENTICATION.DATABASE"),
                                    databaseConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray()
                            ),
                            MongoClientOptions.builder().build()
                    ).getDatabase(databaseConfig.getString("MONGO.DATABASE"));
                } else {
                    mongoDatabase = new MongoClient(databaseConfig.getString("MONGO.HOST"), databaseConfig.getInteger("MONGO.PORT"))
                            .getDatabase(databaseConfig.getString("MONGO.DATABASE"));
                }
            } catch (Exception e) {
                System.out.println("The cPractice plugin was disabled as it failed to connect to the MongoDB");
                Bukkit.getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    private void runTasks() {
        TaskUtil.runTimer(() -> Bukkit.getOnlinePlayers().forEach(player -> Bukkit.getOnlinePlayers().forEach(other -> TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(player, other)))), 20L, 20L);
        TaskUtil.runTimerAsync(new ClassTask(), 5L, 5L);
        TaskUtil.runTimer(new BardEnergyTask(), 15L, 20L);
        TaskUtil.runTimer(() ->
            Profile.getProfiles().values().stream()
                    .filter(profile -> profile.getPlayer() != null && profile.getPlayer().isOnline())
                    .filter(profile -> profile.getRematchData() != null)
                    .forEach(profile -> profile.getRematchData().validate())
                , 20L, 20L);
    }

    private void setUpWorld() {
        // Set the difficulty for each world to HARD
        // Clear the droppedItems for each world
        getServer().getWorlds().forEach(world -> {
            world.setDifficulty(Difficulty.HARD);
            world.setGameRuleValue("doDaylightCycle", "false");
            getEssentials().clearEntities(world);
        });
    }

    private void removeCrafting() {
        Arrays.asList(
                Material.WORKBENCH,
                Material.STICK,
                Material.WOOD_PLATE,
                Material.WOOD_BUTTON,
                Material.SNOW_BLOCK,
                Material.STONE_BUTTON
        ).forEach(InventoryUtil::removeCrafting);
    }

    private void registerListeners() {
        Arrays.asList(
                new KitEditorListener(),
                new PartyListener(),
                new ProfileListener(),
                new MatchListener(),
                new QueueListener(),
                new ArenaListener(),
                new EventGameListener(),
                new BardListener(),
                new ArcherClass(),
                new RogueClass(),
                new ClanListener(),
                new EssentialsListener(),
                new MenuListener(),
                new ChatListener(),
                new LeaderboardListener(),
                new TournamentListener(),
                new FFAListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        if (getMainConfig().getBoolean("MOD_MODE")) getServer().getPluginManager().registerEvents(new ModmodeListener(), this);
    }

    public void registerCommands() {
        new CommandManager(this);
        if (mainConfig.getBoolean("MESSAGE-REPLY-BOOLEAN")) {
            new MessageCommand();
            new ReplyCommand();
        }
        new CosmeticsCommand();
        new KillEffectCommand();
        new TrollCommand();
        new FFACommand();
        new ArenaCommand();
        new ArenasCommand();
        new DuelCommand();
        new DuelRoundCommand();
        new DuelAcceptCommand();
        new EventCommand();
        new EventHostCommand();
        new EventsCommand();
        new EventMapVoteCommand();
        new RematchCommand();
        new SpectateCommand();
        new CancelMatchCommand();
        new StopSpectatingCommand();
        new FlyCommand();
        new ViewMatchCommand();
        new PartyCommand();
        new KitCommand();
        new KitsCommand();
        new ViewInventoryCommand();
        new ToggleScoreboardCommand();
        new ToggleSpectatorsCommand();
        new ToggleDuelRequestsCommand();
        new ClanCommand();
        new CategoryCommand();
        new TournamentCommand();
        new ClearCommand();
        new DayCommand();
        new GameModeCommand();
        new AbilityCommand();
        new HysteriaReloadCommand();
        new HealCommand();
        new LangCommand();
        new LocationCommand();
        new MoreCommand();
        new NightCommand();
        new PingCommand();
        new RenameCommand();
        new SetSlotsCommand();
        new SetSpawnCommand();
        new ShowAllPlayersCommand();
        new ShowPlayerCommand();
        new SpawnCommand();
        new SudoAllCommand();
        new SudoCommand();
        new SunsetCommand();
        new TeleportWorldCommand();
        new OptionsCommand();
        new ClearChatCommand();
        new SlowChatCommand();
        new MuteChatCommand();
        new EloCommand();
        new SetEloCommand();
        new ResetEloCommand();
        new AdminInformationCommand();
        new CreateWorldCommand();
        new StatsCommand();
        new LeaderboardCommand();
        new RankedCommand();
        new UnRankedCommand();
        new HCFClassCommand();
        new ResetCommand();
        new ToggleGlobalChatCommand();
        new TogglePrivateMessagesCommand();
        new ToggleScoreboardCommand();
        new ToggleSoundsCommand();
        new ToggleSpectatorsCommand();
        new PingFactorCommand();
        if (getMainConfig().getBoolean("MOD_MODE")) new StaffModeCommand();
    }

    private void loadEssentials() {
        this.bridgeRounds = getMainConfig().getInteger("MATCH.ROUNDS_BRIDGE");
        this.rankedSumoRounds = getMainConfig().getInteger("MATCH.ROUNDS_RANKED_SUMO");
    }

    public static cPractice get(){
        return getPlugin(cPractice.class);
    }
}
