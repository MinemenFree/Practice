package rip.crystal.practice.game.event.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventGameState {

	WAITING_FOR_PLAYERS("Waiting For Players"),
	STARTING_EVENT("Starting Event"),
	STARTING_ROUND("Starting Round"),
	PLAYING_ROUND("Playing Round"),
	ENDING_ROUND("Ending Round"),
	ENDING_EVENT("Ending Event");

	private String readable;

}
