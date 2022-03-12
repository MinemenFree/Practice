package rip.crystal.practice.player.profile.meta.option.button;

import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.meta.option.menu.ProfileOptionButton;
import rip.crystal.practice.visual.tablist.TabType;
import rip.crystal.practice.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ChangeTabTypeOptionButton extends ProfileOptionButton {

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.CARPET).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.CARPET).build();
    }

    @Override
    public String getOptionName() {
        return "Change TabType";
    }

    @Override
    public String getDescription() {
        return "If you don't like this Tablist mode you can change it";
    }

    @Override
    public String getEnabledOption() {
        return "Default Type";
    }

    @Override
    public String getDisabledOption() {
        return "Weight Type";
    }

    @Override
    public boolean isEnabled(Player player) {
        return Profile.get(player.getUniqueId()).getTabType() == TabType.DEFAULT;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getTabType() == TabType.DEFAULT) profile.setTabType(TabType.WEIGHT);
        else profile.setTabType(TabType.DEFAULT);
    }
}
