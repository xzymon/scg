package com.xzymon.scg.communication.server;

import org.json.JSONObject;

public class FrontStateBuilder {
	public static final String KEY_ACTIVE = "active";

	private Boolean active;

	private FrontStateBuilder() {
	}

	public static FrontStateBuilder newInstance() {
		return new FrontStateBuilder();
	}

	public FrontStateBuilder active(Boolean active) {
		this.active = active;
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (active != null) {
			builder.put(KEY_ACTIVE, active);
			notNull = true;
		}
		if (notNull) {
			return builder;
		}
		return null;
	}

}
