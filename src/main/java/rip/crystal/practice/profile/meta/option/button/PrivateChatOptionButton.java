package rip.crystal.practice.profile.meta.option.button;

import rip.crystal.practice.profile.Profile;
import rip.crystal.practice.profile.meta.option.menu.ProfileOptionButton;
import rip.crystal.practice.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PrivateChatOptionButton extends ProfileOptionButton {

	@Override
	public String getOptionName() {
		return "&c&lPrivate Chat";
	}

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.NAME_TAG).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.NAME_TAG).build();
	}

	@Override
	public String getDescription() {
		return "If enabled, you will receive private chat messages.";
	}

	@Override
	public String getEnabledOption() {
		return "Receive private chat messages";
	}

	@Override
	public String getDisabledOption() {
		return "Do not receive private chat messages";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.get(player.getUniqueId()).getOptions().receivingNewConversations();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Profile profile = Profile.get(player.getUniqueId());
		profile.getOptions().receivingNewConversations(!profile.getOptions().receivingNewConversations());
	}

}
