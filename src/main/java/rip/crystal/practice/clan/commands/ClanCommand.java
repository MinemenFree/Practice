package rip.crystal.practice.clan.commands;

import rip.crystal.practice.Locale;
import rip.crystal.practice.category.commands.subcommands.RemoveCommand;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;
import rip.crystal.practice.clan.commands.subcommands.*;

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
        new RemoveCommand();
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
