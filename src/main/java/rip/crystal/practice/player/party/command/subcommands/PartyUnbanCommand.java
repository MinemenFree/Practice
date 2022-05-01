package rip.crystal.practice.player.party.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class PartyUnbanCommand extends BaseCommand {

    @Command(name = "party.unban", aliases = {"p.unban"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please usage: /party unban (leader)");
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

        Party party = profile.getParty();
        if(party.getBannedPlayers().remove(target.getUniqueId())){
            party.getListOfPlayers().forEach(other ->
                    other.sendMessage(ChatColor.RED + target.getName() + " has been unbanned from your party."));
        }else {
            player.sendMessage(ChatColor.RED + "This player is not banned from your party.");
        }
    }
}