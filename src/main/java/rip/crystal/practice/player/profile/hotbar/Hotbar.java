package rip.crystal.practice.player.profile.hotbar;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.game.EventGameState;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.hotbar.entry.HotbarEntry;
import rip.crystal.practice.player.profile.hotbar.impl.HotbarItem;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.PlayerUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Hotbar {

	@Getter
	private static final Map<HotbarItem, HotbarEntry> items = new HashMap<>();

	public static void init() {
		FileConfiguration config = cPractice.get().getHotbarConfig().getConfiguration();

		for (HotbarItem hotbarItem : HotbarItem.values()) {
			try {
				String path = "HOTBAR_ITEMS." + hotbarItem.name() + ".";
				int slot = config.getInt(path + "SLOT");

				ItemBuilder builder = new ItemBuilder(Material.valueOf(config.getString(path + "MATERIAL")));
				builder.durability(config.getInt(path + "DURABILITY"));
				builder.name(config.getString(path + "NAME"));
				builder.lore(config.getStringList(path + "LORE"));

				HotbarEntry hotbarEntry;
				if (slot > 0) hotbarEntry = new HotbarEntry(builder.build(), slot - 1);
				else hotbarEntry = new HotbarEntry(builder.build(), -1);

				items.put(hotbarItem, hotbarEntry);
			} catch (Exception e) {
				System.out.print("Failed to parse item " + hotbarItem.name());
			}
		}

		Map<HotbarItem, String> dynamicContent = new HashMap<>();
		dynamicContent.put(HotbarItem.MAP_SELECTION, "%MAP%");
		dynamicContent.put(HotbarItem.KIT_SELECTION, "%KIT%");

		for (Map.Entry<HotbarItem, String> entry : dynamicContent.entrySet()) {
			String voteName = Hotbar.getItems().get(entry.getKey()).getItemStack().getItemMeta().getDisplayName();
			String[] nameSplit = voteName.split(entry.getValue());

			entry.getKey().setPattern(
					Pattern.compile("(" + nameSplit[0] + ")(.*)(" + (nameSplit.length > 1 ? nameSplit[1] : "") + ")"));
		}
	}

	public static void giveHotbarItems(Player player) {
		Profile profile = Profile.get(player.getUniqueId());

		ItemStack[] itemStacks = new ItemStack[9];
		Arrays.fill(itemStacks, null);

		boolean activeRematch = profile.getRematchData() != null;
		boolean activeEvent = EventGame.getActiveGame() != null &&
				EventGame.getActiveGame().getGameState() == EventGameState.WAITING_FOR_PLAYERS;

		switch (profile.getState()) {
			case LOBBY: {
				if (profile.getParty() == null) {
					if(cPractice.get().getMainConfig().getBoolean("QUEUES.ENABLED")) {
						itemStacks[getSlot(HotbarItem.QUEUES_JOIN)] = getItem(HotbarItem.QUEUES_JOIN);
					} else {
						itemStacks[getSlot(HotbarItem.QUEUE_JOIN_UNRANKED)] = getItem(HotbarItem.QUEUE_JOIN_UNRANKED);
						itemStacks[getSlot(HotbarItem.QUEUE_JOIN_RANKED)] = getItem(HotbarItem.QUEUE_JOIN_RANKED);
					}
					itemStacks[getSlot(HotbarItem.KIT_EDITOR)] = getItem(HotbarItem.KIT_EDITOR);
					itemStacks[getSlot(HotbarItem.SETTINGS)] = getItem(HotbarItem.SETTINGS);

					if (activeRematch && activeEvent) {
						if (profile.getRematchData().isReceive()) itemStacks[getSlot(HotbarItem.REMATCH_ACCEPT)] = getItem(HotbarItem.REMATCH_ACCEPT);
						else itemStacks[getSlot(HotbarItem.REMATCH_REQUEST)] = getItem(HotbarItem.REMATCH_REQUEST);

						itemStacks[getSlot(HotbarItem.EVENT_JOIN)] = getItem(HotbarItem.EVENT_JOIN);
						itemStacks[getSlot(HotbarItem.PARTY_CREATE)] = getItem(HotbarItem.PARTY_CREATE);
					} else if (activeRematch) {
						if (profile.getRematchData().isReceive()) itemStacks[getSlot(HotbarItem.REMATCH_ACCEPT)] = getItem(HotbarItem.REMATCH_ACCEPT);
						else itemStacks[getSlot(HotbarItem.REMATCH_REQUEST)] = getItem(HotbarItem.REMATCH_REQUEST);

						itemStacks[getSlot(HotbarItem.PARTY_CREATE)] = getItem(HotbarItem.PARTY_CREATE);
					} else if (activeEvent) {
						itemStacks[getSlot(HotbarItem.EVENT_JOIN)] = getItem(HotbarItem.EVENT_JOIN);
						itemStacks[getSlot(HotbarItem.PARTY_CREATE)] = getItem(HotbarItem.PARTY_CREATE);
					} else
						itemStacks[getSlot(HotbarItem.PARTY_CREATE)] = getItem(HotbarItem.PARTY_CREATE);

					itemStacks[getSlot(HotbarItem.LEADERBOARD_MENU)] = getItem(HotbarItem.LEADERBOARD_MENU);
					itemStacks[getSlot(HotbarItem.EVENT_SELECT)] = getItem(HotbarItem.EVENT_SELECT);
				} else {
					if (profile.getParty().getLeader().getUniqueId().equals(profile.getUuid())) {
						itemStacks[getSlot(HotbarItem.PARTY_EVENTS)] = getItem(HotbarItem.PARTY_EVENTS);
						if (profile.getParty().getListOfPlayers().size() > 3)
							itemStacks[getSlot(HotbarItem.CLASS_SELECT)] = getItem(HotbarItem.CLASS_SELECT);
						itemStacks[getSlot(HotbarItem.PARTY_INFORMATION)] = getItem(HotbarItem.PARTY_INFORMATION);
						itemStacks[getSlot(HotbarItem.OTHER_PARTIES)] = getItem(HotbarItem.OTHER_PARTIES);
						itemStacks[getSlot(HotbarItem.PARTY_DISBAND)] = getItem(HotbarItem.PARTY_DISBAND);
					} else {
						itemStacks[getSlot(HotbarItem.PARTY_INFORMATION)] = getItem(HotbarItem.PARTY_INFORMATION);
						itemStacks[getSlot(HotbarItem.OTHER_PARTIES)] = getItem(HotbarItem.OTHER_PARTIES);
						itemStacks[getSlot(HotbarItem.PARTY_LEAVE)] = getItem(HotbarItem.PARTY_LEAVE);
					}
				}
			}
			break;
			case QUEUEING: {
				itemStacks[getSlot(HotbarItem.QUEUE_LEAVE)] = getItem(HotbarItem.QUEUE_LEAVE);
			}
			break;
			case TOURNAMENT: {
				itemStacks[getSlot(HotbarItem.VIEWTOURNAMENT)] = getItem(HotbarItem.VIEWTOURNAMENT);
				itemStacks[getSlot(HotbarItem.LEAVETOURNAMENT)] = getItem(HotbarItem.LEAVETOURNAMENT);
			}
			break;
			case SPECTATING:
			case FIGHTING: {
				itemStacks[getSlot(HotbarItem.SPECTATE_STOP)] = getItem(HotbarItem.SPECTATE_STOP);
			}
			break;
			case EVENT: {
				itemStacks[getSlot(HotbarItem.EVENT_LEAVE)] = getItem(HotbarItem.EVENT_LEAVE);
			}
			break;
			case STAFF_MODE: {
				itemStacks[getSlot(HotbarItem.RANDOM_TELEPORT)] = getItem(HotbarItem.RANDOM_TELEPORT);
				itemStacks[getSlot(HotbarItem.HIDE_ALL_PLAYERS)] = getItem(HotbarItem.HIDE_ALL_PLAYERS);
				itemStacks[getSlot(HotbarItem.RESET)] = getItem(HotbarItem.RESET);
				itemStacks[getSlot(HotbarItem.ONLINE_STAFF)] = getItem(HotbarItem.ONLINE_STAFF);
				itemStacks[getSlot(HotbarItem.VIEW_INVENTORYSTAFF)] = getItem(HotbarItem.VIEW_INVENTORYSTAFF);
				itemStacks[getSlot(HotbarItem.COMPASS)] = getItem(HotbarItem.COMPASS);
				itemStacks[getSlot(HotbarItem.FREEZE)] = getItem(HotbarItem.FREEZE);
			}
			break;
		}

		PlayerUtil.reset(player);

		for (int i = 0; i < 9; i++) {
			player.getInventory().setItem(i, itemStacks[i]);
		}

		/*if(profile.getState() == ProfileState.LOBBY) {
			if(profile.getClan() != null){
				Category category = profile.getClan().getCategory();
				player.getInventory().setArmorContents(category.getArmor());
			}
		}*/

		player.updateInventory();
	}

	public static HotbarItem fromItemStack(ItemStack itemStack) {
		for (Map.Entry<HotbarItem, HotbarEntry> entry : Hotbar.getItems().entrySet()) {
			if (entry.getValue() != null && entry.getValue().getItemStack().equals(itemStack)) {
				return entry.getKey();
			}
		}

		return null;
	}

	public static ItemStack getItem(HotbarItem hotbarItem) {
		return items.get(hotbarItem).getItemStack();
	}

	public static int getSlot(HotbarItem hotbarItem) {
		return items.get(hotbarItem).getSlot();
	}

}
