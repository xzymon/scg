package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;

import java.util.Map;

public class DiscardLastPulledCardGameAction extends AbstractGameAction {

	public DiscardLastPulledCardGameAction(String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder > sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	public void execute() {
		Card lastPulledCard = game.getLastPulledCard();
		if (null != lastPulledCard) {
			game.getCardManager().discard(lastPulledCard);
		}
	}
}
