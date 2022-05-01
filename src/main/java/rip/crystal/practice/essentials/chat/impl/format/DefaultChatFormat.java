package rip.crystal.practice.essentials.chat.impl.format;

import org.bukkit.entity.Player;
import rip.crystal.practice.essentials.chat.impl.ChatFormat;
import rip.crystal.practice.utilities.chat.CC;

public class DefaultChatFormat implements ChatFormat {

    @Override
    public String format(Player sender, Player receiver, String message) {
        return CC.translate(sender.getDisplayName() + "&7:&f " +
            (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message)));
    }

}
