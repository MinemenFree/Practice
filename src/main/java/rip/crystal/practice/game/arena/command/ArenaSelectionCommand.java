package rip.crystal.practice.game.arena.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.arena.selection.Selection;

public class ArenaSelectionCommand extends BaseCommand {

	@Command(name = "arena.wand", aliases = {"arena.selection"}, permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		if (player.getInventory().first(Selection.SELECTION_WAND) != -1) {
			player.getInventory().remove(Selection.SELECTION_WAND);
		} else {
			player.getInventory().addItem(Selection.SELECTION_WAND);
		}

		player.updateInventory();
	}
}
