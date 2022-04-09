package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;

import java.util.Map;

public abstract class AbstractCategoryCardScoreGameAction extends AbstractGameAction{
	protected Card cardToBeCovered;

	public AbstractCategoryCardScoreGameAction(Card cardToBeCovered, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
		this.cardToBeCovered = cardToBeCovered;
	}

	public Card getCardToBeCovered() {
		return cardToBeCovered;
	}

	public void setCardToBeCovered(Card cardToBeCovered) {
		this.cardToBeCovered = cardToBeCovered;
	}
}
