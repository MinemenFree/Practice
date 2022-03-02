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
        //profile.getOptions().isUsingPingFactor(!profile.getOptions().isUsingPingFactor());

        if(args.length == 0) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&cUsage: /pingfactor set (ping)"));
            player.sendMessage(CC.translate("&cUsage: /pingfactor toggle"));
            player.sendMessage(CC.translate("&cUsage: /pingfactor status"));
            player.sendMessage(CC.CHAT_BAR);
        }

        /*if(args[0].equalsIgnoreCase("toggle")) {
            if (profile.getOptions().isUsingPingFactor()) {
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.translate("&aYou have enabled ping factor."));
                player.sendMessage(CC.CHAT_BAR);
            } else {
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.translate("&cYou have disabled ping factor."));
                player.sendMessage(CC.CHAT_BAR);
            }
        }

        if(args[0].equalsIgnoreCase("set")) {
            if (!StringUtils.isNumeric(args[1])) {
                player.sendMessage(CC.translate("&cEnter a valid number." + args[1]));
            }

            if(args.length == 1) {
                player.sendMessage(CC.CHAT_BAR);
                profile.setPingRange(Integer.getInteger(args[1]));
                player.sendMessage(CC.translate("&aPing Range has been set to " + args[1]));
                player.sendMessage(CC.CHAT_BAR);
            }
        }*/
    }
}
