package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameLogVariableListBuilder {
	private List<JSONObject> vars;

	private GameLogVariableListBuilder() {
		this.vars = new ArrayList<>();
	}

	public static GameLogVariableListBuilder newInstance() {
		return new GameLogVariableListBuilder();
	}

	public GameLogVariableListBuilder add(GameLogVariableBuilder variable) {
		if (null != variable) {
			JSONObject json = variable.build();
			if (null != json) {
				vars.add(json);
			}
		}
		return this;
	}

	public JSONArray build() {
		if (!vars.isEmpty()) {
			JSONArray result = new JSONArray();
			for (JSONObject var : vars) {
				result.put(var);
			}
			return result;
		}
		return null;
	}
}
