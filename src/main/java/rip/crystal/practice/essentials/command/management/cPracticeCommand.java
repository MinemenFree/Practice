package rip.crystal.practice.essentials.command.management;

import rip.crystal.practice.api.chat.ChatUtil;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import lombok.val;
import org.bukkit.command.CommandSender;

public class cPracticeCommand extends BaseCommand {

    @Command(name = "cpractice", aliases = {"cpractice"}, inGameOnly = false, permission = "cpractice.owner")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();
        String[] args = commandArgs.getArgs();

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            val start = System.currentTimeMillis();
            cPractice.get().getMainConfig().reload();
            cPractice.get().getLang().reload();
            cPractice.get().getLangConfig().reload();
            cPractice.get().getHotbarConfig().reload();
            cPractice.get().getAbilityConfig().reload();
            cPractice.get().getArenasConfig().reload();
            cPractice.get().getEventsConfig().reload();
            cPractice.get().getKitsConfig().reload();
            cPractice.get().getPotionConfig().reload();
            cPractice.get().getKiteditorConfig().reload();
            cPractice.get().getLeaderboardConfig().reload();
            cPractice.get().getScoreboardConfig().reload();
            cPractice.get().getTabLobbyConfig().reload();
            cPractice.get().getTabSingleFFAFightConfig().reload();
            cPractice.get().getTabSingleTeamFightConfig().reload();
            cPractice.get().getTabPartyFFAFightConfig().reload();
            cPractice.get().getTabPartyTeamFightConfig().reload();
            cPractice.get().getTabEventConfig().reload();
            cPractice.get().getTabFFAConfig().reload();
            cPractice.get().getMenuConfig().reload();
            cPractice.get().getEssentials().setMotd(CC.translate(cPractice.get().getLangConfig().getStringList("MOTD")));
            val finish = System.currentTimeMillis();
            sender.sendMessage(CC.translate("&9cPractice reloaded &7(" + (finish - start) + "ms)"));
            return;
        }

        if(args.length > 0 && args[0].equalsIgnoreCase("help")) {
            CC.sender(sender, "&9&lSetup Commands");
            CC.sender(sender, "&f/" + "arena" + " &7- &7Setup arenas!");
            CC.sender(sender, "&f/" + "event" + " &7- &7Setup events!");
            CC.sender(sender, "&f/" + "kit" + " &7- &7Setup kits!");
            CC.sender(sender, ChatUtil.NORMAL_LINE);
            CC.sender(sender, "&9&lCoins Manager");
            CC.sender(sender, "&f/" + "coinsm" + " give <player> <amount> &7- &7Give coins to a player");
            CC.sender(sender, "&f/" + "coinsm" + " set <player> <amount> &7- &7Set coins for a player");
            CC.sender(sender, "&f/" + "coinsm" + " take <player> <amount> &7- &7Take coins from a player");
            CC.sender(sender, ChatUtil.NORMAL_LINE);
            CC.sender(sender, "&9&lPlayer Commands");
            CC.sender(sender, "&f/" + "settings" + " &7- &7View and change your player settings!");
            CC.sender(sender, "&f/" + "leaderboard" + " &7- &7View the leaderboards");
            CC.sender(sender, "&f/" + "fly" + " &7- &7Gives you the ability to fly!");
            CC.sender(sender, ChatUtil.NORMAL_LINE);
            return;
        }
        CC.sender(sender, ChatUtil.NORMAL_LINE);
        CC.sender(sender, "&9&lcPRACTICE &7made by &9ziue");
        CC.sender(sender, "&7Version: &9" + cPractice.get().getDescription().getVersion());
        CC.sender(sender, ChatUtil.NORMAL_LINE);
    }
}