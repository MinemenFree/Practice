package rip.crystal.practice.player.clan.commands.subcommands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.utilities.chat.CC;

public class ClanListCommand extends BaseCommand {

    @Command(name = "clan.list")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.translate("&c&lList of all Clans"));
        Clan.getClans().values().forEach(clan ->
                player.sendMessage(CC.translate("&7- &e" + StringUtils.capitalize(clan.getName().toLowerCase()))));
    }
}
