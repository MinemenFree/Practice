package rip.crystal.practice.game.kit.command;

import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.kit.menu.KitEditEffectsMenu;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.regex.Pattern;

public class KitGameRuleCommand extends BaseCommand {

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Command(name = "kit.setrule", permission = "cpractice.kit.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0 || args.length == 1) {
            player.sendMessage(CC.RED + "Please usage: /kit setrule (kit) (rule) (value)");
            return;
        }

        Kit kit = Kit.getByName(args[0]);
        String rule = args[1];
        if (kit == null) {
            player.sendMessage(CC.RED + "A kit with that name does not exist.");
            return;
        }
        if (args.length == 1 && args[1].equalsIgnoreCase("effect"))  {
            player.sendMessage(CC.RED + "Please use: ");
            player.sendMessage(CC.RED + "/kit setrule " + kit.getName() + " effect list");
            player.sendMessage(CC.RED + "/kit setrule " + kit.getName() + " effect add (effect) (amplifier)");
            player.sendMessage(CC.RED + "/kit setrule " + kit.getName() + " effect remove (effect)");
            return;
        }

        if (args.length > 2) {
            String value = args[2];
            switch (rule.toLowerCase()) {
                case "build": {
                    kit.getGameRules().setBuild(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now build.");
                    else player.sendMessage(CC.RED + "Kit is no longer build.");
                    break;
                }
                case "spleef": {
                    kit.getGameRules().setSpleef(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now spleef.");
                    else player.sendMessage(CC.RED + "Kit is no longer spleef.");
                    break;
                }
                case "sumo": {
                    kit.getGameRules().setSumo(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now sumo.");
                    else player.sendMessage(CC.RED + "Kit is no longer sumo.");
                    break;
                }
                case "parkour": {
                    kit.getGameRules().setParkour(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now parkour.");
                    else player.sendMessage(CC.RED + "Kit is no longer parkour.");
                    break;
                }
                case "healthregeneration": {
                    kit.getGameRules().setHealthRegeneration(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now healthregeneration.");
                    else player.sendMessage(CC.RED + "Kit is no longer healthregeneration.");
                    break;
                }
                case "nofalldamage": {
                    kit.getGameRules().setNofalldamage(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now nofalldamage.");
                    else player.sendMessage(CC.RED + "Kit is no longer nofalldamage.");
                    break;
                }
                case "antifood": {
                    kit.getGameRules().setAntiFood(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now AntiFood.");
                    else player.sendMessage(CC.RED + "Kit is no longer AntiFood.");
                    break;
                }
                case "soup": {
                    kit.getGameRules().setSoup(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now soup.");
                    else player.sendMessage(CC.RED + "Kit is no longer soup.");
                    break;
                }
                case "showhealth": {
                    kit.getGameRules().setShowHealth(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now show health.");
                    else player.sendMessage(CC.RED + "Kit is no longer show health.");
                    break;
                }
                case "hcf": {
                    kit.getGameRules().setHcf(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now hcf.");
                    else player.sendMessage(CC.RED + "Kit is no longer hcf.");
                    break;
                }
                case "hcftrap": {
                    kit.getGameRules().setHcftrap(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now hcftrap.");
                    else player.sendMessage(CC.RED + "Kit is no longer hcftrap.");
                    break;
                }
                case "hitdelay": {
                    if (pattern.matcher(value).matches()) {
                        kit.getGameRules().setHitDelay(Integer.parseInt(value));
                        player.sendMessage(CC.GREEN + "Kit hitdelay is " + Integer.parseInt(value) + ".");
                    } else player.sendMessage(CC.RED + "Please insert valid value.");
                    break;
                }
                case "kbprofile": {
                    kit.getGameRules().setKbProfile(value);
                    player.sendMessage(CC.GREEN + "Kit now have kbprofile: " + value);
                    break;
                }
                case "bridge": {
                    kit.getGameRules().setBridge(Boolean.parseBoolean(value));

                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now bridge.");
                    else player.sendMessage(CC.RED + "Kit is no longer bridge.");

                    break;
                }
                case "ranked": {
                    kit.getGameRules().setRanked(Boolean.parseBoolean(value));

                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now ranked.");
                    else player.sendMessage(CC.RED + "Kit is no longer ranked.");

                    break;
                }
                case "boxing": {
                    kit.getGameRules().setBoxing(Boolean.parseBoolean(value));

                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now boxing.");
                    else player.sendMessage(CC.RED + "Kit is no longer boxing.");

                    break;
                }
                case "bedfight": {
                    kit.getGameRules().setBedFight(Boolean.parseBoolean(value));

                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now boxing.");
                    else player.sendMessage(CC.RED + "Kit is no longer boxing.");

                    break;
                }
                case "allowpotionfill": {
                    kit.getEditRules().setAllowPotionFill(Boolean.parseBoolean(value));
                    if (Boolean.parseBoolean(value)) player.sendMessage(CC.GREEN + "Kit is now allow potion fill.");
                    else player.sendMessage(CC.RED + "Kit is no longer allow potion fill.");
                    break;
                }
                case "effect": {
                    if (args.length == 2) {
                        player.sendMessage(CC.RED + "Please use: ");
                        player.sendMessage(CC.RED + "/kit setrule " + kit.getName() + " effect list");
                        player.sendMessage(CC.RED + "/kit setrule " + kit.getName() + " effect add (effect) (amplifier)");
                        player.sendMessage(CC.RED + "/kit setrule " + kit.getName() + " effect remove (effect)");
                        return;
                    }

                    if (args[2].equalsIgnoreCase("add")) {
                        if (args.length < 5) {
                            player.sendMessage(CC.RED + "Please usage: /kit setrule " + kit.getName() + "effect add (effect) (amplifier)");
                            return;
                        }

                        PotionEffectType effectType = PotionEffectType.getByName(args[3]);
                        if (effectType == null) {
                            player.sendMessage(CC.RED + "This effect doesn't exist.");
                            return;
                        }

                        int amplifier;
                        if (!StringUtils.isNumeric(args[4])) {
                            player.sendMessage(CC.RED + "Use a valid amplifier.");
                            return;
                        }
                        amplifier = Integer.parseInt(args[4]);

                        kit.getGameRules().getEffects().add(new PotionEffect(effectType, Integer.MAX_VALUE, amplifier - 1));
                        player.sendMessage(CC.RED + "Effect " + effectType.getName() + " added correctly.");
                    }
                    else if (args[2].equalsIgnoreCase("remove")) {
                        if (args.length < 4) {
                            player.sendMessage(CC.RED + "Please usage: /kit setrule " + kit.getName() + "effect add (effect) (amplifier)");
                            return;
                        }

                        PotionEffectType effectType = PotionEffectType.getByName(args[3]);
                        if (effectType == null) {
                            player.sendMessage(CC.RED + "This effect doesn't exist.");
                            return;
                        }

                        kit.getGameRules().getEffects().remove(kit.getGameRules().getEffects().stream().filter(potionEffect -> potionEffect.getType().getName().equalsIgnoreCase(args[3])).findFirst().get());
                        player.sendMessage(CC.RED + "Effect " + effectType.getName() + " removed correctly.");
                    }
                    else if (args[2].equalsIgnoreCase("list")) {
                        if (kit.getGameRules().getEffects().isEmpty()) {
                            player.sendMessage(CC.RED + "This list is empty.");
                            return;
                        }

                        for (PotionEffect effect : kit.getGameRules().getEffects()) {
                            player.sendMessage(CC.translate("&7[&c-&7] &b" + effect.getType().getName() + " &7(" + (effect.getAmplifier() + 1) + ")"));
                        }
                    }
                    break;
                }
                default: {
                    player.sendMessage(CC.RED + "A rule with that name does not exist.");
                    break;
                }
            }
            kit.save();
        } else {
            if (rule.equalsIgnoreCase("editoritems")) {
                kit.getEditRules().getEditorItems().clear();
                for (ItemStack content : player.getInventory().getContents()) {
                    if (content != null && content.getType() != Material.AIR)
                        kit.getEditRules().getEditorItems().add(content);
                }
                player.sendMessage(CC.GREEN + "Kit editor items update.");
            } else if (rule.equalsIgnoreCase("effectmenu")) {
                //new KitEditEffectsMenu(kit).openMenu(player);
            } else {
                player.sendMessage(CC.RED + "/kit setrule <kit> <rule> <value>");
            }
        }
    }
}
