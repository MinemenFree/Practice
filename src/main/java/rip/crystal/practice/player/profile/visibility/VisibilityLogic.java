package rip.crystal.practice.player.profile.visibility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.game.event.game.EventGame;
import rip.crystal.practice.game.event.impl.tnttag.TNTTagGameLogic;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.nametags.GxNameTag;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.TaskUtil;

public class VisibilityLogic {

	public static void handle(Player viewer) {
		for (Player target : Bukkit.getOnlinePlayers()) handle(viewer, target);
	}

	public static void handle(Player viewer, Player target) {
		if (viewer == null || target == null) return;

		Profile viewerProfile = Profile.get(viewer.getUniqueId());
		Profile targetProfile = Profile.get(target.getUniqueId());

		if (viewerProfile.getState() == ProfileState.FFA) {
			viewer.showPlayer(target);
			target.showPlayer(viewer);

			if (targetProfile.getState() == ProfileState.STAFF_MODE) {
				viewer.hidePlayer(target);
				return;
			}
			if (targetProfile.getState() == ProfileState.QUEUEING) {
				viewer.hidePlayer(target);
				return;
			}
			if (targetProfile.getState() == ProfileState.LOBBY) {
				viewer.hidePlayer(target);
				//return;
			}
		}
		else if (viewerProfile.getState() == ProfileState.LOBBY || viewerProfile.getState() == ProfileState.QUEUEING) {
			if (viewer.equals(target)) {
				TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(target, viewer));
				return;
			}

			if (targetProfile.getState() == ProfileState.FFA) {
				viewer.hidePlayer(target);
				return;
			}

			if (targetProfile.getState() == ProfileState.STAFF_MODE) {
				viewer.hidePlayer(target);
				return;
			}

			if (viewerProfile.getParty() != null && viewerProfile.getParty().containsPlayer(target.getUniqueId())) {
				viewer.showPlayer(target);
			} else {
				if(!target.hasPermission("cpractice.practice.see")) viewer.hidePlayer(target);
				else viewer.showPlayer(target);
			}
			TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(target, viewer));
		}
		else if (viewerProfile.getState() == ProfileState.FIGHTING) {
			if (viewer.equals(target)) {
				TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(target, viewer));
				return;
			}

			if (targetProfile.getState() == ProfileState.STAFF_MODE) {
				viewer.hidePlayer(target);
				return;
			}

			MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);

			if (targetGamePlayer != null) {
				if (!targetGamePlayer.isDead()) viewer.showPlayer(target);
				else viewer.hidePlayer(target);
			} else {
				viewer.hidePlayer(target);
			}
			TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(target, viewer));
		}
		else if (viewerProfile.getState() == ProfileState.EVENT) {
			if (targetProfile.getState() == ProfileState.STAFF_MODE) {
				viewer.hidePlayer(target);
				return;
			}

			EventGame game = EventGame.getActiveGame();
			if (targetProfile.getState() == ProfileState.EVENT && game.getGameLogic().isPlaying(target)) viewer.showPlayer(target);
			else if (targetProfile.getState() == ProfileState.EVENT && !game.getGameLogic().isPlaying(target))
				viewer.showPlayer(target);
			else if (targetProfile.getState() == ProfileState.EVENT && game.getGameLogic() instanceof TNTTagGameLogic &&
					!game.getGameLogic().isPlaying(target)) viewer.hidePlayer(target);
			else viewer.hidePlayer(target);
			TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(target, viewer));
		}
		else if (viewerProfile.getState() == ProfileState.SPECTATING) {
			if (targetProfile.getState() == ProfileState.STAFF_MODE) {
				viewer.hidePlayer(target);
				return;
			}

			MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);

			if (targetGamePlayer != null) {
				if (!targetGamePlayer.isDead() && !targetGamePlayer.isDisconnected()) viewer.showPlayer(target);
				else viewer.hidePlayer(target);
			} else viewer.hidePlayer(target);
			TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(target, viewer));
		}
		else if (viewerProfile.getState() == ProfileState.STAFF_MODE) {
			if (targetProfile.getState() == ProfileState.STAFF_MODE) {
				viewer.showPlayer(target);
				return;
			}
			if (viewerProfile.getMatch() == null) {
				if(!target.hasPermission("cpractice.practice.see")) viewer.hidePlayer(target);
				else viewer.showPlayer(target);
				TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(target, viewer));
			} else {
				MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
				if (targetGamePlayer != null) {
					if (!targetGamePlayer.isDead() && !targetGamePlayer.isDisconnected()) viewer.showPlayer(target);
					else viewer.hidePlayer(target);
				} else viewer.hidePlayer(target);
			}
			TaskUtil.runAsync(() -> GxNameTag.reloadPlayer(target, viewer));
		}
	}
}