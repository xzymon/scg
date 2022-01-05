package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;

import java.util.List;
import java.util.Map;

public class RemoveCardFromHandGameAction extends AbstractPlayCardAction {

	public RemoveCardFromHandGameAction(Card card, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(card, sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	@Override
	public void execute() {
		Card cardToRemove = getCardToPlay();
		List<Card> playerHand = game.getActivePlayer().getHand();
		playerHand.remove(cardToRemove);
		sessionIdToMessageBuilderMap.get(sessionId).removeOnPlayerHand(MessageHelper.idFromCard(cardToRemove));
	}
}
