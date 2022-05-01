package rip.crystal.practice.player.clan.commands.subcommands;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.TaskUtil;

public class ClanLeaveCommand extends BaseCommand {

    @Command(name = "clan.leave")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        if (clan == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (player.getUniqueId().equals(clan.getLeader())) {
            new MessageFormat(Locale.CLAN_ERROR_OWNER_LEAVE
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        profile.setClan(null);
        clan.getMembers().remove(player.getUniqueId());
        new MessageFormat(Locale.CLAN_LEAVE_PLAYER
                .format(profile.getLocale()))
                .send(player);
        clan.getOnPlayers().forEach(other -> new MessageFormat(Locale.CLAN_LEAVE_BROADCAST.format(Profile.get(other.getUniqueId()).getLocale()))
                .add("{player_name}", player.getName())
                .send(other));

        TaskUtil.runAsync(profile::save);
    }
}