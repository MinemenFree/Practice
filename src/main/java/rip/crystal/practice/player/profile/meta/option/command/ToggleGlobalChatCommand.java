package rip.crystal.practice.player.profile.meta.option.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

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
