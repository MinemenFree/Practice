package rip.crystal.practice.profile.meta.option.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ToggleDuelRequestsCommand extends BaseCommand {

    @Command(name = "toggleduels", aliases = {"tgr", "tgd"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().receiveDuelRequests(!profile.getOptions().receiveDuelRequests());

        if (profile.getOptions().receiveDuelRequests()) {
            new MessageFormat(Locale.OPTIONS_RECEIVE_DUEL_REQUESTS_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_RECEIVE_DUEL_REQUESTS_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
