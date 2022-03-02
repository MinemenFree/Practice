package rip.crystal.practice.category.commands;

import rip.crystal.practice.category.commands.subcommands.*;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.entity.Player;

import static rip.crystal.practice.utilities.chat.CC.translate;

public class CategoryCommand extends BaseCommand {

    public CategoryCommand() {
        super();
        new CreateCommand();
        new SetPointsCommand();
        new SetPrefixCommand();
        new InfoCommand();
        new ListCommand();
        new SetArmorCommand();
        new RemoveCommand();
    }

    @Command(name = "category", permission = "hysteria.category.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.SB_BAR);
        player.sendMessage(CC.translate("&c&lCategory Help"));
        player.sendMessage(CC.translate("&c/category setpoints &f(name) (points)"));
        player.sendMessage(CC.translate("&c/category setprefix &f(name) (prefix)"));
        player.sendMessage(CC.translate("&c/category setarmor &f(name)"));
        player.sendMessage(CC.translate("&c/category create &f(name)"));
        player.sendMessage(CC.translate("&c/category remove &f(name)"));
        player.sendMessage(CC.translate("&c/category info &f(name)"));
        player.sendMessage(CC.translate("&c/category list"));
        player.sendMessage(CC.SB_BAR);
    }
}