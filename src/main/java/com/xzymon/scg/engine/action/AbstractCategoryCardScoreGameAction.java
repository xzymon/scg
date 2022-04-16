package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public abstract class AbstractCategoryCardScoreGameAction extends AbstractGameAction{
	protected Card cardToBeCovered;
	protected Card cardInAction;

	public AbstractCategoryCardScoreGameAction(Card cardToBeCovered, Card cardInAction, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
		this.cardToBeCovered = cardToBeCovered;
		this.cardInAction = cardInAction;
	}

	@Override
	public void execute() {
		Player activePlayer = game.getPlayerByBoundSessionId(sessionId);
		Integer scoreChange = getScoreChangeByCategoryOfCardToBeCovered();
		Integer score = activePlayer.getScore();
		activePlayer.setScore(score + scoreChange);
		sessionIdToMessageBuilderMap.get(sessionId).newGameLogMessage(MessageHelper.standardPlayCardActionGameLog(activePlayer, cardInAction, scoreChange));
	}

	public abstract int getScoreChangeByCategoryOfCardToBeCovered();

	public Card getCardToBeCovered() {
		return cardToBeCovered;
	}

	public void setCardToBeCovered(Card cardToBeCovered) {
		this.cardToBeCovered = cardToBeCovered;
	}

	public Card getCardInAction() {
		return cardInAction;
	}

	public void setCardInAction(Card cardInAction) {
		this.cardInAction = cardInAction;
	}
}
