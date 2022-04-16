package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public class BlueCategoryCardScoreGameAction extends AbstractCategoryCardScoreGameAction {
	public BlueCategoryCardScoreGameAction(Card cardToBeCovered, Card cardInAction, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(cardToBeCovered, cardInAction, sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	public int getScoreChangeByCategoryOfCardToBeCovered() {
		Integer scoreChange = 0;
		switch (cardToBeCovered.getCategory()) {
			case BLACK -> scoreChange = 36;
			case BLUE -> scoreChange = 16;
			case GREEN -> scoreChange = 6;
			case YELLOW -> scoreChange = 1;
			case RED -> scoreChange = -4;
		}
		return scoreChange;
	}
}
