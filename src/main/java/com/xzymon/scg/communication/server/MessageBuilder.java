package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageBuilder {
	public static final String KEY_REGISTERED_PLAYERS = "registeredPlayers";
	public static final String KEY_NEW_PLAYER = "newPlayer";
	public static final String KEY_ACTIONS = "actions";
	public static final String KEY_TOPMOST_CARD = "topmostCard";

	JSONArray registeredPlayers;
	JSONObject newPlayer;
	JSONArray actions;
	JSONObject topmostCard;

	private MessageBuilder() {}

	public static MessageBuilder newInstance() {
		return new MessageBuilder();
	}

	public MessageBuilder registeredPlayers(PlayerListBuilder registeredPlayers) {
		if (null != registeredPlayers) {
			JSONArray json = registeredPlayers.build();
			if (!json.isEmpty()) {
				this.registeredPlayers = json;
			}
		} else {
			this.registeredPlayers = null;
		}
		return this;
	}

	public MessageBuilder newPlayer(PlayerBuilder newPlayer) {
		if (null != newPlayer) {
			JSONObject json = newPlayer.build();
			if (null != json) {
				this.newPlayer = json;
			}
		} else {
			this.newPlayer = null;
		}
		return this;
	}

	public MessageBuilder actions(PlayerListBuilder registeredPlayers) {
		if (null != registeredPlayers) {
			JSONArray json = registeredPlayers.build();
			if (!json.isEmpty()) {
				this.registeredPlayers = json;
			}
		} else {
			this.registeredPlayers = null;
		}
		return this;
	}

	public MessageBuilder topmostCard(CardBuilder topmostCard) {
		if (null != topmostCard) {
			JSONObject json = topmostCard.build();
			if (null != json) {
				this.topmostCard = json;
			}
		} else {
			this.topmostCard = null;
		}
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject result = new JSONObject();
		if (null != registeredPlayers && !registeredPlayers.isEmpty()) {
			result.put(KEY_REGISTERED_PLAYERS, registeredPlayers);
			notNull = true;
		}
		if (null != newPlayer) {
			result.put(KEY_NEW_PLAYER, newPlayer);
			notNull = true;
		}
		if (null != actions && !actions.isEmpty()) {
			result.put(KEY_ACTIONS, actions);
			notNull = true;
		}
		if (null != topmostCard) {
			result.put(KEY_TOPMOST_CARD, topmostCard);
			notNull = true;
		}
		if (notNull) {
			return result;
		}
		return null;
	}
}
