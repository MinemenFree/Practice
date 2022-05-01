package rip.crystal.practice.game.kit.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

public class KitRulesCommand extends BaseCommand {

	@Command(name = "kit.rules", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&4&lKit Rules"));
		player.sendMessage(CC.translate(""));
		player.sendMessage(CC.translate("&4Settings:"));
		player.sendMessage(CC.translate(" &7- &cHealthregeneration &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cNofalldamage &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cEffects &f(add/remove/list)"));
		player.sendMessage(CC.translate(" &7- &cShowhealth &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cAntiFood &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cEditoritems &f(blank)"));
		player.sendMessage(CC.translate(" &7- &cKBProfile &f(profile)"));
		player.sendMessage(CC.translate(" &7- &cRanked &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cHitdelay &f(int)"));
		player.sendMessage(CC.translate(" &7- &cAllowPotionFill &f(int)"));
		player.sendMessage(CC.translate(""));
		player.sendMessage(CC.translate("&4Gamemodes:"));
		player.sendMessage(CC.translate(" &7- &cParkour &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cBridge &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cBedFight &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cSpleef &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cSoup &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cBuild &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cSumo &f(true/false)"));
		player.sendMessage(CC.translate(" &7- &cHCF &f(true/false)"));
		player.sendMessage(CC.CHAT_BAR);
	}
}
