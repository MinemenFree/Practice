package rip.crystal.practice.game.arena.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.ArenaType;
import rip.crystal.practice.game.arena.impl.SharedArena;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.game.arena.selection.Selection;
import rip.crystal.practice.utilities.chat.CC;

import java.util.Arrays;

public class ArenaCreateCommand extends BaseCommand {

	@Command(name = "arena.create", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.translate("&cPlease usage: /arena create (name) (type)"));
			return;
		}

		String arenaName = args[0];
		ArenaType arenaType = Arrays.stream(ArenaType.values())
				.filter(val -> val.name().equalsIgnoreCase(args[1])).findFirst().orElse(null);

		if (arenaType == null) {
			player.sendMessage(CC.translate("&cPlease usage a valid ArenaType: SHARED, STANDALONE"));
			return;
		}

		if (Arena.getByName(arenaName) == null) {
			Selection selection = Selection.createOrGetSelection(player);

			if (selection.isFullObject()) {
				if (arenaType == ArenaType.SHARED) {
					Arena arena = new SharedArena(arenaName, selection.getPoint1(), selection.getPoint2());
					Arena.getArenas().add(arena);

					player.sendMessage(CC.translate("&cCreated new Shared arena &f\"" + arenaName + "\""));
				} else if (arenaType == ArenaType.STANDALONE) {
					Arena arena = new StandaloneArena(arenaName, selection.getPoint1(), selection.getPoint2());
					Arena.getArenas().add(arena);

					player.sendMessage(CC.translate("&cCreated new Standalone arena&f \"" + arenaName + "\""));
				}
			} else {
				player.sendMessage(CC.RED + "Your selection is incomplete.");
			}
		} else {
			player.sendMessage(CC.RED + "An arena with that name already exists.");
		}
	}

}
