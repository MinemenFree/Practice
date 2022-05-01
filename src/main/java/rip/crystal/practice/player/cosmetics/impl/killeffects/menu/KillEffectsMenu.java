package rip.crystal.practice.player.cosmetics.impl.killeffects.menu;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.player.cosmetics.impl.killeffects.KillEffectType;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class KillEffectsMenu extends PaginatedMenu
{

    @Override
    public String getPrePaginatedTitle(final Player player) {
        return ChatColor.DARK_GRAY + "Death Effects";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        //Stream.of(KillEffectType.values()).forEach(type -> buttons.put(KillEffectType.values().length, new SettingsButton(type)));
        for (KillEffectType killEffectTypes : KillEffectType.values()) {
            buttons.put(buttons.size(), new SettingsButton(killEffectTypes));
        }
        return buttons;
    }

    private static class SettingsButton extends Button
    {
        private KillEffectType type;

        @Override
        public ItemStack getButtonItem(final Player player) {
            final Profile profile = Profile.get(player.getUniqueId());
            return new ItemBuilder(type.getMaterial())
                    .name((profile.getKillEffectType() == this.type) ? "&a&l" : (this.type.hasPermission(player) ? (CC.translate("&c&l")) : "&c&l") + this.type.getName())
                    .durability((profile.getKillEffectType() == this.type) ? 5 : (this.type.hasPermission(player) ? 3 : 14))
                    .lore(CC.MENU_BAR)
                    .lore("&7Left click to change your")
                    .lore("&7death effect to to " + "&c" + this.type.getName() + "&7.")
                    .lore("")
                    .lore("&7Selected Death Effect: " + "&c" + ((profile.getKillEffectType() != null) ? profile.getKillEffectType().getName() : "&cNone"))
                    .lore((profile.getKillEffectType() == this.type) ? "&aThat death effect is already selected." : (this.type.hasPermission(player) ? "&7Click to select this death effect." : "&cYou don't own this death effect."))
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            final Profile profile = Profile.get(player.getUniqueId());
            if (!this.type.hasPermission(player)) {
                player.sendMessage(CC.translate("&fYou don't have the &c" + this.type.getName() + "&f death effect. Purchase it at &c store.hy-pvp.com" + "&f."));
            }
            else if (profile.getKillEffectType() == this.type) {
                player.sendMessage(CC.translate("&c" + this.type.getName() + "&f death effect is already selected."));
            }
            else {
                profile.setKillEffectType(this.type);
                player.sendMessage(CC.translate("&c" + this.type.getName() + "&f is now set as your death effect."));
            }
            player.closeInventory();
            TaskUtil.runAsync(profile::save);
        }

        @Override
        public boolean shouldUpdate(final Player player, final ClickType clickType) {
            return true;
        }

        public SettingsButton(final KillEffectType type) {
            this.type = type;
        }
    }
}

