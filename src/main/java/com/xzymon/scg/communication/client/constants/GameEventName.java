package com.xzymon.scg.communication.client.constants;

import com.google.gson.annotations.SerializedName;

public enum GameEventName {
	@SerializedName("pullNextCard")
	PULL_NEXT_CARD("pullNextCard"),
	@SerializedName("playCard")
	PLAY_CARD("playCard")
	;

	private String value;

	public String getValue() {
		return value;
	}

	GameEventName(String value) {
		this.value = value;
	}
}
