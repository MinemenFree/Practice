package rip.crystal.practice.essentials.chat.impl;

import org.bukkit.entity.Player;

public interface ChatFormat {

    String format(Player sender, Player receiver, String message);

}
