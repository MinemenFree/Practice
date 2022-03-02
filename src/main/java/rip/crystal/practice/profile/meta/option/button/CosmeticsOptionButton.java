package rip.crystal.practice.profile.meta.option.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cosmetics.impl.killeffects.menu.KillEffectsMenu;
import rip.crystal.practice.cosmetics.menu.CosmeticsMenu;
import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.meta.option.menu.ProfileOptionButton;
import rip.crystal.practice.utilities.ItemBuilder;

public class CosmeticsOptionButton extends ProfileOptionButton {

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.BLAZE_ROD).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.BLAZE_ROD).build();
    }

    @Override
    public String getOptionName() {
        return "&b&lCosmetics";
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
        new CosmeticsMenu().openMenu(player);
    }

}
