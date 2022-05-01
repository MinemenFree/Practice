package rip.crystal.practice.game.arena.command;

import org.bukkit.command.CommandSender;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;


public class ArenaCommand extends BaseCommand {

    public ArenaCommand() {
        super();
        new ArenaAddKitCommand();
        new ArenaCreateCommand();
        new ArenaDeleteCommand();
        new ArenaGenerateCommand();
        new ArenaGenHelperCommand();
        new ArenaRemoveKitCommand();
        new ArenaSaveCommand();
        new ArenaManageCommand();
        new ArenaSelectionCommand();
        new ArenaSetSpawnCommand();
        new ArenaStatusCommand();
        new ArenaSetAuthorCommand();
        new ArenaTeleportCommand();
        new ArenaSetIconCommand();
    }

    @Command(name = "arena", permission = "cpractice.arena.admin", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();

        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&c&lArena Help"));
        sender.sendMessage(CC.translate("&c/arena create &f(arena_name) (SHARED/STANDALONE)"));
        sender.sendMessage(CC.translate("&c/arena setspawn &f(arena_name) (a/b/[red/blue])"));
        sender.sendMessage(CC.translate("&c/arena setauthor &f(arena_name) (author_name)"));
        sender.sendMessage(CC.translate("&c/arena removekit &f(arena_name) (kit_name)"));
        sender.sendMessage(CC.translate("&c/arena addkit &f(arena_name) (kit_name)"));
        sender.sendMessage(CC.translate("&c/arena teleport &f(arena_name)"));
        sender.sendMessage(CC.translate("&c/arena generate &f(arena_name)"));
        sender.sendMessage(CC.translate("&c/arena seticon &f(arena_name)"));
        sender.sendMessage(CC.translate("&c/arena status &f(arena_name)"));
        sender.sendMessage(CC.translate("&c/arena delete &f(arena_name)"));
        sender.sendMessage(CC.translate("&c/arena genhelper"));
        sender.sendMessage(CC.translate("&c/arena manage"));
        sender.sendMessage(CC.translate("&c/arena save"));
        sender.sendMessage(CC.translate("&c/arena wand"));
        sender.sendMessage(CC.translate("&c/arenas"));
        sender.sendMessage(CC.CHAT_BAR);
    }
}
