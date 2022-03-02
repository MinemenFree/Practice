package rip.crystal.practice.category.commands.subcommands;

import rip.crystal.practice.category.Category;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetPrefixCommand extends BaseCommand {

    @Command(name = "category.setprefix", permission = "hysteria.category.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Category category = Category.getCategory(args[0]);
        if (category == null) {
            player.sendMessage(CC.RED + "This category doesn't exist.");
            return;
        }

        String prefix = args[1];
        if (prefix == null) {
            player.sendMessage(CC.RED + "Please insert a prefix.");
            return;
        }

        category.setPrefix(prefix);
        category.save();
        player.sendMessage(ChatColor.GREEN + "Prefix of " + category.getName() + " has been updated to " + category.getPrefix());
    }
}