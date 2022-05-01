package rip.crystal.practice.essentials.command.staff;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class ClearCommand extends BaseCommand {

	@Command(name = "clearinv", aliases = {"clear", "ci"}, permission = "cpractice.command.clearinv")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.getInventory().setContents(new ItemStack[36]);
			player.getInventory().setArmorContents(new ItemStack[4]);
			player.updateInventory();
			player.sendMessage(CC.RED + "You cleared your inventory.");
		}
		else {
		    Player target = Bukkit.getPlayer(args[0]);
		    if (target == null) {
				new MessageFormat(Locale.PLAYER_NOT_FOUND
						.format(Profile.get(player.getUniqueId()).getLocale()))
						.send(player);
		        return;
            }
            target.getInventory().setContents(new ItemStack[36]);
            target.getInventory().setArmorContents(new ItemStack[4]);
            target.updateInventory();
            target.sendMessage(CC.WHITE + "Your inventory has been cleared by &c" + player.getName());
        }
	}
}
