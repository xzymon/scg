package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;

import java.util.Map;

public abstract class AbstractPlayCardAction extends AbstractGameAction{
	protected Card cardToPlay;

	public AbstractPlayCardAction(Card card, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
		this.cardToPlay = card;
	}

	protected Card getCardToPlay() {
		return cardToPlay;
	}

	protected void setCardToPlay(Card cardToPlay) {
		this.cardToPlay = cardToPlay;
	}
}
