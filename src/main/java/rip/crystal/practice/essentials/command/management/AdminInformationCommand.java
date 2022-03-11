package rip.crystal.practice.essentials.command.management;

import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class AdminInformationCommand extends BaseCommand {

    @Command(name = "admin", aliases = {"admininformation"}, permission = "cpractice.owner")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&9Plugin: &r" + "cPractice"));
        player.sendMessage(CC.translate("&9License: &r" + cPractice.get().getMainConfig().getString("LICENSE")));
        player.sendMessage(CC.translate("&9Developer: &r" + "ziue"));
        player.sendMessage(CC.CHAT_BAR);
    }
}
