package rip.crystal.practice.player.profile.menu.matchmaking;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;

import java.util.ArrayList;
import java.util.List;

public class PingRangeButton extends Button {
    private final String name;
    private final Material material;
    private final int durability;
    private final List<String> lore;

    public PingRangeButton(String string, Material material, int n, List<String> list) {
        this.name = string;
        this.material = material;
        this.durability = n;
        this.lore = list;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ArrayList<String> arrayList = new ArrayList<>(this.lore);
        Profile profile = Profile.get(player.getUniqueId());

        arrayList.add(CC.translate("&7Ping Range: &a" + (profile.getPingRange() == -1 ? "Unrestricted" : Integer.valueOf(profile.getPingRange()))));
        arrayList.add(CC.CHAT_BAR);
        return new ItemBuilder(this.material).name(CC.translate(this.name)).amount(1).lore(arrayList).durability(this.durability).build();
    }

    @Override
    public void clicked(Player player, int n, ClickType clickType, int n2) {
        Profile profile = Profile.get(player.getUniqueId());
        MatchMakingMenu matchmakingMenu = new MatchMakingMenu();
        PingRangeButton.playSuccess(player);
        String[] stringArray = ChatColor.stripColor((this.getButtonItem(player).getItemMeta().getLore().get(8))).split(":");
        int n3 = matchmakingMenu.handleRangeClick(clickType, matchmakingMenu.getPingRanges, matchmakingMenu.parseOrDefault(stringArray[1], -1));
        profile.setPingRange(n3);
    }

    @Override
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }
}

