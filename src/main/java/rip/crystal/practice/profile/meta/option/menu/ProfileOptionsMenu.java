package rip.crystal.practice.profile.meta.option.menu;

import rip.crystal.practice.profile.meta.option.button.*;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ProfileOptionsMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&c&lOptions";
	}

	{
		setPlaceholder(true);
	}

	@Override
	public int getSize() {
		return 9;
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		buttons.put(0, new PublicChatOptionButton());
		buttons.put(1, new PrivateChatOptionButton());
		buttons.put(2, new ChangeTabTypeOptionButton());
		buttons.put(3, new PrivateChatSoundsOptionButton());
		buttons.put(4, new ShowScoreboardOptionButton());
		buttons.put(5, new AllowSpectatorsOptionButton());
		buttons.put(6, new DuelRequestsOptionButton());
		buttons.put(7, new CosmeticsOptionButton());
		//buttons.put(39, new MatchMakingOptionButton());
		return buttons;
	}

}
