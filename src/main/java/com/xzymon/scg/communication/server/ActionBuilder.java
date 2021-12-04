package com.xzymon.scg.communication.server;

import org.json.JSONObject;

public class ActionBuilder {
	public static final String KEY_NAME = "name";
	public static final String KEY_AVAILABILITY = "availability";

	private String name;
	private String availability;

	private ActionBuilder() {
	}

	public static ActionBuilder newInstance() {
		return new ActionBuilder();
	}

	public ActionBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ActionBuilder availability(String availability) {
		this.availability = availability;
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (name != null) {
			builder.put(KEY_NAME, name);
			notNull = true;
		}
		if (availability != null) {
			builder.put(KEY_AVAILABILITY, availability);
			notNull = true;
		}
		if (notNull) {
			return builder;
		}
		return null;
	}
}
