package rip.crystal.practice.pingfactor;

import rip.crystal.practice.pingfactor.subcommands.PingFactorSetCommand;
import rip.crystal.practice.pingfactor.subcommands.PingFactorStatusCommand;
import rip.crystal.practice.pingfactor.subcommands.PingFactorToggleCommand;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class PingFactorCommand extends BaseCommand {

    public PingFactorCommand() {
        new PingFactorStatusCommand();
        new PingFactorSetCommand();
        new PingFactorToggleCommand();
    }

    @Command(name = "pingfactor", aliases = {"pf"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        Profile profile = Profile.get(player.getUniqueId());

        if(args.length == 0) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&cUsage: /pingfactor set (ping)"));
            player.sendMessage(CC.translate("&cUsage: /pingfactor toggle"));
            player.sendMessage(CC.translate("&cUsage: /pingfactor status"));
            player.sendMessage(CC.CHAT_BAR);
        }
    }
}
