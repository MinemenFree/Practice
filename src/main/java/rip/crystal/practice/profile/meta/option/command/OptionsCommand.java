package rip.crystal.practice.profile.meta.option.command;

import rip.crystal.practice.profile.meta.option.menu.ProfileOptionsMenu;
import rip.crystal.api.command.BaseCommand;
import rip.crystal.api.command.Command;
import rip.crystal.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class OptionsCommand extends BaseCommand {

	@Command(name = "options", aliases = {"settings", "hsettings"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		new ProfileOptionsMenu().openMenu(player);
	}
}
