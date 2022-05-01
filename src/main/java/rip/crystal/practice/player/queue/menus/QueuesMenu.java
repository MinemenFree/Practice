package rip.crystal.practice.player.queue.menus;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.queue.menus.buttons.FFAButton;
import rip.crystal.practice.player.queue.menus.buttons.RankedButton;
import rip.crystal.practice.player.queue.menus.buttons.UnrankedButton;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class QueuesMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate(cPractice.get().getMainConfig().getString("QUEUES.TITLE"));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(cPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(cPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();

        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
        buttons.put(cPractice.get().getMainConfig().getInteger("QUEUES.TYPES.UNRANKED.SLOT"), new UnrankedButton());
        buttons.put(cPractice.get().getMainConfig().getInteger("QUEUES.TYPES.RANKED.SLOT"), new RankedButton());
        buttons.put(cPractice.get().getMainConfig().getInteger("QUEUES.TYPES.FFA.SLOT"), new FFAButton());

        return buttons;
    }

    @Override
    public int getSize() {
        return cPractice.get().getMainConfig().getInteger("QUEUES.SIZE") * 9;
    }
}
