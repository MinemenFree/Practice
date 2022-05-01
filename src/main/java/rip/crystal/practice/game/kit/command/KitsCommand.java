package rip.crystal.practice.game.kit.command;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.chat.ChatHelper;

public class KitsCommand extends BaseCommand {

	@Command(name = "kits", permission = "cpractice.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(ChatColor.RED + "Kits:");

		for (Kit kit : Kit.getKits()) {
			ChatComponentBuilder builder = new ChatComponentBuilder("")
					.parse("&7- " + (kit.isEnabled() ? "&a" : "&c") + kit.getName() +
							" &7(" + (kit.getGameRules().isRanked() ? "Ranked" : "Un-Ranked") + ")");

			ChatComponentBuilder status = new ChatComponentBuilder("").parse("&7[&cSTATUS&7]");
			status.attachToEachPart(ChatHelper.hover("&cClick to view this kit's status."));
			status.attachToEachPart(ChatHelper.click("/kit status " + kit.getName()));

			builder.append(" ");

			for (BaseComponent component : status.create()) {
				builder.append((TextComponent) component);
			}

			player.spigot().sendMessage(builder.create());
		}
	}
}
