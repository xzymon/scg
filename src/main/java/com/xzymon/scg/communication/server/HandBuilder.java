package com.xzymon.scg.communication.server;

import com.xzymon.scg.domain.Card;
import org.json.JSONObject;

import java.util.List;

public class HandBuilder {
	public static final String KEY_NEW_CARDS = "newCards";

	private List<Card> newCards;

	private HandBuilder() {
	}

	public static HandBuilder newInstance() {
		return new HandBuilder();
	}

	public HandBuilder newCards(List<Card> newCards) {
		this.newCards = newCards;
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (newCards != null) {
			builder.put(KEY_NEW_CARDS, newCards);
			notNull = true;
		}
		if (notNull) {
			return builder;
		}
		return null;
	}

}
