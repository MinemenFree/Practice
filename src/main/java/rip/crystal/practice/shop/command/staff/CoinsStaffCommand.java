package rip.crystal.practice.shop.command.staff;

import org.bukkit.command.CommandSender;
import rip.crystal.practice.api.chat.ChatUtil;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.shop.command.staff.impl.CoinsGiveCommand;
import rip.crystal.practice.shop.command.staff.impl.CoinsSetCommand;
import rip.crystal.practice.shop.command.staff.impl.CoinsTakeCommand;
import rip.crystal.practice.utilities.chat.CC;

public class CoinsStaffCommand extends BaseCommand {

    public CoinsStaffCommand() {
        new CoinsGiveCommand();
        new CoinsSetCommand();
        new CoinsTakeCommand();
    }

    @Command(name = "coinsmanager", aliases = {"coinsm", "cmanager", "cm"}, permission = "cpractice.command.coinsmanager", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String label = command.getLabel();

        CC.sender(sender, ChatUtil.NORMAL_LINE);
        CC.sender(sender, " &9&lCoins Manager");
        CC.sender(sender, "");
        CC.sender(sender, "&9/" + label + " give <player> <amount> &7- &fGive coins to a player");
        CC.sender(sender, "&9/" + label + " set <player> <amount> &7- &fSet coins for a player");
        CC.sender(sender, "&9/" + label + " take <player> <amount> &7- &fTake coins from a player");
        CC.sender(sender, ChatUtil.NORMAL_LINE);
    }

}
