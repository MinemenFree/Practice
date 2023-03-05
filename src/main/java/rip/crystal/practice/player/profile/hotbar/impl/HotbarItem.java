package rip.crystal.practice.player.profile.hotbar.impl;

import rip.crystal.practice.Practice;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

public enum HotbarItem {
	
	QUEUE_JOIN_RANKED(cPractice.get().getQueueConfig().getString("QUEUE_JOIN_RANKED.COMMAND")),
	QUEUE_JOIN_UNRANKED(cPractice.get().getQueueConfig().getString("QUEUE_JOIN_UNRANKED.COMMAND")),
	QUEUES_JOIN(cPractice.get().getQueueConfig().getString("QUEUES_JOIN.COMMAND")),
	QUEUE_LEAVE(cPractice.get().getQueueConfig().getString("QUEUE_LEAVE.COMMAND")),
	PARTY_EVENTS(null),
	PARTY_CREATE(cPractice.get().getQueueConfig().getString("PARTY_CREATE.COMMAND")),
	PARTY_DISBAND(cPractice.get().getQueueConfig().getString("PARTY_DISBAND.COMMAND")),
	PARTY_LEAVE(cPractice.get().getQueueConfig().getString("PARTY_LEAVE.COMMAND")),
	PARTY_INFORMATION(cPractice.get().getQueueConfig().getString("PARTY_INFORMATION.COMMAND")),
	OTHER_PARTIES(null),
	KIT_EDITOR(cPractice.get().getQueueConfig().getString("KIT_EDITOR.COMMAND")),
	SPECTATE_STOP(cPractice.get().getQueueConfig().getString("SPECTATE_STOP.COMMAND")),
	VIEW_INVENTORY(null),
	EVENT_JOIN(cPractice.get().getQueueConfig().getString("EVENT_JOIN.COMMAND")),
	EVENT_LEAVE(cPractice.get().getQueueConfig().getString("EVENT_LEAVE.COMMAND")),
	MAP_SELECTION(null),

	FOLLOW(null),

	REMATCH_REQUEST(cPractice.get().getQueueConfig().getString("REMATCH_REQUEST.COMMAND")),
	REMATCH_ACCEPT(cPractice.get().getQueueConfig().getString("REMATCH_ACCEPT.COMMAND")),

	CLASS_SELECT(null),
	KIT_SELECTION(null),
	EVENT_SELECT(cPractice.get().getQueueConfig().getString("EVENT_SELECT.COMMAND")),
	LEADERBOARD_MENU(null),

	RANDOM_TELEPORT(null),
	HIDE_ALL_PLAYERS(null),
	VIEW_INVENTORYSTAFF(null),
	SETTINGS(cPractice.get().getQueueConfig().getString("SETTINGS.COMMAND")),
	//FFA_JOIN("ffa join"),
	RESET(null),
	ONLINE_STAFF(cPractice.get().getQueueConfig().getString("ONLINE_STAFF.COMMAND")),
	COMPASS(cPractice.get().getQueueConfig().getString("COMPASS.COMMAND")),
	FREEZE(null),

	VIEWTOURNAMENT(cPractice.get().getQueueConfig().getString("VIEWTOURNAMENT.COMMAND")),
	LEAVETOURNAMENT(cPractice.get().getQueueConfig().getString("LEAVETOURNAMENT.COMMAND"));

	@Getter private final String command;
	@Getter @Setter private Pattern pattern;

	HotbarItem(String command) {
		this.command = command;
	}

}
