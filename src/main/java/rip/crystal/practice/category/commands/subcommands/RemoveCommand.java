package rip.crystal.practice.category.commands.subcommands;

import rip.crystal.practice.category.Category;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class RemoveCommand extends BaseCommand {

    @Command(name = "category.remove", permission = "hysteria.category.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Category category = Category.getCategory(args[0]);
        if (category == null) {
            player.sendMessage(CC.RED + "This category doesn't exist.");
            return;
        }

        category.delete();
        player.sendMessage(CC.translate("&cCategory delete."));
    }
}