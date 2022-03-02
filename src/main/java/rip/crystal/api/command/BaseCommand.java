package rip.crystal.api.command;

import rip.crystal.api.command.CommandArgs;
import rip.crystal.api.command.CommandManager;

public abstract class BaseCommand {

    public BaseCommand() {
        CommandManager.getInstance().registerCommands(this, null);
    }

    public abstract void onCommand(CommandArgs command);
}
