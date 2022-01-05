package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public class UpdatePlayerScoreGameAction extends AbstractGameAction {

	public UpdatePlayerScoreGameAction(String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	public void execute() {
		Player activePlayer = game.getPlayerByBoundSessionId(sessionId);
		Integer score = activePlayer.getScore();
		activePlayer.setScore(score + 2);
	}
}