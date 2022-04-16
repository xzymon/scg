package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

public class GameLogBuilder {

	public static final String KEY_CONTENT = "content";
	public static final String KEY_VARS = "vars";

	private String content;
	private GameLogVariableListBuilder vars;

	private GameLogBuilder() {
		this.vars = GameLogVariableListBuilder.newInstance();
	}

	public static GameLogBuilder newInstance() {
		return new GameLogBuilder();
	}

	public GameLogBuilder content(String content) {
		this.content = content;
		return this;
	}

	public GameLogBuilder vars(GameLogVariableListBuilder vars) {
		this.vars = vars;
		return this;
	}

	public GameLogBuilder addVar(GameLogVariableBuilder var) {
		if (null == vars) {
			vars = GameLogVariableListBuilder.newInstance();
		}
		vars.add(var);
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (content != null) {
			builder.put(KEY_CONTENT, content);
			notNull = true;
		}
		if (vars != null) {
			JSONArray array = vars.build();
			if (array != null) {
				builder.put(KEY_VARS, array);
				notNull = true;
			}
		}

		if (notNull) {
			return builder;
		}
		return null;
	}
}
