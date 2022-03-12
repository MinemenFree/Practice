package rip.crystal.practice.game.kit.meta;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.potion.PotionEffect;

import java.util.List;

@Getter @Setter
public class KitGameRules {

	private boolean build, spleef, sumo, parkour, healthRegeneration, ranked, showHealth, hcf, bridge, boxing, nofalldamage, soup, antiFood, bedFight, hcftrap;
	private int hitDelay = 20;
	private String kbProfile = "default";
	private List<PotionEffect> effects = Lists.newArrayList();

}
