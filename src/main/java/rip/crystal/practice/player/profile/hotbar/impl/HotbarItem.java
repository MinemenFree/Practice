package rip.crystal.practice.player.profile.hotbar.impl;

import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

public enum HotbarItem {

	BasicConfigurationFile config = cPractice.get().getQueueConfig();
	
	QUEUE_JOIN_RANKED(config.getString("QUEUE_JOIN_RANKED.COMMAND")),
	QUEUE_JOIN_UNRANKED(config.getString("QUEUE_JOIN_UNRANKED.COMMAND")),
	QUEUES_JOIN(config.getString("QUEUES_JOIN.COMMAND")),
	QUEUE_LEAVE(config.getString("QUEUE_LEAVE.COMMAND")),
	PARTY_EVENTS(null),
	PARTY_CREATE(config.getString("PARTY_CREATE.COMMAND")),
	PARTY_DISBAND(config.getString("PARTY_DISBAND.COMMAND")),
	PARTY_LEAVE(config.getString("PARTY_LEAVE.COMMAND")),
	PARTY_INFORMATION(config.getString("PARTY_INFORMATION.COMMAND")),
	OTHER_PARTIES(null),
	KIT_EDITOR(config.getString("KIT_EDITOR.COMMAND")),
	SPECTATE_STOP(config.getString("SPECTATE_STOP.COMMAND")),
	VIEW_INVENTORY(null),
	EVENT_JOIN(config.getString("EVENT_JOIN.COMMAND")),
	EVENT_LEAVE(config.getString("EVENT_LEAVE.COMMAND")),
	MAP_SELECTION(null),

	FOLLOW(null),

	REMATCH_REQUEST(config.getString("REMATCH_REQUEST.COMMAND")),
	REMATCH_ACCEPT(config.getString("REMATCH_ACCEPT.COMMAND")),

	CLASS_SELECT(null),
	KIT_SELECTION(null),
	EVENT_SELECT(config.getString("EVENT_SELECT.COMMAND")),
	LEADERBOARD_MENU(null),

	RANDOM_TELEPORT(null),
	HIDE_ALL_PLAYERS(null),
	VIEW_INVENTORYSTAFF(null),
	SETTINGS(config.getString("SETTINGS.COMMAND")),
	//FFA_JOIN("ffa join"),
	RESET(null),
	ONLINE_STAFF(config.getString("ONLINE_STAFF.COMMAND")),
	COMPASS(config.getString("COMPASS.COMMAND")),
	FREEZE(null),

	VIEWTOURNAMENT(config.getString("VIEWTOURNAMENT.COMMAND")),
	LEAVETOURNAMENT(config.getString("LEAVETOURNAMENT.COMMAND"));

	@Getter private final String command;
	@Getter @Setter private Pattern pattern;

	HotbarItem(String command) {
		this.command = command;
	}

}
