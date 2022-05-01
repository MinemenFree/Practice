package rip.crystal.practice.essentials.chat.impl.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import rip.crystal.practice.essentials.chat.impl.ChatAttempt;
import rip.crystal.practice.utilities.event.BaseEvent;

@Getter
public class ChatAttemptEvent extends BaseEvent implements Cancellable {

	private final Player player;
	private final ChatAttempt chatAttempt;
	@Setter private String chatMessage;
	@Setter private boolean cancelled;
	@Setter private String cancelReason = "";

	public ChatAttemptEvent(Player player, ChatAttempt chatAttempt, String chatMessage) {
		this.player = player;
		this.chatAttempt = chatAttempt;
		this.chatMessage = chatMessage;
	}

}
