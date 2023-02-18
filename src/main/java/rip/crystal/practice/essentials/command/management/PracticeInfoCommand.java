package rip.crystal.practice.essentials.command.management;

import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.clan.Clan;
//import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.MessageFormat;

public class PracticeInfoCommand extends BaseCommand {

    @Command(name = "practiceinfo", aliases = {"cpracticeinfo", "cpracinfo", "pracinfo", "admininfo"}, permission = "cpractice.info")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
	Profile profile = Profile.get(player.getUniqueId());
	    
        new MessageFormat(Locale.MISC_PRACTICE_INFO.format(profile.getLocale()))
                .add("<practice_name>", cPractice.get().getName())
                .add("<practice_version>", cPractice.get().getDescription().getVersion())
                .add("<practice_license>", cPractice.get().getMainConfig().getString("LICENSE"))
                .add("<rank_core>", cPractice.get().getRankManager().getRankSystem())
                .add("<spigot>", cPractice.get().getServer().getName())
                .add("<arenas>", Integer.toString(Arena.getArenas().size()))
                .add("<kits>", Integer.toString(Kit.getKits().size()))
                .add("<ranked_kits>", Long.toString(Kit.getKits().stream().filter(kit -> kit.getGameRules().isRanked()).count()))
                .add("<clans>", Integer.toString(Clan.getClans().size()))
		.send(player);
    }
}
