package rip.crystal.practice.player.clan.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.nametags.GxNameTag;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.StyleUtil;

public class ClanSetColorCommand extends BaseCommand {

    @Command(name = "clan.setcolor")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please insert color.");
            return;
        }

        String color = args[0];
        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        ChatColor chatColor = ChatColor.valueOf(color.toUpperCase());
        if (clan == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (!player.getUniqueId().equals(profile.getClan().getLeader())) {
            new MessageFormat(Locale.CLAN_ERROR_ONLY_OWNER
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (!chatColor.isColor()) {
            player.sendMessage(CC.translate("&cYou only can use a colors!"));
            return;
        }

        clan.setColor(chatColor);
        clan.save();
        clan.broadcast(Locale.CLAN_SET_COLOR_BROADCAST, new MessageFormat()
                .add("{new_color}", StyleUtil.colorName(ChatColor.valueOf(color.toUpperCase())))
                .add("{color}", color.toUpperCase()));
        TaskUtil.runAsync(() -> {
            GxNameTag.reloadOthersFor(player);
            GxNameTag.reloadPlayer(player);
        });
    }
}