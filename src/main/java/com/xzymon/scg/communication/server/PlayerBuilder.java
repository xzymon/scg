package com.xzymon.scg.communication.server;

import org.json.JSONObject;

import java.util.List;

public class PlayerBuilder {
	public static final String KEY_NAME = "name";
	public static final String KEY_SESSION_ID = "sessionId";
	public static final String KEY_BACK_OUT = "backOut";
	public static final String KEY_ACTIVE = "active";
	public static final String KEY_PULL_CARD = "pullCard";

	private String name;
	private String sessionId;
	private Boolean backOut;
	private Boolean active;
	private Boolean pullCard;

	private PlayerBuilder() {
	}

	public static PlayerBuilder newInstance() {
		return new PlayerBuilder();
	}

	public PlayerBuilder name(String name) {
		this.name = name;
		return this;
	}

	public PlayerBuilder sessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}

	public PlayerBuilder backOut(Boolean backOut) {
		this.backOut = backOut;
		return this;
	}

	public PlayerBuilder active(Boolean active) {
		this.active = active;
		return this;
	}

	public PlayerBuilder pullCard(Boolean pullCard) {
		this.pullCard = pullCard;
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (name != null) {
			builder.put(KEY_NAME, name);
			notNull = true;
		}
		if (sessionId != null) {
			builder.put(KEY_SESSION_ID, sessionId);
			notNull = true;
		}
		if (backOut != null) {
			builder.put(KEY_BACK_OUT, backOut);
			notNull = true;
		}
		if (active != null) {
			builder.put(KEY_ACTIVE, active);
			notNull = true;
		}
		if (pullCard != null) {
			builder.put(KEY_PULL_CARD, pullCard);
			notNull = true;
		}
		if (notNull) {
			return builder;
		}
		return null;
	}
}
