package rip.crystal.practice.essentials.command.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.Practice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.hotbar.Hotbar;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.PlayerUtil;

public class ResetCommand extends BaseCommand {

    @Command(name = "reset", permission = "practice.command.reset")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            PlayerUtil.reset(player);
            player.teleport(Practice.get().getEssentials().getSpawn());
            Hotbar.giveHotbarItems(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        PlayerUtil.reset(target);
        target.teleport(Practice.get().getEssentials().getSpawn());
        Hotbar.giveHotbarItems(player);
    }
}
