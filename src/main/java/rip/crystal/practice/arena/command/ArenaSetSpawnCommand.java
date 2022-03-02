package rip.crystal.practice.arena.command;

import rip.crystal.practice.arena.Arena;
import rip.crystal.practice.arena.cuboid.Cuboid;
import rip.crystal.practice.arena.impl.StandaloneArena;
import rip.crystal.practice.arena.selection.Selection;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ArenaSetSpawnCommand extends BaseCommand {

	@Command(name = "arena.setspawn", permission = "hysteria.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.translate("&cPlease usage: /arena setspawn (arena) (a/b/red/blue)"));
			player.sendMessage(CC.CHAT_BAR);
			return;
		}
		Arena arena = Arena.getByName(args[0]);
		String pos = args[1];

		if (arena != null) {
			if (pos.equalsIgnoreCase("a")) {
				arena.setSpawnA(player.getLocation());
			} else if (pos.equalsIgnoreCase("b")) {
				arena.setSpawnB(player.getLocation());
			}else if (pos.equalsIgnoreCase("red")) {
				if(!(arena instanceof StandaloneArena)){
					player.sendMessage(CC.CHAT_BAR);
					player.sendMessage("Only StandAloneArena allow this");
					player.sendMessage(CC.CHAT_BAR);
					return;
				}
				StandaloneArena standaloneArena = (StandaloneArena) arena;
				Selection selection = Selection.createOrGetSelection(player);
				if (!selection.isFullObject()) {
					player.sendMessage(CC.CHAT_BAR);
					player.sendMessage(CC.RED + "Your selection is incomplete.");
					player.sendMessage(CC.CHAT_BAR);
					return;
				}
				standaloneArena.setSpawnRed(new Cuboid(selection.getPoint1(), selection.getPoint2()));
			}else if (pos.equalsIgnoreCase("blue")) {
				if(!(arena instanceof StandaloneArena)){
					player.sendMessage(CC.CHAT_BAR);
					player.sendMessage("Only StandAloneArena allow this");
					player.sendMessage(CC.CHAT_BAR);
					return;
				}
				StandaloneArena standaloneArena = (StandaloneArena) arena;
				Selection selection = Selection.createOrGetSelection(player);
				if (!selection.isFullObject()){
					player.sendMessage(CC.CHAT_BAR);
					player.sendMessage(CC.RED + "Your selection is incomplete.");
					player.sendMessage(CC.CHAT_BAR);
					return;
				}
				standaloneArena.setSpawnBlue(new Cuboid(selection.getPoint1(), selection.getPoint2()));
			} else {
				player.sendMessage(CC.CHAT_BAR);
				player.sendMessage(CC.RED + "Invalid spawn point. Try \"a\" or \"b\".");
				player.sendMessage(CC.CHAT_BAR);
				return;
			}

			arena.save();

			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.RED + "Updated spawn point&f \"" + pos + "\" &cfor arena&f \"" + arena.getName() + "\"");
			player.sendMessage(CC.CHAT_BAR);
		} else {

			player.sendMessage(CC.RED + "An arena with that name already exists.");
		}
	}
}
