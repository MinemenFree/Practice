package rip.crystal.practice.game.arena.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.cuboid.Cuboid;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.game.arena.selection.Selection;
import rip.crystal.practice.utilities.LocationUtil;
import rip.crystal.practice.utilities.chat.CC;

import java.io.IOException;

public class ArenaSetSpawnCommand extends BaseCommand {

	@Setter
	@Getter
	private Location spawn;

	@Command(name = "arena.setspawn", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.translate("&cPlease usage: /arena setspawn (arena) (a/b/red/blue)"));
			player.sendMessage(CC.translate("&cPlease usage: /arena setspawn (safezone)"));
			player.sendMessage(CC.CHAT_BAR);
			return;
		}

		if (args[0].equalsIgnoreCase("safezone")) {
			Selection selection = Selection.createOrGetSelection(player);
			if (!selection.isFullObject()) {
				player.sendMessage(CC.CHAT_BAR);
				player.sendMessage(CC.RED + "Your selection is incomplete.");
				player.sendMessage(CC.CHAT_BAR);
				return;
			}
			cPractice.get().getFfaManager().setFfaSafezone(new Cuboid(selection.getPoint1(), selection.getPoint2()));
			FileConfiguration configuration = cPractice.get().getMainConfig().getConfiguration();
			configuration.set("FFA.SAFEZONE.location1", LocationUtil.serialize(cPractice.get().getFfaManager().getFfaSafezone().getLowerCorner()));
			configuration.set("FFA.SAFEZONE.location2", LocationUtil.serialize(cPractice.get().getFfaManager().getFfaSafezone().getUpperCorner()));
			player.sendMessage(CC.translate("&aYou have set the FFA Safezone."));
			try {
				configuration.save(cPractice.get().getMainConfig().getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.sendMessage(CC.translate("&aSaved."));
			return;
		}

		if (args.length < 2) {
			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.translate("&cPlease usage: /arena setspawn (arena) (a/b/red/blue)"));
			player.sendMessage(CC.translate("&cPlease usage: /arena setspawn (safezone)"));
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
			} else if (pos.equalsIgnoreCase("red")) {
				if (!(arena instanceof StandaloneArena)) {
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
			} else if (pos.equalsIgnoreCase("blue")) {
				if (!(arena instanceof StandaloneArena)) {
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
				standaloneArena.setSpawnBlue(new Cuboid(selection.getPoint1(), selection.getPoint2()));
			} else {
				player.sendMessage(CC.CHAT_BAR);
				player.sendMessage(CC.RED + "Invalid spawn point. Try \"a\" or \"b\".");
				player.sendMessage(CC.CHAT_BAR);
				return;
			}

			arena.save();

			player.sendMessage(CC.CHAT_BAR);
			player.sendMessage(CC.translate( "&cUpdated spawn point&f \"" + pos + "\" &cfor arena &f\"" + arena.getName() + "\""));
			player.sendMessage(CC.CHAT_BAR);
		} else {
			player.sendMessage(CC.RED + "An arena with that name already exists.");
		}
	}
}
