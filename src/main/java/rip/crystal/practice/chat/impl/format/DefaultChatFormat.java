package rip.crystal.practice.chat.impl.format;

import rip.crystal.practice.chat.impl.ChatFormat;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.entity.Player;

public class DefaultChatFormat implements ChatFormat {

    @Override
    public String format(Player sender, Player receiver, String message) {
        return CC.translate(sender.getDisplayName() + "&7:&f " +
            (sender.hasPermission("hysteria.chat.color") ? CC.translate(message) : CC.strip(message)));
    }

}
