package rip.crystal.practice.essentials.command.donator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;

public class ShowAllPlayersCommand extends BaseCommand {

	@Command(name = "showallplayers", permission = "cpractice.command.showallplayers")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage("You are now showing all players.");

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			player.showPlayer(otherPlayer);
		}
	}
}
