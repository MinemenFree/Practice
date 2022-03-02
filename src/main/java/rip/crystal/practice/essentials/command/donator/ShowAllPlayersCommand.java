package rip.crystal.practice.essentials.command.donator;

import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ShowAllPlayersCommand extends BaseCommand {

	@Command(name = "showallplayers", permission = "hysteria.command.showallplayers")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage("You are now showing all players.");

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			player.showPlayer(otherPlayer);
		}
	}
}
