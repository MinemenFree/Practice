package rip.crystal.practice.profile.meta.option.button;
/* 
   Made by Hysteria Development Team
   Created on 05.11.2021
*/

import rip.crystal.practice.cosmetics.impl.killeffects.menu.KillEffectsMenu;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.utilities.menu.Button;

import java.util.ArrayList;
import java.util.List;

public class DeathAnimationsOptionsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&eClick to open Kill Effects Menu!");

        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .name("&9&lKill Effects")
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.get(player.getUniqueId());
        new KillEffectsMenu().openMenu(player);
    }
}
