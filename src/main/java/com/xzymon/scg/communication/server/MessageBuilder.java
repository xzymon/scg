package com.xzymon.scg.communication.server;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageBuilder {
	public static final String KEY_REGISTERED_PLAYERS = "registeredPlayers";
	public static final String KEY_NEW_PLAYER = "newPlayer";
	public static final String KEY_REMOVED_PLAYER = "removedPlayer";
	public static final String KEY_TOPMOST_CARD = "topmostCard";
	public static final String KEY_FRONT_STATE = "frontState";
	public static final String KEY_PLAYER_HAND = "playerHand";

	PlayerListBuilder registeredPlayers;
	PlayerBuilder newPlayer;
	PlayerBuilder removedPlayer;
	CardBuilder topmostCard;
	FrontStateBuilder frontState;
	HandBuilder playerHand;

	private MessageBuilder() {}

	public static MessageBuilder newInstance() {
		return new MessageBuilder();
	}

	public MessageBuilder registeredPlayers(PlayerListBuilder registeredPlayers) {
		this.registeredPlayers = registeredPlayers;
		return this;
	}

	private JSONArray buildRegisteredPlayers() {
		if (null != registeredPlayers) {
			JSONArray array = registeredPlayers.build();
			if (!array.isEmpty()) {
				return array;
			}
		}
		return null;
	}

	public MessageBuilder newPlayer(PlayerBuilder newPlayer) {
		this.newPlayer = newPlayer;
		return this;
	}

	private JSONObject buildNewPlayer() {
		if (null != newPlayer) {
			return newPlayer.build();
		}
		return null;
	}

	public MessageBuilder removedPlayer(PlayerBuilder removedPlayer) {
		this.removedPlayer = removedPlayer;
		return this;
	}

	private JSONObject buildRemovedPlayer() {
		if (null != removedPlayer) {
			return removedPlayer.build();
		}
		return null;
	}

	public MessageBuilder topmostCard(CardBuilder topmostCard) {
		this.topmostCard = topmostCard;
		return this;
	}

	private JSONObject buildTopmostCard() {
		if (null != topmostCard) {
			return topmostCard.build();
		}
		return null;
	}

	public MessageBuilder frontState(FrontStateBuilder frontState) {
		this.frontState = frontState;
		return this;
	}

	private JSONObject buildFrontState() {
		if (null != frontState) {
			return frontState.build();
		}
		return null;
	}

	public MessageBuilder frontStateName(String name) {
		if (null == frontState) {
			frontState = FrontStateBuilder.newInstance();
		}
		frontState.name(name);
		return this;
	}

	public MessageBuilder playerHand(HandBuilder handBuilder) {
		this.playerHand = handBuilder;
		return this;
	}

	private JSONObject buildPlayerHand() {
		if (null != playerHand) {
			return playerHand.build();
		}
		return null;
	}

	public MessageBuilder removeOnPlayerHand(CardBuilder cardBuilder) {
		if (null == playerHand) {
			playerHand = HandBuilder.newInstance();
		}
		playerHand.addRemovedCard(cardBuilder);
		return this;
	}

	public MessageBuilder maintainOnPlayerHand(CardBuilder cardBuilder) {
		if (null == playerHand) {
			playerHand = HandBuilder.newInstance();
		}
		playerHand.addMaintainedCard(cardBuilder);
		return this;
	}

	public MessageBuilder newOnPlayerHand(CardBuilder cardBuilder) {
		if (null == playerHand) {
			playerHand = HandBuilder.newInstance();
		}
		playerHand.addNewCard(cardBuilder);
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject result = new JSONObject();
		JSONArray registeredPlayers = buildRegisteredPlayers();
		if (null != registeredPlayers) {
			result.put(KEY_REGISTERED_PLAYERS, registeredPlayers);
			notNull = true;
		}
		JSONObject newPlayer = buildNewPlayer();
		if (null != newPlayer) {
			result.put(KEY_NEW_PLAYER, newPlayer);
			notNull = true;
		}
		JSONObject removedPlayer = buildRemovedPlayer();
		if (null != removedPlayer) {
			result.put(KEY_REMOVED_PLAYER, removedPlayer);
			notNull = true;
		}
		JSONObject topmostCard = buildTopmostCard();
		if (null != topmostCard) {
			result.put(KEY_TOPMOST_CARD, topmostCard);
			notNull = true;
		}
		JSONObject frontState = buildFrontState();
		if (null != frontState) {
			result.put(KEY_FRONT_STATE, frontState);
			notNull = true;
		}
		JSONObject playerHand = buildPlayerHand();
		if (null != playerHand) {
			result.put(KEY_PLAYER_HAND, playerHand);
			notNull = true;
		}
		if (notNull) {
			return result;
		}
		return null;
	}
}
