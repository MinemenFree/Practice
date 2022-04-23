package rip.crystal.practice.player.party.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PartyEvent {

	FFA("FFA"),
	SPLIT("Split"),
	HCFClass("HCFClass");

	private final String name;

}
