package rip.crystal.practice.game.kit;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.meta.KitEditRules;
import rip.crystal.practice.game.kit.meta.KitGameRules;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.utilities.InventoryUtil;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.PotionUtil;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Kit {

	@Getter private static final List<Kit> kits = new ArrayList<>();

	private final String name;
	@Setter private boolean enabled;
	@Setter private ItemStack displayIcon;
	@Setter private int slot;
	private final KitLoadout kitLoadout = new KitLoadout();
	private final KitEditRules editRules = new KitEditRules();
	private final KitGameRules gameRules = new KitGameRules();

	public Kit(String name) {
		this.name = name;
		this.displayIcon = new ItemStack(Material.DIAMOND_SWORD);
	}

	public ItemStack getDisplayIcon() {
		return this.displayIcon.clone();
	}

	public void save() {
		String path = "kits." + name;

		FileConfiguration configFile = cPractice.get().getKitsConfig().getConfiguration();
		configFile.set(path + ".enabled", enabled);
		configFile.set(path + ".slot", slot);
		configFile.set(path + ".icon.material", displayIcon.getType().name());
		configFile.set(path + ".icon.durability", displayIcon.getDurability());

		configFile.set(path + ".loadout.armor", InventoryUtil.itemStackArrayToBase64(kitLoadout.getArmor()));
		configFile.set(path + ".loadout.contents", InventoryUtil.itemStackArrayToBase64(kitLoadout.getContents()));

		configFile.set(path + ".game-rules.allow-build", gameRules.isBuild());
		configFile.set(path + ".game-rules.spleef", gameRules.isSpleef());
		configFile.set(path + ".game-rules.parkour", gameRules.isParkour());
		configFile.set(path + ".game-rules.soup", gameRules.isSoup());
		configFile.set(path + ".game-rules.sumo", gameRules.isSumo());
		configFile.set(path + ".game-rules.bridge", gameRules.isBridge());
		configFile.set(path + ".game-rules.health-regeneration", gameRules.isHealthRegeneration());
		configFile.set(path + ".game-rules.antifood", gameRules.isAntiFood());
		configFile.set(path + ".game-rules.nofalldamage", gameRules.isNofalldamage());
		configFile.set(path + ".game-rules.show-health", gameRules.isShowHealth());
		configFile.set(path + ".game-rules.hit-delay", gameRules.getHitDelay());
		configFile.set(path + ".game-rules.ranked", gameRules.isRanked());
		configFile.set(path + ".game-rules.hcf", gameRules.isHcf());
		configFile.set(path + ".game-rules.hcftrap", gameRules.isHcftrap());
		configFile.set(path + ".game-rules.effects",
				gameRules.getEffects().stream().map(PotionUtil::convertPotionEffectToString).collect(Collectors.toList()));
		configFile.set(path + ".game-rules.boxing", gameRules.isBoxing());
		configFile.set(path + ".game-rules.bedfight", gameRules.isBedFight());
		configFile.set(path + ".game-rules.kbprofile", gameRules.getKbProfile());
		configFile.set(path + ".edit-rules.allow-potion-fill", editRules.isAllowPotionFill());

		cPractice.get().getKitsConfig().save();
		cPractice.get().getKitsConfig().reload();
	}

	public void delete() {
		String path = "kits." + name;

		BasicConfigurationFile configFile = cPractice.get().getKitsConfig();
		configFile.getConfiguration().set(path, null);

		configFile.save();
		configFile.reload();

		kits.remove(this);
	}

	public static void init() {
		FileConfiguration config = cPractice.get().getKitsConfig().getConfiguration();

		for (String key : config.getConfigurationSection("kits").getKeys(false)) {
			String path = "kits." + key;

			Kit kit = new Kit(key);
			kit.setEnabled(config.getBoolean(path + ".enabled"));

			kit.setSlot(config.getInt(path + ".slot"));

			kit.setDisplayIcon(new ItemBuilder(Material.valueOf(config.getString(path + ".icon.material")))
					.durability(config.getInt(path + ".icon.durability"))
					.build());

			if (config.contains(path + ".loadout.armor")) {
				try {
					kit.getKitLoadout().setArmor(InventoryUtil.itemStackArrayFromBase64(config.getString(path + ".loadout.armor")));
				} catch (IOException ignore) {
					System.out.print("THIS KIT DOESN'T SAVE!");
				}
			}

			if (config.contains(path + ".loadout.contents")) {
				try {
					kit.getKitLoadout().setContents(InventoryUtil.itemStackArrayFromBase64(config.getString(path + ".loadout.contents")));
				} catch (IOException ignore) {
					System.out.print("THIS KIT DOESN'T SAVE!");
				}
			}

			kit.getGameRules().setBuild(config.getBoolean(path + ".game-rules.allow-build"));
			kit.getGameRules().setSpleef(config.getBoolean(path + ".game-rules.spleef"));
			kit.getGameRules().setParkour(config.getBoolean(path + ".game-rules.parkour"));
			kit.getGameRules().setSumo(config.getBoolean(path + ".game-rules.sumo"));
			kit.getGameRules().setSoup(config.getBoolean(path + ".game-rules.soup"));
			kit.getGameRules().setBridge(config.getBoolean(path + ".game-rules.bridge"));
			kit.getGameRules().setHealthRegeneration(config.getBoolean(path + ".game-rules.health-regeneration"));
			kit.getGameRules().setNofalldamage(config.getBoolean(path + ".game-rules.nofalldamage"));
			kit.getGameRules().setAntiFood(config.getBoolean(path + ".game-rules.antifood"));
			kit.getGameRules().setShowHealth(config.getBoolean(path + ".game-rules.show-health"));
			kit.getGameRules().setHitDelay(config.getInt(path + ".game-rules.hit-delay"));
			kit.getGameRules().setRanked(config.getBoolean(path + ".game-rules.ranked"));
			kit.getGameRules().setHcf(config.getBoolean(path + ".game-rules.hcf"));
			kit.getGameRules().setHcftrap(config.getBoolean(path + ".game-rules.hcftrap"));
			kit.getGameRules().setBoxing(config.getBoolean(path + ".game-rules.boxing"));
			kit.getGameRules().setBedFight(config.getBoolean(path + ".game-rules.bedfight"));
			for (PotionEffect potionEffect : config.getStringList(path + ".game-rules.effects").stream()
					.map(PotionUtil::convertStringToPotionEffect).collect(Collectors.toList())) {
				kit.getGameRules().getEffects().add(potionEffect);
			}
			kit.getGameRules().setKbProfile(config.getString(path + ".game-rules.kbprofile"));
			kit.getEditRules().setAllowPotionFill(config.getBoolean(".edit-rules.allow-potion-fill"));

			if (config.getConfigurationSection(path + ".edit-rules.items") != null) {
				for (String itemKey : config.getConfigurationSection(path + ".edit-rules.items").getKeys(false)) {
					kit.getEditRules().getEditorItems().add(new ItemBuilder(Material.valueOf(
							config.getString(path + ".edit-rules.items." + itemKey + ".material")))
							.durability(config.getInt(path + ".edit-rules.items." + itemKey + ".durability"))
							.amount(config.getInt(path + ".edit-rules.items." + itemKey + ".amount"))
							.build());
				}
			}

			kits.add(kit);
		}

		kits.forEach(kit -> {
			if (kit.isEnabled()) {
				new Queue(kit, false);
				if(kit.getGameRules().isRanked()){
					new Queue(kit, true);
				}
			}
		});
	}

	public static Kit getByName(String name) {
		for (Kit kit : kits) {
			if (kit.getName().equalsIgnoreCase(name)) {
				return kit;
			}
		}
		return null;
	}
}
