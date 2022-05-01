package rip.crystal.practice.visual.leaderboard.menu.button;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.elo.EloUtil;
import rip.crystal.practice.utilities.menu.Button;

import java.util.List;

@AllArgsConstructor
public class StatsButton extends Button {

    public Player target;

    @Override
    public ItemStack getButtonItem(Player player){
        List<String> lore = Lists.newArrayList();
        Profile profile = Profile.get(target.getUniqueId());

        for (String s : cPractice.get().getLeaderboardConfig().getStringList("INVENTORY.PERSONAL_STATS.DESCRIPTION")) {
            if (s.contains("{kits}")) {
                Kit.getKits().stream().filter(kit -> kit.getGameRules().isRanked()).forEach(kit ->
                        lore.add(cPractice.get().getLeaderboardConfig().getString("INVENTORY.PERSONAL_STATS.KITS_FORMAT")
                                .replace("{kit}", kit.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().get(kit).getElo()))));
                continue;
            }
            lore.add(s
                    .replace("{bars}", CC.MENU_BAR)
                    .replace("{elo}", String.valueOf(EloUtil.getGlobalElo(profile))));
        }

        ItemStack item = new ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .name(cPractice.get().getLeaderboardConfig().getString("INVENTORY.PERSONAL_STATS.TITLE")
                        .replace("{color}", profile.getColor())
                        .replace("{name}", target.getName()))
                .lore(lore)
                .build();

        SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
        itemMeta.setOwner(target.getName());
        item.setItemMeta(itemMeta);
        return item;
    }
}
