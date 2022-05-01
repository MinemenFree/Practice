package rip.crystal.practice.essentials.abilities.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.utilities.JavaUtils;
import rip.crystal.practice.utilities.chat.CC;

@Getter
@Setter
public class AbilityCommand extends BaseCommand {

    private cPractice plugin = cPractice.get();

    @Command(name = "ability", permission = "cpractice.command.ability")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if (args.length < 1) {
            this.getUsage(player, "ability");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "give":
                if (args.length < 4) {
                    CC.sender(player, "&cUsage: /" + "ability" + " give <player> <ability|all> <amount>");
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    CC.sender(player, "&cPlayer '" + args[1] + "' not found.");
                    return;
                }

                Integer amount = JavaUtils.tryParseInt(args[3]);

                if (amount == null) {
                    CC.sender(player, "&cAmount must be a number.");
                    return;
                }
                if (amount <= 0) {
                    CC.sender(player, "&cAmount must be positive.");
                    return;
                }

                plugin.getAbilityManager().getAbilities().forEach(ability -> {
                    String displayName = cPractice.get().getAbilityConfig().getString(ability + ".ICON.DISPLAYNAME");
                    if (args[2].equalsIgnoreCase(ability)) {
                        plugin.getAbilityManager().giveAbility(player, target, ability, displayName, amount);
                        return;
                    }
                    if (args[2].equals("all")) {
                        plugin.getAbilityManager().giveAbility(player, target, ability, displayName, amount);
                    }
                });
                break;
            case "list":
                CC.sender(player, "&7&m-----------------------------");
                CC.sender(player, "&3&lAbilities List &7(" + Ability.getAbilities().size() + ")");
                CC.sender(player, "");
                plugin.getAbilityManager().getAbilities().forEach(
                        ability -> CC.sender(player, " &7- &b" + ability));
                CC.sender(player, "&7&m-----------------------------");
                break;
        }
        return;
    }

    private void getUsage(CommandSender sender, String label) {
        CC.sender(sender, "&7&m-----------------------------");
        CC.sender(sender, "&3&lAbility Help");
        CC.sender(sender, "");
        CC.sender(sender, "&b/" + label + " give <player> <ability|all> <amount>");
        CC.sender(sender, "&b/" + label + " list");
        CC.sender(sender, "&7&m-----------------------------");
    }
}
