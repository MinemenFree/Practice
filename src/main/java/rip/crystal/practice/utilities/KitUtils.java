package rip.crystal.practice.utilities;

import rip.crystal.practice.kit.Kit;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.profile.Profile;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class KitUtils {

    public static void giveBridgeKit(Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();
        ItemStack[] armorRed = InventoryUtil.leatherArmor(Color.RED);
        ItemStack[] armorBlue = InventoryUtil.leatherArmor(Color.BLUE);
        if(teamMatch.getParticipantA() == null) {
            return;
        }
        if(teamMatch.getParticipantB() == null) {
            return;
        }
        if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
            player.getInventory().setArmorContents(armorRed);
            player.getInventory().all(Material.STAINED_CLAY).forEach((key, value) -> {
                player.getInventory().setItem(key, new ItemBuilder(Material.STAINED_CLAY).durability(14).amount(64).build());
                player.getInventory().setItem(key, new ItemBuilder(Material.STAINED_CLAY).durability(14).amount(64).build());
            });
        } else {
            player.getInventory().setArmorContents(armorBlue);
            player.getInventory().all(Material.STAINED_CLAY).forEach((key, value) -> {
                player.getInventory().setItem(key, new ItemBuilder(Material.STAINED_CLAY).durability(11).amount(64).build());
                player.getInventory().setItem(key, new ItemBuilder(Material.STAINED_CLAY).durability(11).amount(64).build());
            });
        }
        player.updateInventory();
    }

    public static void giveBaseRaidingKit(Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();
        ItemStack[] armorRed = InventoryUtil.leatherArmor(Color.RED);
        ItemStack[] armorBlue = InventoryUtil.leatherArmor(Color.BLUE);
        Kit kit;
        if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
            //player.getInventory().setArmorContents(armorRed);
            kit = Kit.getByName("HCFRaider");
            player.getInventory().setArmorContents(Objects.requireNonNull(kit).getKitLoadout().getArmor());
            player.getInventory().setContents(kit.getKitLoadout().getContents());
        } else {
            //player.getInventory().setArmorContents(armorBlue);
            kit = Kit.getByName("HCFTrapper");
            player.getInventory().setArmorContents(Objects.requireNonNull(kit).getKitLoadout().getArmor());
            player.getInventory().setContents(kit.getKitLoadout().getContents());
        }
        player.updateInventory();
    }

}