package rip.crystal.practice.essentials.command.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.menu.match.ViewMatchMenu;
import rip.crystal.practice.utilities.MessageFormat;

public class ViewMatchCommand extends BaseCommand {

    @Command(name = "viewmatch", aliases = {"matches"}, permission = "cpractice.viewmatch")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            Profile profile = Profile.get(player.getUniqueId());
            new ViewMatchMenu(profile).openMenu(player);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        Profile profile = Profile.get(target.getUniqueId());
        new ViewMatchMenu(profile).openMenu(player);
    }
}