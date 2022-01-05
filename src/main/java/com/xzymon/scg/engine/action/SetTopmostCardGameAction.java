package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;

import java.util.Map;

public class SetTopmostCardGameAction extends AbstractPlayCardAction {

	public SetTopmostCardGameAction(Card card, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(card, sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	@Override
	public void execute() {
		if (null != getCardToPlay()) {
			game.setTopmostCard(getCardToPlay());
			for (Map.Entry<String, MessageBuilder> entry : sessionIdToMessageBuilderMap.entrySet()) {
				entry.getValue().topmostCard(MessageHelper.topmostCardFromGame(game));
			}
		}
	}
}
