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
        private final KillEffectType killEffectType;

        @Override
        public ItemStack getButtonItem(final Player player) {
            final Profile profile = Profile.get(player.getUniqueId());
            return new ItemBuilder(this.killEffectType.getMaterial())
                    .name(ChatColor.BLUE + this.killEffectType.getName())
                    .durability((profile.getKillEffectType() == this.killEffectType) ? 5 : (this.killEffectType.hasPermission(player) ? 3 : 14))
                    .lore(CC.MENU_BAR)
                    .lore(this.killEffectType.hasPermission(player) ? "&aYou already own this death effect!" : "&cYou don't own this death effect.")
                    .lore(this.killEffectType.hasPermission(player) ? "&aPrice: None!" : "&cPrice: &f" + this.killEffectType.getPrice())
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());

            if (this.killEffectType.hasPermission(player)) { // If player has permission.
                player.sendMessage(CC.translate("&aYou already own this death effect."));
                return;
            } else {
                if(profile.getCoins() == killEffectType.getPrice() || profile.getCoins() > killEffectType.getPrice()) {
                    profile.setCoins(profile.getCoins() - killEffectType.getPrice());
                    player.sendMessage(CC.translate("&aYou have purchased &9" + killEffectType.getName() + " &ffor &9" + killEffectType.getPrice() + " &fcoins."));

                    Bukkit.dispatchCommand(player, cPractice.get().getMainConfig().getString("PURCHASE-COSMETICS-CMD").replace("<player>", player.getName()).replace("<effect>", killEffectType.getName()));
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
            this.killEffectType = type;
        }
    }
}

