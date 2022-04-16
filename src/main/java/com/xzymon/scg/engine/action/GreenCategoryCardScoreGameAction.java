package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public class GreenCategoryCardScoreGameAction extends AbstractCategoryCardScoreGameAction {
	public GreenCategoryCardScoreGameAction(Card cardToBeCovered, Card cardInAction, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(cardToBeCovered, cardInAction, sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	public int getScoreChangeByCategoryOfCardToBeCovered() {
		Integer scoreChange = 0;
		switch (cardToBeCovered.getCategory()) {
			case BLACK -> scoreChange = 22;
			case BLUE -> scoreChange = 10;
			case GREEN -> scoreChange = 4;
			case YELLOW -> scoreChange = 1;
			case RED -> scoreChange = 2;
		}
		return scoreChange;
	}
}
