package rip.crystal.practice.player.profile.hotbar.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

public enum HotbarItem {

	QUEUE_JOIN_RANKED(null),
	QUEUE_JOIN_UNRANKED(null),
	QUEUES_JOIN(null),
	QUEUE_LEAVE(null),
	PARTY_EVENTS(null),
	PARTY_CREATE("party create"),
	PARTY_DISBAND("party disband"),
	PARTY_LEAVE("party leave"),
	PARTY_INFORMATION("party info"),
	OTHER_PARTIES(null),
	KIT_EDITOR(null),
	SPECTATE_STOP("stopspectating"),
	VIEW_INVENTORY(null),
	EVENT_JOIN("event join"),
	EVENT_LEAVE("event leave"),
	MAP_SELECTION(null),

	FOLLOW(null),

	REMATCH_REQUEST("rematch"),
	REMATCH_ACCEPT("rematch"),

	CLASS_SELECT(null),
	KIT_SELECTION(null),
	EVENT_SELECT("host"),
	LEADERBOARD_MENU(null),

	RANDOM_TELEPORT(null),
	HIDE_ALL_PLAYERS(null),
	VIEW_INVENTORYSTAFF(null),
	SETTINGS("options"),
	//FFA_JOIN("ffa join"),
	RESET(null),
	ONLINE_STAFF("stafflist"),
	COMPASS("/thru"),
	FREEZE(null),

	VIEWTOURNAMENT("tournament status"),
	LEAVETOURNAMENT("tournament leave");

	@Getter private final String command;
	@Getter @Setter private Pattern pattern;

	HotbarItem(String command) {
		this.command = command;
	}

}
