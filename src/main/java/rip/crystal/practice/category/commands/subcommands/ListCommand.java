package rip.crystal.practice.category.commands.subcommands;

import rip.crystal.practice.category.Category;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

import static rip.crystal.practice.utilities.chat.CC.translate;

public class ListCommand extends BaseCommand {

    @Command(name = "category.list")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.translate("&c&lCategory list"));

        Category.getCategories().values().forEach(category -> player.sendMessage(CC.translate("&7- &c" + category.getName())));
    }
}