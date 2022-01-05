package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public class PassTurnToNextPlayerGameAction extends AbstractGameAction {

	public PassTurnToNextPlayerGameAction(String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	@Override
	public void execute() {
		Player activePlayerBeforeExecution = game.getActivePlayer();
		game.makeNextPlayerActive();
		Player activePlayerAfterExecution = game.getActivePlayer();
		if (!activePlayerAfterExecution.equals(activePlayerBeforeExecution)) {
			activePlayerBeforeExecution.setCanPullCard(false);
			sessionIdToMessageBuilderMap.get(activePlayerBeforeExecution.getSessionId()).frontState(MessageHelper.frontStateFromPlayer(activePlayerBeforeExecution));
		}
		activePlayerAfterExecution.setCanPullCard(true);
		sessionIdToMessageBuilderMap.get(activePlayerAfterExecution.getSessionId()).frontState(MessageHelper.frontStateFromPlayer(activePlayerAfterExecution));
	}
}
