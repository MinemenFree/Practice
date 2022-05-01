package rip.crystal.practice.player.profile.meta.option.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

public class TogglePrivateMessagesCommand extends BaseCommand {

    @Command(name = "togglepm", aliases = {"togglepms", "tpm", "tpms"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().receivingNewConversations(!profile.getOptions().receivingNewConversations());
        profile.getConversations().expireAllConversations();

        if (profile.getOptions().receivingNewConversations()) {
            new MessageFormat(Locale.OPTIONS_PRIVATE_MESSAGES_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_PRIVATE_MESSAGES_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
