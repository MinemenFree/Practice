package rip.crystal.practice.player.cosmetics.menu;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.meta.option.button.DeathAnimationsOptionsButton;
import rip.crystal.practice.player.profile.meta.option.button.TrailsOptionsButton;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class CosmeticsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_GRAY + "Cosmetics Menu";
    }

    {
        setPlaceholder(true);
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(cPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(cPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        buttons.put(11, new TrailsOptionsButton());
        buttons.put(13, new DeathAnimationsOptionsButton());

        return buttons;
    }
}
