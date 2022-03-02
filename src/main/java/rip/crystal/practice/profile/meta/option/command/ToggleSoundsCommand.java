package rip.crystal.practice.profile.meta.option.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ToggleSoundsCommand extends BaseCommand {

    @Command(name = "togglesounds", aliases = {"sounds"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().playingMessageSounds(!profile.getOptions().playingMessageSounds());

        if (profile.getOptions().playingMessageSounds()) {
            new MessageFormat(Locale.OPTIONS_PRIVATE_MESSAGE_SOUND_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_PRIVATE_MESSAGE_SOUND_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
