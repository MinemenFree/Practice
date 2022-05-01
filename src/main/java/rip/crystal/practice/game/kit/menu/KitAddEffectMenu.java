package rip.crystal.practice.game.kit.menu;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.Map;

@AllArgsConstructor
public class KitAddEffectMenu extends Menu {
    
    private final Kit kit;

    {
        setUpdateAfterClick(true);
    }
    
    @Override
    public String getTitle(Player player) {
        return "&cAdd effect for &f" + kit.getName();
    }

    @Override
    public int getSize() {
        return 9*2;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        for (PotionEffectType value : PotionEffectType.values()) {
            buttons.put(buttons.size(), new EffectButton(value, kit));
        }
        
        return buttons;
    }

    @AllArgsConstructor
    private static class EffectButton extends Button {

        private final PotionEffectType type;
        private final Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.GLASS_BOTTLE)
//                    .name((kit.getGameRules().getEffects().contains(type.getName()) ? CC.RED : CC.GREEN) + type.getName())
//                    .lore("",
//                            (kit.getGameRules().getEffects().contains(type.getName()) ?
//                            "&cYour Kit already added this effect" :
//                            "&aYou can add this effect to your Kit"),
//                    "")
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
//            if (kit.getGameRules().getEffects().contains(type.getName())) {
//                kit.getGameRules().getEffects().remove(type.getName());
//                player.sendMessage(CC.translate("&aThe effect '" + type.getName() + "' has been added to " + kit.getName() + " Kit"));
//            } else {
//                kit.getGameRules().getEffects().add(type.getName());
//                player.sendMessage(CC.translate("&cThe effect '" + type.getName() + "' has been removed of " + kit.getName() + " Kit"));
//            }
        }
    }
}
