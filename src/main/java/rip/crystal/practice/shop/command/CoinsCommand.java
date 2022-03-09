package rip.crystal.practice.shop.command;

import org.bukkit.entity.Player;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.shop.menu.ShopMenu;
import rip.crystal.practice.utilities.chat.CC;

public class CoinsCommand extends BaseCommand {

    @Command(name="coins", aliases = {"coin"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        CC.sender(player, " &9Coins: &f" + profile.getCoins());
    }
}
