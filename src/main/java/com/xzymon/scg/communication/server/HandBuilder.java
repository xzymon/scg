package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

public class HandBuilder {
	public static final String KEY_REMOVED_CARDS = "removedCards";
	public static final String KEY_MAINTAINED_CARDS = "maintainedCards";
	public static final String KEY_NEW_CARDS = "newCards";

	private CardListBuilder removedCards;
	private CardListBuilder maintainedCards;
	private CardListBuilder newCards;

	private HandBuilder() {
		this.removedCards = CardListBuilder.newInstance();
		this.maintainedCards = CardListBuilder.newInstance();
		this.newCards = CardListBuilder.newInstance();
	}

	public static HandBuilder newInstance() {
		return new HandBuilder();
	}

	public HandBuilder removedCards(CardListBuilder cardListBuilder) {
		this.removedCards = cardListBuilder;
		return this;
	}

	public HandBuilder maintainedCards(CardListBuilder cardListBuilder) {
		this.maintainedCards = cardListBuilder;
		return this;
	}

	public HandBuilder newCards(CardListBuilder cardListBuilder) {
		this.newCards = cardListBuilder;
		return this;
	}

	public HandBuilder addRemovedCard(CardBuilder cardBuilder) {
		this.removedCards.add(cardBuilder);
		return this;
	}

	public HandBuilder addMaintainedCard(CardBuilder cardBuilder) {
		this.maintainedCards.add(cardBuilder);
		return this;
	}

	public HandBuilder addNewCard(CardBuilder cardBuilder) {
		this.newCards.add(cardBuilder);
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (null != removedCards) {
			JSONArray array = removedCards.build();
			if (array != null) {
				builder.put(KEY_REMOVED_CARDS, array);
				notNull = true;
			}
		}
		if (null != maintainedCards) {
			JSONArray array = maintainedCards.build();
			if (array != null) {
				builder.put(KEY_MAINTAINED_CARDS, array);
				notNull = true;
			}
		}
		if (null != newCards) {
			JSONArray array = newCards.build();
			if (array != null) {
				builder.put(KEY_NEW_CARDS, array);
				notNull = true;
			}
		}
		if (notNull) {
			return builder;
		}
		return null;
	}

}
