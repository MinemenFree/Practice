package rip.crystal.practice.party.command.subcommands;

import rip.crystal.practice.Locale;
import rip.crystal.practice.party.Party;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.ProfileState;
import rip.crystal.practice.profile.hotbar.Hotbar;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class PartyCreateCommand extends BaseCommand {

	@Command(name = "party.create", aliases = {"p.create"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getParty() != null) {
			player.sendMessage(CC.RED + "You already have a party.");
			return;
		}

		if (profile.getState() != ProfileState.LOBBY) {
			player.sendMessage(CC.RED + "You must be in the lobby to create a party.");
			return;
		}

		profile.setParty(new Party(player));

		Hotbar.giveHotbarItems(player);

		new MessageFormat(Locale.PARTY_CREATE
				.format(profile.getLocale()))
				.send(player);
	}
}
