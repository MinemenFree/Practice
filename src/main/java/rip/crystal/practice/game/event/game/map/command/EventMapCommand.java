package rip.crystal.practice.game.event.game.map.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

public class EventMapCommand extends BaseCommand {

    private final String[][] HELP = new String[][] {
            new String[]{ "/event map create", "Create a event map" },
            new String[]{ "/event map delete", "Delete a event map" },
            new String[]{ "/event map addspawn", "Add spawn to event map" },
            new String[]{ "/event map status", "Get status of event map" }
    };

    public EventMapCommand() {
        super();
        new EventMapCreateCommand();
        new EventMapDeleteCommand();
        new EventMapSetSpawnCommand();
        new EventMapStatusCommand();
    }

    @Command(name = "event.map", permission = "cpractice.event.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.GOLD + "Event Map Help");

        for (String[] command : HELP) {
            player.sendMessage(CC.BLUE + command[0] + CC.GRAY + " - " + CC.WHITE + command[1]);
        }

        player.sendMessage(CC.CHAT_BAR);
    }
}
