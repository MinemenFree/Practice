package rip.crystal.practice.player.clan.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;

public class ClanKickCommand extends BaseCommand {

    @Command(name = "clan.kick")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please insert a Target.");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        if(clan == null){
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        Profile profileTarget = Profile.get(target.getUniqueId());

        if(!clan.getLeader().equals(player.getUniqueId())){
            new MessageFormat(Locale.CLAN_ERROR_ONLY_OWNER
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if(!profile.getClan().getMembers().contains(target.getUniqueId())){
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_IN_YOUR_CLAN.format(profile.getLocale()))
                    .add("{target_name}", target.getName())
                    .send(player);
            return;
        }

        profileTarget.setClan(null);

        clan.getMembers().remove(target.getUniqueId());

        Player onTarget = Bukkit.getPlayer(target.getUniqueId());

        if(onTarget != null){
            new MessageFormat(Locale.CLAN_KICKED_PLAYER
                    .format(Profile.get(onTarget.getUniqueId()).getLocale()))
                    .send(onTarget);
        }

        clan.getOnPlayers().forEach(other -> new MessageFormat(Locale.CLAN_KICKED_BROADCAST.format(Profile.get(other.getUniqueId()).getLocale()))
                .add("{target_name}", target.getName())
                .send(other));

        TaskUtil.runAsync(profileTarget::save);
    }
}