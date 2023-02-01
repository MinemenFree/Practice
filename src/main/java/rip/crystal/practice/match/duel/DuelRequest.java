package rip.crystal.practice.match.duel;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.MessageFormat;

import java.util.UUID;

public class DuelRequest {

	@Getter private final UUID sender;
	@Getter private final UUID target;
	@Getter private final boolean party;
	@Getter @Setter private Kit kit;
	@Getter @Setter private Arena arena;
	private final long timeStamp = System.currentTimeMillis();
	@Getter @Setter private int rounds;

	DuelRequest(UUID sender, UUID target, boolean party) {
		this.sender = sender;
		this.target = target;
		this.party = party;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() - this.timeStamp >= 30_000;
	}

	public void expire() {
		Player sender = Bukkit.getPlayer(this.sender);
		Player target = Bukkit.getPlayer(this.target);

		if (sender != null && target != null) {
			new MessageFormat(Locale.DUEL_SENDER_EXPIRED.format(Profile.get(sender.getUniqueId()).getLocale()))
				        .add("<arena_name>", arena.getName())
					.add("<kit_name>", kit.getName())
				        .add("<arena_author>", arena.getAuthor())
					.add("<target_name>", target.getName())
				        .add("<target_ping>", Integer.toString(PlayerUtil.getPing(target))
					/*.add("<sender_name>", sender.getName())
					.add("<sender_ping>", Integer.toString(PlayerUtil.getPing(sender))*/
					.send(sender);

			new MessageFormat(Locale.DUEL_TARGET_EXPIRED.format(Profile.get(target.getUniqueId()).getLocale()))
				        .add("<arena_name>", arena.getName())
					.add("<kit_name>", kit.getName())
				        .add("<arena_author>", arena.getAuthor())
					.add("<sender_name>", sender.getName())
					.add("<sender_ping>", Integer.toString(PlayerUtil.getPing(sender))
					/*.add("<target_name>", target.getName())
					.add("<target_ping>", Integer.toString(PlayerUtil.getPing(target))*/
					.send(target);
		}
	}

}
