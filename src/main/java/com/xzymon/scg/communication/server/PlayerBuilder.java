package com.xzymon.scg.communication.server;

import org.json.JSONObject;

import java.util.List;

public class PlayerBuilder {
	public static final String KEY_NAME = "name";
	public static final String KEY_SESSION_ID = "sessionId";

	private String name;
	private String sessionId;

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
		if (notNull) {
			return builder;
		}
		return null;
	}
}
