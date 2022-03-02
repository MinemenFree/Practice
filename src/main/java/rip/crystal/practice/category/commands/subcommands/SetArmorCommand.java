package rip.crystal.practice.category.commands.subcommands;

import rip.crystal.practice.category.Category;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import net.ghoulpvp.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetArmorCommand extends BaseCommand {

    @Command(name = "category.setarmor", permission = "hysteria.category.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Category category = Category.getCategory(args[0]);
        if (category == null) {
            player.sendMessage(CC.RED + "This category doesn't exist.");
            return;
        }

        category.setArmor(player.getInventory().getArmorContents());
        player.sendMessage(ChatColor.GREEN + "Armor of " + category.getName() + " has been updated");
        category.save();
    }
}