package rip.crystal.practice.essentials.command.staff;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class GameModeCommand extends BaseCommand {

	private static final ImmutableMap<String, GameMode> MAP = ImmutableMap.<String, GameMode>
					builder()
			.put("0", GameMode.SURVIVAL)
			.put("s", GameMode.SURVIVAL)
			.put("survival", GameMode.SURVIVAL)
			.put("1", GameMode.CREATIVE)
			.put("c", GameMode.CREATIVE)
			.put("creative", GameMode.CREATIVE)
			.build();

	@Command(name = "gamemode", aliases = {"gm"}, permission = "cpractice.command.gamemode")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.FFA) {
			player.sendMessage(CC.translate("&9You cannot execute this command while fighting."));
			return;
		}

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please insert GameMode.");
			return;
		}

		GameMode gameMode = MAP.get(args[0].toLowerCase());

		if (gameMode == null) {
			player.sendMessage(CC.RED + "Please insert a valid GameMode.");
			return;
		}

		if (args.length == 1) {
			player.setGameMode(gameMode);
			player.updateInventory();
			player.sendMessage(CC.GOLD + "You updated your game mode.");
			return;
		}

		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {
			new MessageFormat(Locale.PLAYER_NOT_FOUND
					.format(Profile.get(player.getUniqueId()).getLocale()))
					.send(player);
			return;
		}

		target.setGameMode(gameMode);
		target.updateInventory();
		target.sendMessage(CC.GOLD + "Your game mode has been updated by " + player.getName());
	}
}
