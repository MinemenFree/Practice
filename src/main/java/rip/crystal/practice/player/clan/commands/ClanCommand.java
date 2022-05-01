package rip.crystal.practice.player.clan.commands;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.clan.commands.subcommands.*;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

public class ClanCommand extends BaseCommand {

    public ClanCommand() {
        new ClanCreateCommand();
        new ClanDisbandCommand();
        new ClanInfoCommand();
        new ClanListCommand();
        new ClanJoinCommand();
        new ClanInviteCommand();
        new ClanRenameCommand();
        new ClanKickCommand();
        new ClanSetColorCommand();
        new ClanLeaveCommand();
        new ClanSetPointsCommand();
        new ClanChatCommand();
    }

    @Command(name = "clan")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new MessageFormat(Locale.CLAN_HELP
                .format(Profile.get(player.getUniqueId()).getLocale()))
                .send(player);
    }
}
