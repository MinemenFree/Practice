package rip.crystal.practice.player.profile.hotbar.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

public enum HotbarItem {

	QUEUE_JOIN_RANKED("cpractice:ranked"),
	QUEUE_JOIN_UNRANKED("cpractice:unranked"),
	QUEUES_JOIN("cpractice:queues"),
	QUEUE_LEAVE("cpractice:queue leave"),
	PARTY_EVENTS(null),
	PARTY_CREATE("cpractice:party create"),
	PARTY_DISBAND("cpractice:party disband"),
	PARTY_LEAVE("cpractice:party leave"),
	PARTY_INFORMATION("cpractice:party info"),
	OTHER_PARTIES(null),
	KIT_EDITOR("cpractice:kiteditor"),
	SPECTATE_STOP("cpractice:stopspectating"),
	VIEW_INVENTORY(null),
	EVENT_JOIN("cpractice:event join"),
	EVENT_LEAVE("cpractice:event leave"),
	MAP_SELECTION(null),

	FOLLOW(null),

	REMATCH_REQUEST("cpractice:rematch"),
	REMATCH_ACCEPT("cpractice:rematch"),

	CLASS_SELECT(null),
	KIT_SELECTION(null),
	EVENT_SELECT("cpractice:host"),
	LEADERBOARD_MENU(null),

	RANDOM_TELEPORT(null),
	HIDE_ALL_PLAYERS(null),
	VIEW_INVENTORYSTAFF(null),
	SETTINGS("cpractice:options"),
	//FFA_JOIN("ffa join"),
	RESET(null),
	ONLINE_STAFF("stafflist"),
	COMPASS("/thru"),
	FREEZE(null),

	VIEWTOURNAMENT("cpractice:tournament status"),
	LEAVETOURNAMENT("cpractice:tournament leave");

	@Getter private final String command;
	@Getter @Setter private Pattern pattern;

	HotbarItem(String command) {
		this.command = command;
	}

}
