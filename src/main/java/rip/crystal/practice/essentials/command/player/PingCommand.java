package rip.crystal.practice.essentials.command.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.BukkitReflection;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.StyleUtil;

public class PingCommand extends BaseCommand {

    @Command(name = "ping")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.YELLOW + "Your Ping: " + StyleUtil.colorPing(BukkitReflection.getPing(player)));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }
        player.sendMessage(CC.translate(Profile.get(target.getUniqueId()).getColor() + target.getName() + CC.YELLOW + "'s Ping: " +
                StyleUtil.colorPing(BukkitReflection.getPing(target))));
    }
}
