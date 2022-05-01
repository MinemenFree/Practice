package rip.crystal.practice.player.queue.menus.buttons;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import rip.crystal.practice.utilities.menu.Button;

import java.util.ArrayList;

public class RankedButton extends Button {

    BasicConfigurationFile config = cPractice.get().getMainConfig();
    //private Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        ArrayList<String> lore = Lists.newArrayList();
        for (String text : this.config.getStringList("QUEUES.TYPES.RANKED.LORE")) {
            lore.add(text.replace("<kit>", cPractice.get().getMainConfig().getString("FFA.KIT")));
        }
        return new ItemBuilder(Material.valueOf(cPractice.get().getMainConfig().getString("QUEUES.TYPES.RANKED.ICON"))).lore(CC.translate(lore)).amount(1).name(CC.translate(cPractice.get().getMainConfig().getString("QUEUES.TYPES.RANKED.NAME"))).durability(cPractice.get().getMainConfig().getInteger("QUEUES.TYPES.RANKED.DATA")).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        Queue.getRankedMenu().openMenu(player);
    }
}

