package rip.crystal.practice.essentials.command.management;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.chat.CC;

public class AdminInformationCommand extends BaseCommand {

    @Command(name = "admin", aliases = {"admininformation"}, permission = "cpractice.owner")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate(" &7▢ &9Plugin: &r" + "cPractice"));
        player.sendMessage(CC.translate(" &7▢ &9Version: &r" + cPractice.get().getDescription().getVersion()));
        player.sendMessage(CC.translate(" &7▢ &9License: &r" + cPractice.get().getMainConfig().getString("LICENSE")));
        player.sendMessage(CC.translate(" &7▢ &9Developer: &r" + "ziue"));
        player.sendMessage(CC.CHAT_BAR);
    }
}
