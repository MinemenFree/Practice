package rip.crystal.practice.game.tournament.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.tournament.Tournament;
import rip.crystal.practice.game.tournament.impl.TournamentSolo;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.Clickable;

public class TournamentStartCommand extends BaseCommand {

    @Command(name = "tournament.start", permission = "tournament.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        Tournament<?> tournament = new TournamentSolo();

        if (args.length == 0) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.RED + "Please use /tournament start (kit)");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        Kit kit = Kit.getByName(args[0]);
        if (kit == null) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.RED + "This kit doesn't exist.");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        if (Tournament.getTournament() != null) {
            new MessageFormat(Locale.TOURNAMENT_ALREADY_CREATED.format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        tournament.setLimit(100);
        tournament.setKit(kit);
        Tournament.setTournament(tournament);
        Clickable clickable = new Clickable("&7(*) &cTournament has started, click here to join &7(*)", "Click to join", "/tournament join");

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(CC.CHAT_BAR));
        Bukkit.getOnlinePlayers().forEach(clickable::sendToPlayer);
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(CC.CHAT_BAR));
    }
}