package rip.crystal.practice.essentials.chat.impl;

import rip.crystal.practice.essentials.chat.impl.filter.ChatFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatAttempt {

	private Response response;
	private ChatFilter filterFlagged;
	private Object value;

	public ChatAttempt(Response response) {
		this.response = response;
	}

	public ChatAttempt(Response response, ChatFilter filterFlagged) {
		this.response = response;
		this.filterFlagged = filterFlagged;
	}

	public enum Response {
		ALLOWED,
		MESSAGE_FILTERED,
		CHAT_MUTED,
		CHAT_DELAYED
	}

}
