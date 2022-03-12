package rip.crystal.practice.player.profile.meta.option.button;

import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.meta.option.menu.ProfileOptionButton;
import rip.crystal.practice.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PublicChatOptionButton extends ProfileOptionButton {

	@Override
	public String getOptionName() {
		return "&a&lPublic Chat";
	}

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.BOOK_AND_QUILL).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.BOOK_AND_QUILL).build();
	}

	@Override
	public String getDescription() {
		return "If enabled, you will receive public chat messages.";
	}

	@Override
	public String getEnabledOption() {
		return "Receive public chat messages";
	}

	@Override
	public String getDisabledOption() {
		return "Do not receive public chat messages";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.get(player.getUniqueId()).getOptions().publicChatEnabled();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Profile profile = Profile.get(player.getUniqueId());
		profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());
	}

}
