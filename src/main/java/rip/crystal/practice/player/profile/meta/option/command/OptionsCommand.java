package rip.crystal.practice.player.profile.meta.option.command;

import rip.crystal.practice.player.profile.menu.settings.SettingsMenu;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class OptionsCommand extends BaseCommand {

	@Command(name = "options", aliases = {"settings", "hsettings"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		new SettingsMenu().openMenu(player);
	}
}
