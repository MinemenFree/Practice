package rip.crystal.practice.visual.leaderboard.menu.button;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.meta.ProfileKitData;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.visual.leaderboard.Leaderboard;
import rip.crystal.practice.visual.leaderboard.entry.LeaderboardKitsEntry;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class KitButton extends Button {

    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = Lists.newArrayList();
        List<LeaderboardKitsEntry> leaderboard = Leaderboard.getKitLeaderboards().get(kit.getName()).stream().limit(10).collect(Collectors.toList());

        int pos = 0;

        lore.add(CC.MENU_BAR);
        for (LeaderboardKitsEntry leaderboardKitsEntry : leaderboard) {
            pos++;
            Profile profile = leaderboardKitsEntry.getProfile();
            if (profile != null) {
                if (pos == 1) {
                    List<String> first = cPractice.get().getLeaderboardConfig().getStringList("INVENTORY.KIT.POSITIONS.1");
                    for (String s : first) {
                        lore.add(s
                                .replace("{pos}", String.valueOf(pos))
                                .replace("{name}", profile.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getElo()))
                                .replace("{winstreak}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getKillstreak()))
                                .replace("{bars}", CC.MENU_BAR));
                    }
                } else if (pos == 2) {
                    List<String> second = cPractice.get().getLeaderboardConfig().getStringList("INVENTORY.KIT.POSITIONS.2");
                    for (String s : second) {
                        lore.add(s
                                .replace("{pos}", String.valueOf(pos))
                                .replace("{name}", profile.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getElo()))
                                .replace("{winstreak}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getKillstreak()))
                                .replace("{bars}", CC.MENU_BAR));
                    }
                } else if (pos == 3) {
                    List<String> third = cPractice.get().getLeaderboardConfig().getStringList("INVENTORY.KIT.POSITIONS.3");
                    for (String s : third) {
                        lore.add(s
                                .replace("{pos}", String.valueOf(pos))
                                .replace("{name}", profile.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getElo()))
                                .replace("{winstreak}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getKillstreak()))
                                .replace("{bars}", CC.MENU_BAR));
                    }
                } else {
                    List<String> another = cPractice.get().getLeaderboardConfig().getStringList("INVENTORY.KIT.POSITIONS.ANOTHER");
                    for (String s : another) {
                        lore.add(s
                                .replace("{pos}", String.valueOf(pos))
                                .replace("{name}", profile.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getElo()))
                                .replace("{winstreak}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getKillstreak()))
                                .replace("{bars}", CC.MENU_BAR));
                    }
                }
            }
        }
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(kit.getDisplayIcon().getType())
                .name(cPractice.get().getLeaderboardConfig().getString("INVENTORY.KIT.TITLE").replace("{kit}", kit.getName()))
                .durability(kit.getDisplayIcon().getDurability())
                .lore(lore)
                .build();
    }
}
