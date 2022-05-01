package rip.crystal.practice.player.party.command.subcommands;
/* 
   Made by cpractice Development Team
   Created on 07.11.2021
*/

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.party.enums.PartyPrivacy;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.TimeUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.Clickable;

public class PartyAnnounceCommand extends BaseCommand {

    private boolean sendMessage;

    @Command(name = "party.announce", aliases = {"p.announce"}, permission = "cpractice.party.announce")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getParty() == null) { // If player isn't in party, return.
            player.sendMessage(ChatColor.RED + "You are not in a party.");
            return;
        }

        if (!profile.getParty().getLeader().equals(player)) { // If the executor isn't the party leader, return.
            player.sendMessage(ChatColor.RED + "You are not the leader of this party.");
            return;
        }

        if(profile.getParty().getPrivacy() != PartyPrivacy.OPEN) { // If party isn't public while command is executed, make party public.
            profile.getParty().setPrivacy(PartyPrivacy.OPEN);
        }

        if(!profile.getPartyAnnounceCooldown().hasExpired()) { // If the cooldown hasn't expired, send message then return.
            String time = TimeUtil.millisToSeconds(profile.getPartyAnnounceCooldown().getRemaining()); // Get the remaining time of cooldown and init to String.
            // Message to broadcast
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&4&lPARTY ANNOUNCEMENT"));
            player.sendMessage(CC.translate("  &7* &cYou are on cooldown."));
            player.sendMessage(CC.translate("  &7* &cRemaining time: &f" + time));
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        profile.setPartyAnnounceCooldown(new Cooldown(60_000)); // Set the party announce cooldown to 60 seconds (1 minute)
        // Message to broadcast
        Bukkit.broadcastMessage(CC.CHAT_BAR);
        Bukkit.broadcastMessage(CC.translate("&4&lPARTY ANNOUNCEMENT"));
        Bukkit.broadcastMessage(CC.translate("  &7* &4&l" + player.getName() + " &cis hosting a public party!"));
        Clickable clickHereToJoin = new Clickable(CC.translate("  &7* &cClick &c&lhere &cto join"), CC.translate("&cClick here to join"), "/p join " + player.getName());
        Bukkit.getOnlinePlayers().forEach(clickHereToJoin::sendToPlayer);
        Bukkit.broadcastMessage(CC.translate("  &7* &cOr use /party join " + player.getName()));
        Bukkit.broadcastMessage(CC.CHAT_BAR);
    }
}
