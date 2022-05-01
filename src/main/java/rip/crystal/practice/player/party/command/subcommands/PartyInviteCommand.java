package rip.crystal.practice.player.party.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.party.enums.PartyPrivacy;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class PartyInviteCommand extends BaseCommand {

	@Command(name = "party.invite", aliases = {"p.invite"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /party invite (player)");
			return;
		}

		Profile profile = Profile.get(player.getUniqueId());
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			new MessageFormat(Locale.PLAYER_NOT_FOUND
					.format(profile.getLocale()))
					.send(player);
			return;
		}

		if (profile.getParty() == null) {
			player.sendMessage(CC.RED + "You do not have a party.");
			return;
		}

		if (!profile.getParty().getLeader().equals(player)) {
			player.sendMessage(CC.RED + "You are not the leader of your party.");
			return;
		}

		if (profile.getParty().getInvite(target.getUniqueId()) != null) {
			player.sendMessage(CC.RED + "That player has already been invited to your party.");
			return;
		}

		if (profile.getParty().containsPlayer(target.getUniqueId())) {
			player.sendMessage(CC.RED + "That player is already in your party.");
			return;
		}

		if (profile.getParty().getPrivacy() == PartyPrivacy.OPEN) {
			player.sendMessage(CC.RED + "The party state is Open. You do not need to invite players.");
			return;
		}

		if(profile.getParty().getBannedPlayers().contains(player.getUniqueId())){
			player.sendMessage(ChatColor.RED + "You can't invite banned players from your party.");
			return;
		}

		Profile targetData = Profile.get(target.getUniqueId());

		if (targetData.isBusy()) {
			player.sendMessage(target.getDisplayName() + CC.RED + " is currently busy.");
			return;
		}

		profile.getParty().invite(target);
	}
}
