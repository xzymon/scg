package com.xzymon.scg.communication.server;

import org.json.JSONObject;

public class FrontStateBuilder {
	public static final String KEY_ACTIVE_HAND = "activeHand";
	public static final String KEY_ACTIVE_PULL_CARD = "activePullCard";

	private Boolean activeHand;
	private Boolean activePullCard;

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
		if (notNull) {
			return builder;
		}
		return null;
	}

}
