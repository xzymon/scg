package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActionListBuilder {
	private List<JSONObject> actions;

	private ActionListBuilder() {
		this.actions = new ArrayList<>();
	}

	public static ActionListBuilder newInstance() {
		return new ActionListBuilder();
	}

	public ActionListBuilder add(ActionBuilder action) {
		if (null != action) {
			JSONObject json = action.build();
			if (null != json) {
				actions.add(json);
			}
		}
		return this;
	}

	public JSONArray build() {
		if (!actions.isEmpty()) {
			JSONArray result = new JSONArray();
			for (JSONObject action : actions) {
				result.put(action);
			}
			return result;
		}
		return null;
	}
}
