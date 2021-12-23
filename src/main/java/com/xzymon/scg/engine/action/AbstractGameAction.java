package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Game;

import java.util.Map;

public abstract class AbstractGameAction implements GameAction {
	protected String sessionId;
	protected GameEvent gameEvent;
	protected Game game;
	protected Map<String, MessageBuilder> sessionIdToMessageBuilderMap;

	public AbstractGameAction(String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		this.sessionId = sessionId;
		this.gameEvent = gameEvent;
		this.game = game;
		this.sessionIdToMessageBuilderMap = sessionIdToMessageBuilderMap;
	}

	protected String getSessionId() {
		return sessionId;
	}

	protected void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	protected GameEvent getGameEvent() {
		return gameEvent;
	}

	protected void setGameEvent(GameEvent gameEvent) {
		this.gameEvent = gameEvent;
	}

	protected Game getGame() {
		return game;
	}

	protected void setGame(Game game) {
		this.game = game;
	}

	protected Map<String, MessageBuilder> getSessionIdToMessageBuilderMap() {
		return sessionIdToMessageBuilderMap;
	}

	protected void setSessionIdToMessageBuilderMap(Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		this.sessionIdToMessageBuilderMap = sessionIdToMessageBuilderMap;
	}
}
