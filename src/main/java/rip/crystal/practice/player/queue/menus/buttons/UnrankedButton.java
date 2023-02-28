package rip.crystal.practice.player.queue.menus.buttons;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
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

@AllArgsConstructor
public class UnrankedButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        BasicConfigurationFile config = cPractice.get().getMainConfig();
        ArrayList<String> lore = Lists.newArrayList();
        for (String text : config.getStringList("QUEUES.TYPES.UNRANKED.LORE")) {
            lore.add(text.replace("<kit>", config.getString("FFA.KIT")));
        }
        return new ItemBuilder(Material.valueOf(config.getString("QUEUES.TYPES.UNRANKED.ICON"))).lore(CC.translate(lore)).amount(1).name(CC.translate(config.getString("QUEUES.TYPES.UNRANKED.NAME"))).durability(config.getInteger("QUEUES.TYPES.UNRANKED.DATA")).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        Queue.getUnRankedMenu().openMenu(player);
    }
}

