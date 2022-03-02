package rip.crystal.practice.arena.command;

import rip.crystal.practice.arena.Arena;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaSaveCommand extends BaseCommand {

	@Command(name = "arena.save", permission = "hysteria.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		Arena.getArenas().forEach(Arena::save);
		player.sendMessage(ChatColor.GREEN + "Saved all arenas!");
	}

}
