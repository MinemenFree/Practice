package rip.crystal.practice.profile.meta.option.button;
/* 
   Made by Hysteria Development Team
   Created on 05.11.2021
*/

import rip.crystal.practice.cosmetics.impl.killeffects.menu.KillEffectsMenu;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.meta.option.menu.ProfileOptionButton;
import rip.crystal.practice.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class TrailsOptionsButton extends ProfileOptionButton {
    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.BOW).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.BOW).build();
    }

    @Override
    public String getOptionName() {
        return "&c&lTrails";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getEnabledOption() {
        return "";
    }

    @Override
    public String getDisabledOption() {
        return "";
    }

    @Override
    public boolean isEnabled(Player player) {
        return false;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.get(player.getUniqueId());
        //new KillEffectsMenu().openMenu(player);
    }
}
