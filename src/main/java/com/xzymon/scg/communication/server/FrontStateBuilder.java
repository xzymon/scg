package com.xzymon.scg.communication.server;

import org.json.JSONObject;

public class FrontStateBuilder {
	public static final String KEY_ACTIVE_HAND = "activeHand";
	public static final String KEY_ACTIVE_PULL_CARD = "activePullCard";
	public static final String KEY_SCORE = "score";
	public static final String KEY_NAME = "name";

	private Boolean activeHand;
	private Boolean activePullCard;
	private Integer score;
	private String name;

	private FrontStateBuilder() {
	}

	public static FrontStateBuilder newInstance() {
		return new FrontStateBuilder();
	}

	public FrontStateBuilder activePullCard(Boolean active) {
		this.activePullCard = active;
		return this;
	}

	public FrontStateBuilder activeHand(Boolean active) {
		this.activeHand = active;
		return this;
	}

	public FrontStateBuilder score(Integer score) {
		this.score = score;
		return this;
	}

	public FrontStateBuilder name(String name) {
		this.name = name;
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (activePullCard != null) {
			builder.put(KEY_ACTIVE_PULL_CARD, activePullCard);
			notNull = true;
		}
		if (activeHand != null) {
			builder.put(KEY_ACTIVE_HAND, activeHand);
			notNull = true;
		}
		if (score != null) {
			builder.put(KEY_SCORE, score);
			notNull = true;
		}
		if (name != null) {
			builder.put(KEY_NAME, name);
			notNull = true;
		}
		if (notNull) {
			return builder;
		}
		return null;
	}

}
