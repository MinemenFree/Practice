package rip.crystal.practice.essentials.command.staff;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class HealCommand extends BaseCommand {

	@Command(name = "heal", permission = "cpractice.command.heal")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.setHealth(20.0);
			player.setFoodLevel(20);
			player.setSaturation(5.0F);
			player.updateInventory();
			player.sendMessage(CC.GOLD + "You healed yourself.");
		} else {
			Player target = commandArgs.getPlayer();
			if (target == null) {
				new MessageFormat(Locale.PLAYER_NOT_FOUND
						.format(Profile.get(player.getUniqueId()).getLocale()))
						.send(player);
				return;
			}
			target.setHealth(20.0);
			target.setFoodLevel(20);
			target.setSaturation(5.0F);
			target.updateInventory();
			target.sendMessage(CC.GOLD + "You have been healed by " + player.getName());
		}
	}
}
