package rip.crystal.practice.game.kit.command;

import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitCommand extends BaseCommand {

    public KitCommand() {
        super();
        new KitCreateCommand();
        new KitGameRuleCommand();
        new KitGetLoadoutCommand();
        new KitSetLoadoutCommand();
        new KitSetIconCommand();
        new KitToggleComand();
        new KitDeleteCommand();
        new KitStatusCommand();
        new KitRulesCommand();
        new KitSetSlotCommand();
    }

    @Command(name = "kit", permission = "cpractice.kit.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&c&lKits Help"));
        player.sendMessage(CC.translate("&c/kit setrule &f(kit_name) (rule) (value)"));
        player.sendMessage(CC.translate("&c/kit setslot &f(kit_name) (slot)"));
        player.sendMessage(CC.translate("&c/kit setloadout &f(kit_name)"));
        player.sendMessage(CC.translate("&c/kit getloadout &f(kit_name)"));
        player.sendMessage(CC.translate("&c/kit seticon &f(kit_name)"));
        player.sendMessage(CC.translate("&c/kit toggle &f(kit_name)"));
        player.sendMessage(CC.translate("&c/kit create &f(kit_name)"));
        player.sendMessage(CC.translate("&c/kit delete &f(kit_name)"));
        player.sendMessage(CC.translate("&c/kit rules"));
        player.sendMessage(CC.translate("&c/kits"));
        player.sendMessage(CC.CHAT_BAR);
    }
}
