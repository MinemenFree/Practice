package rip.crystal.practice.player.party.command.subcommands;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;

public class PartyChatCommand extends BaseCommand {

	@Command(name = "party.chat", aliases = {"p.chat"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please insert a valid message.");
			return;
		}

		Profile profile = Profile.get(player.getUniqueId());
		StringBuilder builder = new StringBuilder();

		for (String arg : args) {
			builder.append(arg).append(" ");
		}

		if (profile.getParty() != null) {
			profile.getParty().sendChat(player, builder.toString());
		}
	}
}
