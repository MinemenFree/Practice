package rip.crystal.practice.visual.leaderboard.menu.button;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.visual.leaderboard.menu.StatisticsMenu;

import java.util.ArrayList;

public class PlayerStatsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ArrayList<String> lore = Lists.newArrayList();
        lore.add(CC.translate("&7&m-------------------"));
        lore.add(CC.translate("&fView your stats"));
        lore.add(CC.translate("&7&m-------------------"));
        return new ItemBuilder(Material.valueOf(String.valueOf(Material.LEASH))).lore(lore).name(CC.translate("&9&lPlayer Statistics")).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        new StatisticsMenu(player).openMenu(player);
    }
}

