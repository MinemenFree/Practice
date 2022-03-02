package rip.crystal.practice.essentials.command.management;

import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import lombok.val;
import org.bukkit.command.CommandSender;

public class HysteriaReloadCommand extends BaseCommand {

    @Command(name = "hysteria", aliases = {"hpractice"}, inGameOnly = false, permission = "hysteria.owner")
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
            cPractice.get().getNpcConfig().reload();
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
            cPractice.get().getEssentials().setMotd(CC.translate(cPractice.get().getLangConfig().getStringList("MOTD")));
            val finish = System.currentTimeMillis();
            sender.sendMessage(CC.translate("&cHysteria reloaded &7(" + (finish - start) + "ms)"));
            return;
        }

        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&6&lh/cPractice Core"));
        sender.sendMessage(CC.translate("&fhPractice is created by &cziue &fmade for Hysteria Network."));
        sender.sendMessage(CC.CHAT_BAR);
    }
}