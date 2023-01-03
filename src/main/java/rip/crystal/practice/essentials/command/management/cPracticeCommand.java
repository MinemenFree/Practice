package rip.crystal.practice.essentials.command.management;

import lombok.val;
import org.bukkit.command.CommandSender;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.chat.CC;

public class cPracticeCommand extends BaseCommand {

    @Command(name = "cpractice", aliases = {"cpractice"}, inGameOnly = false, permission = "cpractice.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();
        String[] args = commandArgs.getArgs();

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            val start = System.currentTimeMillis();
            cPractice.get().getMainConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getLang().reload();
            sender.sendMessage(CC.translate("&7▢ &9Lang reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getLangConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Lang Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getHotbarConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Hotbar Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getAbilityConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Ability Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getArenasConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Arena Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getEventsConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Events Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getKitsConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Kits Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getKiteditorConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Kit Editor Menu Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getLeaderboardConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Leaderboard Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getScoreboardConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Scoreboard Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getTabLobbyConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Lobby Tablist Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getTabSingleFFAFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Single FFA Fight Tablist Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getTabSingleTeamFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Single Team Fight Tablist Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getTabPartyFFAFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Party FFA Fight Tablist Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getTabPartyTeamFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Party Team Fight Tablist Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getTabEventConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Events Tablist Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getTabFFAConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9FFA Tablist Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getMenuConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &9Menu Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            cPractice.get().getEssentials().setMotd(CC.translate(cPractice.get().getLangConfig().getStringList("MOTD")));
            sender.sendMessage(CC.translate("&7▢ &9MOTD Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            val finish = System.currentTimeMillis();
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &9cPractice &fhas been reloaded. &8(&7" + (finish - start) + "&8) &7▢"));
            sender.sendMessage(CC.CHAT_BAR);
            return;
        }

        if(args.length > 0 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate("&9&lSetup &7&m-&r &9&lHelp"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &9/arena &8(&7&oSetup arenas&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/kit &8(&7&oSetup kits&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/event &8(&7&oSetup events&8&o)"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate("&9&lCommands &7&m-&r &9&lHelp"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &9/cpractice &8(&7&oMain commands&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/leaderboard &8(&7&oView leaderboard&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/settings &8(&7&oView player settings&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/party &8(&7&oParty commands&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/clan &8(&7&oClan commands&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/stats &8(&7&oView player stats&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/fly &8(&7&oToggle flight&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &9/ffa &8(&7&oFFA commands&8&o)"));
            sender.sendMessage(CC.CHAT_BAR);
            return;
        }
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&9cPractice"));
        sender.sendMessage(CC.translate("&7Version: &9" + cPractice.get().getDescription().getVersion()));
        sender.sendMessage(CC.translate("&7License: &9" + cPractice.get().getMainConfig().getString("LICENSE")));
        sender.sendMessage(CC.translate("&7Developer: &9" + "MinemenFree"));
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&9Admin &7&m-&r &9Help"));
        sender.sendMessage(CC.translate(" &7▢ &9/cpractice reload &8(&7&oReload configs&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &9/cpractice help &8(&7&oView help command&8&o)"));
        sender.sendMessage(CC.CHAT_BAR);
    }
}
