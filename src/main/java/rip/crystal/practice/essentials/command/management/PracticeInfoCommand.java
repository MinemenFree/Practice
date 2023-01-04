package rip.crystal.practice.essentials.command.management;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.clan.Clan;
import rip.crystal.practice.utilities.chat.CC;

public class PracticeInfoCommand extends BaseCommand {

    @Command(name = "practiceinfo", aliases = {"cpracticeinfo", "cpracinfo", "pracinfo", "admininfo"}, permission = "cpractice.info")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate("     &9" + cPractice.get().getName()));
        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate(" &7| &9Author&7: &f" + cPractice.get().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        player.sendMessage(CC.translate(" &7| &9Version&7: &f" + cPractice.get().getDescription().getVersion()));
        player.sendMessage(CC.translate(" &7| &9License&7: &f" + cPractice.get().getMainConfig().getString("LICENSE")));
        player.sendMessage(CC.translate(" &7| &9Rank System&7: &f" + cPractice.get().getRankManager().getRankSystem()));
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate(" &7| &9Spigot&7: &f" + cPractice.get().getServer().getName()));
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate(" &7| &9Arenas&7: &f" + Arena.getArenas().size()));
        player.sendMessage(CC.translate(" &7| &9Kits&7: &f" + Kit.getKits().size()));
        player.sendMessage(CC.translate(" &7| &9Ranked Kits&7: &f" + Kit.getKits().stream().filter(kit -> kit.getGameRules().isRanked()).count()));
        player.sendMessage(CC.translate(" &7| &9Clans&7: &f" + Clan.getClans().size()));
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.CHAT_BAR);
    }
}
