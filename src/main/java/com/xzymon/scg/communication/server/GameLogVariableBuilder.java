package com.xzymon.scg.communication.server;

import org.json.JSONObject;

public class GameLogVariableBuilder {
	//"name":"player","value":"First player","cssClass":"initiator","color":"#00ff00"
	public static final String KEY_NAME = "name";
	public static final String KEY_VALUE = "value";
	public static final String KEY_CSS_CLASS = "cssClass";
	public static final String KEY_COLOR = "color";

	private String name;
	private String value;
	private String cssClass;
	private String color;

	private GameLogVariableBuilder() {
	}

	public static GameLogVariableBuilder newInstance() {
		return new GameLogVariableBuilder();
	}

	public GameLogVariableBuilder name(String name) {
		this.name = name;
		return this;
	}

	public GameLogVariableBuilder value(String value) {
		this.value = value;
		return this;
	}

	public GameLogVariableBuilder cssClass(String cssClass) {
		this.cssClass = cssClass;
		return this;
	}

	public GameLogVariableBuilder color(String color) {
		this.color = color;
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (name != null) {
			builder.put(KEY_NAME, name);
			notNull = true;
		}
		if (value != null) {
			builder.put(KEY_VALUE, value);
			notNull = true;
		}
		if (cssClass != null) {
			builder.put(KEY_CSS_CLASS, cssClass);
			notNull = true;
		}
		if (color != null) {
			builder.put(KEY_COLOR, color);
			notNull = true;
		}
		if (notNull) {
			return builder;
		}
		return null;
	}
}
