package rip.crystal.practice.essentials.abilities;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.abilities.impl.*;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.TaskUtils;
import rip.crystal.practice.utilities.chat.CC;

import java.util.List;
import java.util.Set;

@Getter
public class AbilityManager {

    private Cookie cookie;
    private Rocket rocket;
    private TimeWarp timeWarp;
    private GuardianAngel guardianAngel;
    private Combo combo;
    private TankIngot tankIngot;
    private EffectDisabler effectDisabler;
    private Scrammbler scrammbler;
    private Strength strength;
    private SwapperAxe swapperAxe;
    private Switcher switcher;
    private NinjaStar ninjaStar;
    private PocketBard pocketBard;
    private AntiTrapper antitrapper;

    public AbilityManager() {
        this.guardianAngel = new GuardianAngel();
        this.combo = new Combo();
        this.antitrapper = new AntiTrapper();
        this.effectDisabler = new EffectDisabler();
        this.ninjaStar = new NinjaStar();
        this.pocketBard = new PocketBard();
        this.scrammbler = new Scrammbler();
        this.strength = new Strength();
        this.swapperAxe = new SwapperAxe();
        this.switcher = new Switcher();
        this.tankIngot = new TankIngot();
        this.rocket = new Rocket();
        this.cookie = new Cookie();
        this.timeWarp = new TimeWarp();
    }


    public void load() {

        Ability.getAbilities().forEach(Ability::register);
    }

    public ItemStack getAbility(String ability, int amount) {
        return new ItemBuilder(getMaterial(ability))
                .amount(amount)
                .data(getData(ability))
                .name(getDisplayName(ability))
                .lore(getDescription(ability))
                .build();
    }

    public String getDisplayName(String ability) {
        return cPractice.get().getAbilityConfig().getString( ability + ".ICON.DISPLAYNAME");
    }

    public List<String> getDescription(String ability) {
        return cPractice.get().getAbilityConfig().getStringList( ability + ".ICON.DESCRIPTION");
    }

    public Material getMaterial(String ability) {
        return Material.valueOf(cPractice.get().getAbilityConfig().getString(ability + ".ICON.MATERIAL"));
    }

    public int getData(String ability) {
        return cPractice.get().getAbilityConfig().getInteger(ability + ".ICON.DATA");
    }

    public int getCooldown(String ability) {
        return cPractice.get().getAbilityConfig().getInteger(ability + ".COOLDOWN");
    }

    public Set<String> getAbilities() {
        return cPractice.get().getAbilityConfig().getConfiguration().getKeys(false);
    }

    public void giveAbility(CommandSender sender, Player player, String key, String abilityName, int amount) {
        player.getInventory().addItem(this.getAbility(key, amount));
        if (player == sender) {
            CC.message(player, cPractice.get().getAbilityConfig().getString("RECEIVED_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount)));
        }
        else {
            CC.message(player, cPractice.get().getAbilityConfig().getString("RECEIVED_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount)));
            CC.sender(sender, cPractice.get().getAbilityConfig().getString("GIVE_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount))
                    .replace("%PLAYER%", player.getName()));
        }
    }

    public void playerMessage(Player player, String ability) {
        String displayName = getDisplayName(ability);
        String cooldown = String.valueOf(getCooldown(ability));

        cPractice.get().getAbilityConfig().getStringList(ability + ".MESSAGE.PLAYER").forEach(
                message -> CC.message(player, message
                        .replace("%ABILITY%", displayName)
                        .replace("%COOLDOWN%", cooldown)));
    }

    public void targetMessage(Player target, Player player, String ability) {
        String displayName = getDisplayName(ability);

        cPractice.get().getAbilityConfig().getStringList(ability + ".MESSAGE.TARGET").forEach(
                message -> CC.message(target, message
                        .replace("%ABILITY%", displayName)
                        .replace("%PLAYER%", player.getName())));
    }

    public void cooldown(Player player, String abilityName, String cooldown) {
        CC.message(player, cPractice.get().getAbilityConfig().getString("STILL_ON_COOLDOWN")
                .replace("%ABILITY%", abilityName)
                .replace("%COOLDOWN%", cooldown));
    }

    public void cooldownExpired(Player player, String abilityName, String ability) {
        TaskUtils.runLaterAsync(() ->
                CC.message(player, cPractice.get().getAbilityConfig().getString("COOLDOWN_EXPIRED")
                        .replace("%ABILITY%", abilityName)), getCooldown(ability) * 20L);
    }
}
