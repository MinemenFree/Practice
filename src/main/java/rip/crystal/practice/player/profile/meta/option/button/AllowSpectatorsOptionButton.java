package rip.crystal.practice.player.profile.meta.option.button;

import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.meta.option.menu.ProfileOptionButton;
import rip.crystal.practice.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class AllowSpectatorsOptionButton extends ProfileOptionButton {

	@Override
	public ItemStack getEnabledItem(Player player) {
		return new ItemBuilder(Material.REDSTONE_TORCH_ON).build();
	}

	@Override
	public ItemStack getDisabledItem(Player player) {
		return new ItemBuilder(Material.REDSTONE_TORCH_ON).build();
	}

	@Override
	public String getOptionName() {
		return "&d&lSpectators";
	}

	@Override
	public String getDescription() {
		return "If enabled, players will be able to spectate your match.";
	}

	@Override
	public String getEnabledOption() {
		return "Allow players to spectate";
	}

	@Override
	public String getDisabledOption() {
		return "Do not allow players to spectate";
	}

	@Override
	public boolean isEnabled(Player player) {
		return Profile.get(player.getUniqueId()).getOptions().allowSpectators();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Profile profile = Profile.get(player.getUniqueId());
		profile.getOptions().allowSpectators(!profile.getOptions().allowSpectators());
	}

}
