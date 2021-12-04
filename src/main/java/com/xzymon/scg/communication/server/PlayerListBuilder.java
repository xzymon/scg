package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerListBuilder {
	private List<JSONObject> players;

	private PlayerListBuilder() {
		this.players = new ArrayList<>();
	}

	public static PlayerListBuilder newInstance() {
		return new PlayerListBuilder();
	}

	public PlayerListBuilder add(PlayerBuilder player) {
		if (null != player) {
			JSONObject json = player.build();
			if (null != json) {
				players.add(json);
			}
		}
		return this;
	}

	public JSONArray build() {
		if (!players.isEmpty()) {
			JSONArray result = new JSONArray();
			for (JSONObject player : players) {
				result.put(player);
			}
			return result;
		}
		return null;
	}
}
