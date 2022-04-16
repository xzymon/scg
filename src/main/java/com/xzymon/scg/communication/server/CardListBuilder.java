package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardListBuilder {
	private List<JSONObject> cards;

	private CardListBuilder() {
		this.cards = new ArrayList<>();
	}

	public static CardListBuilder newInstance() {
		return new CardListBuilder();
	}

	public CardListBuilder add(CardBuilder card) {
		if (null != card) {
			JSONObject json = card.build();
			if (null != json) {
				cards.add(json);
			}
		}
		return this;
	}

	public JSONArray build() {
		if (!cards.isEmpty()) {
			JSONArray result = new JSONArray();
			for (JSONObject card : cards) {
				result.put(card);
			}
			return result;
		}
		return null;
	}
}
