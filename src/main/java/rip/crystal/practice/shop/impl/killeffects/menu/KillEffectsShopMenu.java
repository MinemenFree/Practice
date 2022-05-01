package rip.crystal.practice.shop.impl.killeffects.menu;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.cosmetics.impl.killeffects.KillEffectType;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class KillEffectsShopMenu extends PaginatedMenu
{

    @Override
    public String getPrePaginatedTitle(final Player player) {
        return ChatColor.DARK_GRAY + "Death Effects";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

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
                    .name((this.type.hasPermission(player) ? (CC.translate("&a&l")) : "&c&l") + this.type.getName())
                    .durability((profile.getKillEffectType() == this.type) ? 5 : (this.type.hasPermission(player) ? 3 : 14))
                    .lore(CC.MENU_BAR)
                    .lore(this.type.hasPermission(player) ? "&aYou already own this death effect!" : "&cYou don't own this death effect.")
                    .lore(this.type.hasPermission(player) ? "&aPrice: None!" : "&cPrice: &f" + this.type.getPrice())
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());

            if (this.type.hasPermission(player)) { // If player has permission.
                player.sendMessage(CC.translate("&aYou already own this death effect."));
                return;
            } else {
                if(profile.getCoins() == type.getPrice() || profile.getCoins() > type.getPrice()) {
                    profile.setCoins(profile.getCoins() - type.getPrice());
                    player.sendMessage(CC.translate("&aYou have purchased &9" + type.getName() + " &ffor &9" + type.getPrice() + " &fcoins."));

                    Bukkit.dispatchCommand(player, cPractice.get().getMainConfig().getString("PURCHASE-COSMETICS-CMD").replace("{player}", player.getName()).replace("{effect}", type.getName()));
                    return;
                } else {
                    player.sendMessage(CC.translate("&cYou don't have enough funds to buy this."));
                }
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

