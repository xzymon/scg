package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
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
		activePlayerBeforeExecution.setCanPullCard(false);
		game.makeNextPlayerActive();
		Player activePlayerAfterExecution = game.getActivePlayer();
		activePlayerAfterExecution.setCanPullCard(true);
	}
}
