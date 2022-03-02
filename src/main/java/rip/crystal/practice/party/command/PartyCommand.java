package rip.crystal.practice.party.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.party.command.subcommands.*;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class PartyCommand extends BaseCommand {

    public PartyCommand() {
        super();
        new PartyChatCommand();
        new PartyCloseCommand();
        new PartyCreateCommand();
        new PartyDisbandCommand();
        new PartyInfoCommand();
        new PartyInviteCommand();
        new PartyJoinCommand();
        new PartyKickCommand();
        new PartyLeaveCommand();
        new PartyOpenCommand();
        new PartyInviteClanCommand();
        new PartyBanCommand();
        new PartyUnbanCommand();
        new PartyAnnounceCommand();
    }

    @Command(name = "party", aliases = {"p"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new MessageFormat(Locale.PARTY_HELP
                .format(Profile.get(player.getUniqueId()).getLocale()))
                .send(player);
    }
}
