package rip.crystal.practice.leaderboard.commands;

import rip.crystal.practice.Locale;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ResetEloCommand extends BaseCommand {

    @Command(name = "resetelo", permission = "hysteria.resetelo")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please usage: /resetelo (player)");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null || !target.hasPlayedBefore()) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        Profile profile = Profile.get(target.getUniqueId());
        profile.getKitData().forEach((kit, profileKitData) -> {
            profileKitData.setElo(1000);
        });
        player.sendMessage(CC.translate("&a" + target.getName() + " reset elo successfully"));
    }
}
