package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameLogListBuilder {
	private List<JSONObject> gameLogs;

	private GameLogListBuilder() {
		this.gameLogs = new ArrayList<>();
	}

	public static GameLogListBuilder newInstance() {
		return new GameLogListBuilder();
	}

	public GameLogListBuilder add(GameLogBuilder gameLog) {
		if (null != gameLog) {
			JSONObject json = gameLog.build();
			if (null != json) {
				gameLogs.add(json);
			}
		}
		return this;
	}

	public JSONArray build() {
		if (!gameLogs.isEmpty()) {
			JSONArray result = new JSONArray();
			for (JSONObject gameLog : gameLogs) {
				result.put(gameLog);
			}
			return result;
		}
		return null;
	}
}
