package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;

import java.util.Map;

public class PullNextCardGameAction extends AbstractGameAction {

	public PullNextCardGameAction(String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	public void execute() {
		Card nextCard = game.getNextCard();
		if (null != nextCard) {
			for (Map.Entry<String, MessageBuilder> entry : sessionIdToMessageBuilderMap.entrySet()) {
				entry.getValue().topmostCard(MessageHelper.topmostCardFromGame(game));
			}
		}
	}
}
