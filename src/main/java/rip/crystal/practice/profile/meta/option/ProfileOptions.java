package rip.crystal.practice.profile.meta.option;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ProfileOptions {

	@Getter @Setter private boolean showScoreboard = true;
	@Getter @Setter private boolean receiveDuelRequests = true;
	@Getter @Setter private boolean allowSpectators = true;
	@Getter @Setter private boolean publicChatEnabled = true;
	@Getter @Setter private boolean receivingNewConversations = true;
	@Getter @Setter private boolean playingMessageSounds = true;
	@Getter @Setter private boolean isUsingPingFactor = false;

}
