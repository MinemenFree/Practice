package rip.crystal.practice.player.profile.hotbar.impl;

import rip.crystal.practice.cPractice;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

public enum HotbarItem {
	
	QUEUE_JOIN_RANKED(cPractice.get().getHotbarConfig().getString("QUEUE_JOIN_RANKED.COMMAND")),
	QUEUE_JOIN_UNRANKED(cPractice.get().getHotbarConfig().getString("QUEUE_JOIN_UNRANKED.COMMAND")),
	QUEUES_JOIN(cPractice.get().getHotbarConfig().getString("QUEUES_JOIN.COMMAND")),
	QUEUE_LEAVE(cPractice.get().getHotbarConfig().getString("QUEUE_LEAVE.COMMAND")),
	PARTY_EVENTS(null),
	PARTY_CREATE(cPractice.get().getHotbarConfig().getString("PARTY_CREATE.COMMAND")),
	PARTY_DISBAND(cPractice.get().getHotbarConfig().getString("PARTY_DISBAND.COMMAND")),
	PARTY_LEAVE(cPractice.get().getHotbarConfig().getString("PARTY_LEAVE.COMMAND")),
	PARTY_INFORMATION(cPractice.get().getHotbarConfig().getString("PARTY_INFORMATION.COMMAND")),
	OTHER_PARTIES(null),
	KIT_EDITOR(cPractice.get().getHotbarConfig().getString("KIT_EDITOR.COMMAND")),
	SPECTATE_STOP(cPractice.get().getHotbarConfig().getString("SPECTATE_STOP.COMMAND")),
	VIEW_INVENTORY(null),
	EVENT_JOIN(cPractice.get().getHotbarConfig().getString("EVENT_JOIN.COMMAND")),
	EVENT_LEAVE(cPractice.get().getHotbarConfig().getString("EVENT_LEAVE.COMMAND")),
	MAP_SELECTION(null),

	FOLLOW(null),

	REMATCH_REQUEST(cPractice.get().getHotbarConfig().getString("REMATCH_REQUEST.COMMAND")),
	REMATCH_ACCEPT(cPractice.get().getHotbarConfig().getString("REMATCH_ACCEPT.COMMAND")),

	CLASS_SELECT(null),
	KIT_SELECTION(null),
	EVENT_SELECT(cPractice.get().getHotbarConfig().getString("EVENT_SELECT.COMMAND")),
	LEADERBOARD_MENU(null),

	RANDOM_TELEPORT(null),
	HIDE_ALL_PLAYERS(null),
	VIEW_INVENTORYSTAFF(null),
	SETTINGS(cPractice.get().getHotbarConfig().getString("SETTINGS.COMMAND")),
	//FFA_JOIN("ffa join"),
	RESET(null),
	ONLINE_STAFF(cPractice.get().getHotbarConfig().getString("ONLINE_STAFF.COMMAND")),
	COMPASS(cPractice.get().getHotbarConfig().getString("COMPASS.COMMAND")),
	FREEZE(null),

	VIEWTOURNAMENT(cPractice.get().getHotbarConfig().getString("VIEWTOURNAMENT.COMMAND")),
	LEAVETOURNAMENT(cPractice.get().getHotbarConfig().getString("LEAVETOURNAMENT.COMMAND"));

	@Getter private final String command;
	@Getter @Setter private Pattern pattern;

	HotbarItem(String command) {
		this.command = command;
	}

}
