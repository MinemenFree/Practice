package rip.crystal.practice.player.profile.meta.option;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import rip.crystal.practice.player.cosmetics.impl.killeffects.KillEffectType;

@Accessors(fluent = true)
public class ProfileOptions {

	//@Getter @Setter private boolean showScoreboard = true;
	//@Getter @Setter private boolean receiveDuelRequests = true;
	/*@Getter @Setter private boolean allowSpectators = true;
	@Getter @Setter private boolean publicChatEnabled = true;
	@Getter @Setter private boolean receivingNewConversations = true;
	@Getter @Setter private boolean playingMessageSounds = true;*/
	//@Getter @Setter private boolean isUsingPingFactor = false;

	@Getter @Setter private boolean receiveDuelRequests = true;
	@Getter @Setter private boolean vanillaTab = false;
	@Getter @Setter private boolean showScoreboard = true;
	@Getter @Setter private boolean publicChatEnabled = true;
	@Getter @Setter private boolean allowSpectators = true;
	@Getter @Setter private boolean receivingNewConversations = false;
	@Getter @Setter private boolean playingMessageSounds = false;

	private KillEffectType specialEffect = KillEffectType.NONE;

	public KillEffectType getSpecialEffect() {
		return this.specialEffect;
	}


	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof ProfileOptions)) {
			return false;
		}
		ProfileOptions playerSettings = (ProfileOptions)object;
		if (!playerSettings.canEqual(this)) {
			return false;
		}
		if (this.receiveDuelRequests() != playerSettings.receiveDuelRequests()) {
			return false;
		}
		if (this.vanillaTab() != playerSettings.vanillaTab()) {
			return false;
		}
		if (this.allowSpectators() != playerSettings.allowSpectators()) {
			return false;
		}
		if (this.showScoreboard() != playerSettings.showScoreboard()) {
			return false;
		}
		if (this.publicChatEnabled() != playerSettings.publicChatEnabled()) {
			return false;
		}
		if (this.receivingNewConversations() != playerSettings.receivingNewConversations()) {
			return false;
		}
		if (this.playingMessageSounds() != playerSettings.playingMessageSounds()) {
			return false;
		}

		KillEffectType specialEffects = this.getSpecialEffect();
		KillEffectType specialEffects2 = playerSettings.getSpecialEffect();
		if (specialEffects == null ? specialEffects2 != null : !((Object)(specialEffects)).equals(specialEffects2)) {
			return false;
		}
		return true;
	}

	protected boolean canEqual(Object object) {
		return object instanceof ProfileOptions;
	}

	public int hashCode() {
		int n = 59;
		int n2 = 1;
		n2 = n2 * 59 + (this.receiveDuelRequests() ? 79 : 97);
		n2 = n2 * 59 + (this.vanillaTab() ? 79 : 97);
		n2 = n2 * 59 + (this.allowSpectators() ? 79 : 97);
		n2 = n2 * 59 + (this.showScoreboard() ? 79 : 97);
		n2 = n2 * 59 + (this.publicChatEnabled() ? 79 : 97);
		n2 = n2 * 59 + (this.receivingNewConversations() ? 79 : 97);
		n2 = n2 * 59 + (this.playingMessageSounds() ? 79 : 97);
		KillEffectType specialEffects = this.getSpecialEffect();
		n2 = n2 * 59 + (specialEffects == null ? 43 : ((Object)(specialEffects)).hashCode());
		return n2;
	}

	public String toString() {
		return "ProfileSettings(duelRequests=" + this.receiveDuelRequests() + ", vanillaTab=" + this.vanillaTab() + ", spectatorsAllowed=" + this.allowSpectators() + ", scoreboardToggled=" + this.showScoreboard() + ", publicChat=" + this.publicChatEnabled() + ", receivingnewconversations=" + this.receivingNewConversations() + ", playingmessagesounds=" + this.playingMessageSounds() + ", specialEffect=" + (this.getSpecialEffect()) + ")";
	}
}
