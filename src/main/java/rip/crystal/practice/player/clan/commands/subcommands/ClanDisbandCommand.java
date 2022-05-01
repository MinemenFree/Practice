package rip.crystal.practice.player.clan.commands.subcommands;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;

public class ClanDisbandCommand extends BaseCommand {

    @Command(name = "clan.disband", aliases = {"clan.remove", "clan.delete"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length != 0) {
            if (player.hasPermission("cpractice.clan.disband")) {
                Clan clan = Clan.getByName(args[0]);
                if (clan == null) {
                    player.sendMessage(CC.RED + "Please insert a valid Clan.");
                    return;
                }

                clan.disband(player);
            } else {
                player.sendMessage(CC.RED + "You do not have permissions to disband other clans.");
            }
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        if (profile.getClan() == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }
        else if (!profile.getClan().getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.RED + "You do not leader to disband this clan");
            return;
        }
        profile.getClan().disband(player);
        if(clan.getMembers().isEmpty()) {
            return;
        }
        clan.getMembers().remove(clan.getMembers());
    }
}
