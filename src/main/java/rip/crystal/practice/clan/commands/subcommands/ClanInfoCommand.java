package rip.crystal.practice.clan.commands.subcommands;

import rip.crystal.practice.clan.Clan;
import rip.crystal.practice.profile.Profile;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanInfoCommand extends BaseCommand {

    @Command(name = "clan.info", aliases = {"clan.show", "clan.i"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            Profile profile = Profile.get(player.getUniqueId());

            if (profile.getClan() != null) profile.getClan().show(player);
            else player.sendMessage(ChatColor.GRAY + "/clan info (name)");
            return;
        }

        Clan clan = Clan.getByName(args[0]);
        if(clan == null) {
            return;
        }
        clan.show(player);
    }
}
