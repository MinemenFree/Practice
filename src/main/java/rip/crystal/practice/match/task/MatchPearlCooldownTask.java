package rip.crystal.practice.match.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.MessageFormat;

public class MatchPearlCooldownTask extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : cPractice.get().getServer().getOnlinePlayers()) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.EVENT || profile.getState() == ProfileState.FFA) {
				if (profile.getEnderpearlCooldown().hasExpired()) {
					if (!profile.getEnderpearlCooldown().isNotified()) {
						profile.getEnderpearlCooldown().setNotified(true);
						new MessageFormat(Locale.MATCH_ENDERPEARL_COOLDOWN_EXPIRED
								.format(profile.getLocale()))
								.send(player);
					}
				} else {
					int seconds = Math.round(profile.getEnderpearlCooldown().getRemaining()) / 1_000;

					player.setLevel(seconds);
					player.setExp(profile.getEnderpearlCooldown().getRemaining() / 16_000.0F);
				}
			} else {
				if (player.getLevel() > 0) {
					player.setLevel(0);
				}

				if (player.getExp() > 0.0F) {
					player.setExp(0.0F);
				}
			}
		}
	}

}
