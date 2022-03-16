package rip.crystal.practice.shop.impl.trails.menu;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.player.cosmetics.impl.trails.TrailsEffectType;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class TrailEffectsShopMenu extends PaginatedMenu
{

    @Override
    public String getPrePaginatedTitle(final Player player) {
        return "&9&lTrail Effects Shop";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        for (TrailsEffectType trailEffectTypes : TrailsEffectType.values()) {
            buttons.put(buttons.size(), new SettingsButton(trailEffectTypes));
        }
        return buttons;
    }

    private static class SettingsButton extends Button
    {
        private TrailsEffectType type;

        @Override
        public ItemStack getButtonItem(final Player player) {
            final Profile profile = Profile.get(player.getUniqueId());
            return new ItemBuilder(type.getMaterial())
                    .name((this.type.hasPermission(player) ? (CC.translate("&a&l")) : "&c&l") + this.type.getName())
                    .durability((profile.getTrailsEffectType() == this.type) ? 5 : (this.type.hasPermission(player) ? 3 : 14))
                    .lore(CC.MENU_BAR)
                    .lore(this.type.hasPermission(player) ? "&aYou already own this effect!" : "&cYou don't own this kill effect.")
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            final Profile profile = Profile.get(player.getUniqueId());
            if (this.type.hasPermission(player)) {
                player.sendMessage(CC.translate("&aYou already own this effect."));
                return;
            } else {
                // purchase logic here
            }
            player.closeInventory();
            TaskUtil.runAsync(profile::save);
        }

        @Override
        public boolean shouldUpdate(final Player player, final ClickType clickType) {
            return true;
        }

        public SettingsButton(final TrailsEffectType type) {
            this.type = type;
        }
    }
}

