package rip.crystal.practice.game.arena.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.arena.Arena;

public class ArenaSaveCommand extends BaseCommand {

	@Command(name = "arena.save", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		Arena.getArenas().forEach(Arena::save);
		player.sendMessage(ChatColor.GREEN + "Saved all arenas!");
	}

}
