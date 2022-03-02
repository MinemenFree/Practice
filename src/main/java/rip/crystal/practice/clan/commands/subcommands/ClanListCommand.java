package rip.crystal.practice.clan.commands.subcommands;

import rip.crystal.practice.clan.Clan;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

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
