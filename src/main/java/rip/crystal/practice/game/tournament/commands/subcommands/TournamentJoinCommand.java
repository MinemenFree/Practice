package rip.crystal.practice.game.tournament.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.tournament.impl.TournamentSolo;
import rip.crystal.practice.player.profile.Profile;

public class TournamentJoinCommand extends BaseCommand {

    @Command(name = "tournament.join")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        TournamentSolo tournament = (TournamentSolo) TournamentSolo.getTournament();

        if (tournament == null) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if(profile.isBusy()) {
            player.sendMessage(ChatColor.RED + "You may not join the tournament in your current state.");
            return;
        }
        if (profile.isInTournament()) {
            player.sendMessage(ChatColor.RED + "You are already in the tournament.");
            return;
        }

        profile.setInTournament(true);
        tournament.join(player);
        /*if (tournament.getPlayers().size() == tournament.getLimit()) {
            player.sendMessage(ChatColor.RED + "Tournament is full.");
            return;
        }*/
        /*if (tournament.isClans()) {
            Clan clan = profile.getClan();
            if (clan == null) {
                player.sendMessage(CC.translate("&cYou can only enter this tournament with clan."));
                return;
            }

            if (!clan.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(CC.translate("&cOnly clan owner can enter."));
                return;
            }

            if (clan.getOnPlayers().size() != tournament.getSize()) {
                player.sendMessage(CC.translate("&cYou need a minimum of " + tournament.getSize() + " people to enter the tournament."));
                return;
            }

            tournament.join(clan);
            return;
        }

        if (tournament.getSize() > 1) {
            Party party = profile.getParty();
            if (party == null) {
                player.sendMessage(CC.translate("&cYou can only enter this tournament with party."));
                return;
            }

            if (party.getLeader() != player) {
                player.sendMessage(CC.translate("&cOnly the party owner can enter."));
                return;
            }

            if (party.getPlayers().size() != tournament.getSize()) {
                player.sendMessage(CC.translate("&cYou need a minimum of &f" + tournament.getSize() + " &cpeople to enter the tournament."));
                return;
            }
            tournament.join(party);
            return;
        }*/
    }
}