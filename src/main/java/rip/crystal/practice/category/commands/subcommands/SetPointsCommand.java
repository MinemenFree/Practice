package rip.crystal.practice.category.commands.subcommands;

import rip.crystal.practice.category.Category;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetPointsCommand extends BaseCommand {

    @Command(name = "category.setpoints", permission = "hysteria.category.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Category category = Category.getCategory(args[0]);
        if (category == null) {
            player.sendMessage(CC.RED + "This category doesn't exist.");
            return;
        }

        int points;
        if (!StringUtils.isNumeric(args[1])) {
            player.sendMessage(CC.RED + "Please insert a valid number to points.");
            return;
        }
        points = Integer.getInteger(args[1]);

        category.setRequiredPoints(points);
        category.save();
        player.sendMessage(ChatColor.GREEN + "Required points for " + category.getName() + " has been updated to " + points);
    }
}