package rip.crystal.practice.game.kit.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.chat.CC;

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
		player.sendMessage(CC.translate("&9&lKits Status &7(" + (kit.isEnabled() ? "&a" : "&c") + kit.getName() + "&7)"));
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate(" &7▢ &9Ranked&f: " + (kit.getGameRules().isRanked() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Build&f: " + (kit.getGameRules().isBuild() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Spleef&f: " + (kit.getGameRules().isSpleef() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Sumo&f: " + (kit.getGameRules().isSumo() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Soup&f: " + (kit.getGameRules().isSoup() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Parkour&f: " + (kit.getGameRules().isParkour() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9HCF&f: " + (kit.getGameRules().isHcf() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9HCFTrap&f: " + (kit.getGameRules().isHcftrap() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Bridge&f: " + (kit.getGameRules().isBridge() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9BedFight&f: " + (kit.getGameRules().isBedFight() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Health Regeneration&f: " + (kit.getGameRules().isHealthRegeneration() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9NoFall Damage&f: " + (kit.getGameRules().isNofalldamage() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Anti Food&f: " + (kit.getGameRules().isAntiFood() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Show Health&f: " + (kit.getGameRules().isShowHealth() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate(" &7▢ &9Hit Delay&f: " + kit.getGameRules().getHitDelay()));
		player.sendMessage(CC.translate(" &7▢ &9KB Profile&f: " + kit.getGameRules().getKbProfile()));
		player.sendMessage(CC.CHAT_BAR);
	}
}
