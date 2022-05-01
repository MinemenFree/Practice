package rip.crystal.practice.player.clan.commands.subcommands;

import com.mongodb.client.model.Filters;
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

public class ClanRenameCommand extends BaseCommand {

    @Command(name = "clan.rename")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please insert new name.");
            return;
        }

        String new_name = args[0];
        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
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

        if (new_name.contains("&")) {
            player.sendMessage(CC.translate("&cPlease insert a valid Clan name."));
            return;
        }

        if (new_name.length() > 4) {
            new MessageFormat(Locale.CLAN_ERROR_MAX_LENGTH_NAME
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }
        String old_name = clan.getName();
        Clan.getClans().remove(clan.getName());
        Clan.getCollection().deleteOne(Filters.eq("name", clan.getName()));
        clan.setName(new_name);

        clan.broadcast(Locale.CLAN_RENAME_BROADCAST, new MessageFormat()
                .add("{old_name}", old_name)
                .add("{new_name}", new_name));

        Clan.getClans().put(new_name, clan);
        clan.save();
        TaskUtil.runAsync(() -> {
            GxNameTag.reloadOthersFor(player);
            GxNameTag.reloadPlayer(player);
        });
    }
}