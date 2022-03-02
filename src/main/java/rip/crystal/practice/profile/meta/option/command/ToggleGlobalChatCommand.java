package rip.crystal.practice.profile.meta.option.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ToggleGlobalChatCommand extends BaseCommand {

    @Command(name = "toggleglobalchat", aliases = {"tgc", "togglepublicchat", "tpc"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());

        if (profile.getOptions().publicChatEnabled()) {
            new MessageFormat(Locale.OPTIONS_GLOBAL_CHAT_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_GLOBAL_CHAT_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
