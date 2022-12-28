package rip.crystal.practice.essentials.command.management;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.Practice;
import rip.crystal.practice.utilities.chat.CC;

public class AdminInformationCommand extends BaseCommand {

    @Command(name = "admin", aliases = {"admininformation"}, permission = "practice.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate(" &7▢ &9Plugin: &r" + "Practice"));
        player.sendMessage(CC.translate(" &7▢ &9Version: &r" + "1.0"));
        player.sendMessage(CC.translate(" &7▢ &9Developer: &r" + "MinemenFree"));
        player.sendMessage(CC.CHAT_BAR);
    }
}
