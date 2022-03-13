package rip.crystal.practice.player.cosmetics.impl.trails.menu;

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

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TrailsEffectsMenu extends PaginatedMenu
{

    @Override
    public String getPrePaginatedTitle(final Player player) {
        return Color.DARK_GRAY + "Trails";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        //Stream.of(KillEffectType.values()).forEach(type -> buttons.put(KillEffectType.values().length, new SettingsButton(type)));
        for (TrailsEffectType trailsEffectType : TrailsEffectType.values()) {
            buttons.put(buttons.size(), new SettingsButton(trailsEffectType));
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
                    .name((profile.getTrailsEffectType() == this.type) ? "&a&l" : (this.type.hasPermission(player) ? (CC.translate("&a&l")) : "&c&l") + this.type.getName())
                    .durability((profile.getTrailsEffectType() == this.type) ? 5 : (this.type.hasPermission(player) ? 3 : 14))
                    .lore(CC.MENU_BAR)
                    .lore("&7Left click to change your")
                    .lore("&7trail effect to to " + "&c" + this.type.getName() + "&7.")
                    .lore("")
                    .lore("&7Selected Kill Effect: " + "&c" + ((profile.getKillEffectType() != null) ? profile.getKillEffectType().getName() : "&cNone"))
                    .lore((profile.getTrailsEffectType() == this.type) ? "&aThat trail effect is already selected." : (this.type.hasPermission(player) ? "&7Click to select this trail effect." : "&cYou don't own this trail effect."))
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            final Profile profile = Profile.get(player.getUniqueId());
            if (!this.type.hasPermission(player)) {
                player.sendMessage(CC.translate("&fYou don't have the &c" + this.type.getName() + "&f kill effect. Purchase it at &c store.hy-pvp.com" + "&f."));
            }
            else if (profile.getTrailsEffectType() == this.type) {
                player.sendMessage(CC.translate("&c" + this.type.getName() + "&f kill effect is already selected."));
            }
            else {
                profile.setTrailsEffectType(this.type);
                player.sendMessage(CC.translate("&c" + this.type.getName() + "&f is now set as your kill effect."));
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

