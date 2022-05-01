package rip.crystal.practice.player.profile.conversation;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;

import java.util.UUID;

@Getter
public class Conversation {

	private final UUID initiatedBy;
	private final UUID target;
	private long lastMessageSentAt;
	private UUID lastMessageSentBy;

	public Conversation(UUID initiatedBy, UUID target) {
		this.initiatedBy = initiatedBy;
		this.target = target;
		this.lastMessageSentAt = System.currentTimeMillis();

		Profile initiatorProfile = Profile.get(initiatedBy);
		initiatorProfile.getConversations().getConversations().put(target, this);

		Profile targetProfile = Profile.get(target);
		targetProfile.getConversations().getConversations().put(initiatedBy, this);
	}

	public void sendMessage(Player sender, Player target, String message) {
		Profile senderProfile = Profile.get(sender.getUniqueId());
		Profile targetProfile = Profile.get(target.getUniqueId());

		new MessageFormat(Locale.CONVERSATION_SEND_MESSAGE.format(senderProfile.getLocale()))
			.add("{sender_name}", sender.getName())
			.add("{sender_displayname}", sender.getDisplayName())
			.add("{sender_color}", senderProfile.getColor())
			.add("{sender_prefix}", cPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
			.add("{sender_suffix}", cPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
			.add("{target_name}", target.getName())
			.add("{target_displayname}", target.getDisplayName())
			.add("{target_color}", targetProfile.getColor())
			.add("{target_prefix}", cPractice.get().getRankManager().getRank().getPrefix(target.getUniqueId()))
			.add("{target_suffix}", cPractice.get().getRankManager().getRank().getSuffix(target.getUniqueId()))
			.add("{msg}", message)
			.send(sender);

		if (targetProfile.getOptions().playingMessageSounds()) {
			target.playSound(target.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
		}

		new MessageFormat(Locale.CONVERSATION_RECEIVE_MESSAGE.format(targetProfile.getLocale()))
				.add("{sender_name}", sender.getName())
				.add("{sender_displayname}", sender.getDisplayName())
				.add("{sender_color}", senderProfile.getColor())
				.add("{sender_prefix}", cPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
				.add("{sender_suffix}", cPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
				.add("{target_name}", target.getName())
				.add("{target_displayname}", target.getDisplayName())
				.add("{target_color}",  targetProfile.getColor())
				.add("{target_prefix}", cPractice.get().getRankManager().getRank().getPrefix(target.getUniqueId()))
				.add("{target_suffix}", cPractice.get().getRankManager().getRank().getSuffix(target.getUniqueId()))
				.add("{msg}",  message)
			.send(target);

		lastMessageSentAt = System.currentTimeMillis();
		lastMessageSentBy = sender.getUniqueId();
	}

	public boolean validate() {
		Player initiator = Bukkit.getPlayer(initiatedBy);

		if (initiator == null || !initiator.isOnline()) {
			destroy();
			return false;
		}

		Player target = Bukkit.getPlayer(this.target);

		if (target == null || !target.isOnline()) {
			destroy();
			return false;
		}

		return true;
	}

	public void destroy() {
		for (Player player : new Player[] { Bukkit.getPlayer(initiatedBy), Bukkit.getPlayer(target) }) {
			if (player != null && player.isOnline()) {
				Profile profile = Profile.get(player.getUniqueId());
				profile.getConversations().getConversations().remove(getPartner(player.getUniqueId()));
			}
		}
	}

	public UUID getPartner(UUID compareWith) {
		if (initiatedBy.equals(compareWith)) {
			return target;
		} else if (target.equals(compareWith)) {
			return initiatedBy;
		}

		return null;
	}

}
