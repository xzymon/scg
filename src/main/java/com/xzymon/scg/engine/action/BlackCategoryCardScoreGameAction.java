package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public class BlackCategoryCardScoreGameAction extends AbstractCategoryCardScoreGameAction {
	public BlackCategoryCardScoreGameAction(Card cardToBeCovered, Card cardInAction, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(cardToBeCovered, cardInAction, sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	public int getScoreChangeByCategoryOfCardToBeCovered() {
		Integer scoreChange = 0;
		switch (cardToBeCovered.getCategory()) {
			case BLACK -> { /*imposible, only one such card in game*/}
			case BLUE -> scoreChange = 28;
			case GREEN -> scoreChange = 10;
			case YELLOW -> scoreChange = 1;
			case RED -> scoreChange = -8;
		}
		return scoreChange;
	}
}
