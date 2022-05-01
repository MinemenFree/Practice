package rip.crystal.practice.player.party.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.player.party.enums.PartyEvent;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class PartyEventSelectEventMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&9Select an event";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		buttons.put(3, new SelectEventButton(PartyEvent.FFA));
		buttons.put(5, new SelectEventButton(PartyEvent.SPLIT));
		//buttons.put(7, new SelectEventButton(PartyEvent.HCFClass));
		return buttons;
	}

	@AllArgsConstructor
	private class SelectEventButton extends Button {

		private final PartyEvent partyEvent;

		@Override
		public ItemStack getButtonItem(Player player) {
			if(partyEvent == PartyEvent.FFA) {
				return new ItemBuilder(Material.QUARTZ)
					.name("&c"+ partyEvent.getName())
					.lore(CC.SB_BAR)
					.lore("&7&oA fight all against all")
					.lore("&7&oAnd the last one to stay alive wins")
					.lore(CC.SB_BAR)
					.build();
			}

			/*if(partyEvent == PartyEvent.HCFClass) {
				return new ItemBuilder(Material.GOLD_CHESTPLATE)
						.name("&c"+ partyEvent.getName())
						.build();
			}*/

			return new ItemBuilder(Material.REDSTONE)
					.name("&c" + partyEvent.getName())
					.lore(CC.SB_BAR)
					.lore("&7&oThe party is divided into two")
					.lore("&7&oTeams and they fight")
					.lore(CC.SB_BAR)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getParty() == null) {
				player.sendMessage(CC.RED + "You are not in a party.");
				return;
			}

			for (Player member : profile.getParty().getListOfPlayers()) {
				Profile profileMember = Profile.get(member.getUniqueId());
				if (profileMember.getState() != ProfileState.LOBBY) {
					player.sendMessage(CC.translate("&cAll player of the party have to be in spawn for you to start another event"));
					player.closeInventory();
					return;
				}
			}

			/*if(partyEvent == PartyEvent.HCFClass) {
				new HCFClassMenu().openMenu(player);
			}
			new HCFClassMenu().openMenu(player);*/
			new PartyEventSelectKitMenu(partyEvent).openMenu(player);
		}
	}
}
