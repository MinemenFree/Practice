package rip.crystal.practice.game.kit.command;

import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitStatusCommand extends BaseCommand {

	@Command(name = "kit.status", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit status (kit)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&c&lKits Status &f(" + (kit.isEnabled() ? "&a" : "&c") + kit.getName() + "&f)"));
		player.sendMessage("");
		player.sendMessage(CC.translate("&cRanked&f: " + (kit.getGameRules().isRanked() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cBuild&f: " + (kit.getGameRules().isBuild() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cSpleef&f: " + (kit.getGameRules().isSpleef() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cSumo&f: " + (kit.getGameRules().isSumo() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cSoup&f: " + (kit.getGameRules().isSoup() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cParkour&f: " + (kit.getGameRules().isParkour() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cHCF&f: " + (kit.getGameRules().isHcf() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cHCFTrap&f: " + (kit.getGameRules().isHcftrap() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cBridge&f: " + (kit.getGameRules().isBridge() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cHealth Regeneration&f: " + (kit.getGameRules().isHealthRegeneration() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cNoFall Damage&f: " + (kit.getGameRules().isNofalldamage() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cAnti Food&f: " + (kit.getGameRules().isAntiFood() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cShow Health&f: " + (kit.getGameRules().isShowHealth() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&cHit Delay&f: " + kit.getGameRules().getHitDelay()));
		player.sendMessage(CC.translate("&cKb Profile&f: " + kit.getGameRules().getKbProfile()));
		player.sendMessage(CC.CHAT_BAR);
	}
}
