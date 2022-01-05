package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.FrontStateBuilder;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Game;

import java.util.Map;

public class ReactivateActivePlayerGameAction extends AbstractGameAction {

	public ReactivateActivePlayerGameAction(String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	@Override
	public void execute() {
		MessageBuilder activePlayerMB = sessionIdToMessageBuilderMap.get(sessionId);
		activePlayerMB.frontState(FrontStateBuilder.newInstance().activePullCard(true));
	}
}
