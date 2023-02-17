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
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.util.HashMap;
import java.util.Map;

public class QueuesMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        BasicConfigurationFile config = cPractice.get().getMainConfig();
       
        return config.getString("QUEUES.TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        BasicConfigurationFile config = cPractice.get().getMainConfig();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(config.getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(config.getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();

        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
        buttons.put(config.getInteger("QUEUES.TYPES.UNRANKED.SLOT"), new UnrankedButton());
        buttons.put(config.getInteger("QUEUES.TYPES.RANKED.SLOT"), new RankedButton());
        buttons.put(config.getInteger("QUEUES.TYPES.FFA.SLOT"), new FFAButton());

        return buttons;
    }

    @Override
    public int getSize() {
        BasicConfigurationFile config = cPractice.get().getMainConfig();
        
        return config.getInteger("QUEUES.SIZE") * 9;
    }
}
