package rip.crystal.practice.game.kit.meta;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KitEditRules {

	@Getter @Setter private boolean allowPotionFill;
	@Getter private final List<ItemStack> editorItems = Lists.newArrayList();

}
