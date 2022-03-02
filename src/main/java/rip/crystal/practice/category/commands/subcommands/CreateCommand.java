package rip.crystal.practice.category.commands.subcommands;

import rip.crystal.practice.category.Category;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CreateCommand extends BaseCommand {

    @Command(name = "category.create")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        String name = args[0];
        if (Category.getCategory(name) != null) {
            player.sendMessage(ChatColor.RED + "Category is ready created.");
            return;
        }

        Category category = new Category(name);

        player.sendMessage(category.getPrefix() + ChatColor.GREEN + " created.");
        category.save();
        Category.getCategories().put(name, category);
    }
}