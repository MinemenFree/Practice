package rip.crystal.practice.player.profile.meta.option.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

public class ToggleScoreboardCommand extends BaseCommand {

    @Command(name = "togglescoreboard", aliases = {"tsb"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().showScoreboard(!profile.getOptions().showScoreboard());

        if (profile.getOptions().showScoreboard()) {
            new MessageFormat(Locale.OPTIONS_SCOREBOARD_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_SCOREBOARD_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
