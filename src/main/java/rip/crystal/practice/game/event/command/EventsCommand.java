package rip.crystal.practice.game.event.command;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.game.event.Event;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.chat.ChatHelper;

public class EventsCommand extends BaseCommand {

	@Command(name = "events", permission = "cpractice.event.host")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(CC.RED + "Events:");
		for (Event events : Event.events) {
			ChatComponentBuilder builder = new ChatComponentBuilder("")
					.parse("&7- " + "&a" + events.getName());

			ChatComponentBuilder status = new ChatComponentBuilder("").parse("&7[&cSTATUS&7]");
			status.attachToEachPart(ChatHelper.hover("&cClick to view this event's status."));
			status.attachToEachPart(ChatHelper.click("/event info"));

			builder.append(" ");

			for (BaseComponent component : status.create()) {
				builder.append((TextComponent) component);
			}

			player.spigot().sendMessage(builder.create());
		}
	}
}
