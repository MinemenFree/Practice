package rip.crystal.practice.shop.command.staff.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.chat.ChatUtil;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.api.utilities.JavaUtil;
import rip.crystal.practice.player.profile.Profile;

public class CoinsGiveCommand extends BaseCommand {

    @Command(name = "coinsmanager.give", aliases = {"coinsm.give", "cmanager.give", "cm.give"}, permission = "cpractice.command.coinsmanager.give", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String label = command.getLabel().replace(".give", "");
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(ChatUtil.translate("&cUsage: /" + label + " give <player> <amount>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(ChatUtil.translate("&cPlayer " + args[0] + " not found!"));
            return;
        }

        Integer amount = JavaUtil.tryParseInt(args[1]);

        if (amount == null) {
            sender.sendMessage(ChatUtil.translate("&cAmount must be a number."));
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        profile.addCoins(amount);
        sender.sendMessage(ChatUtil.translate("&fYou gave &9" + amount + " &fcoins to &9" + player.getName() + "&f."));
    }
}
