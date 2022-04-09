package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public class YellowCategoryCardScoreGameAction extends AbstractCategoryCardScoreGameAction {
	public YellowCategoryCardScoreGameAction(Card cardToBeCovered, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(cardToBeCovered, sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	@Override
	public void execute() {
		Player activePlayer = game.getPlayerByBoundSessionId(sessionId);
		Integer score = activePlayer.getScore();
		switch (cardToBeCovered.getCategory()) {
			case BLACK -> activePlayer.setScore(score + 15);
			case BLUE -> activePlayer.setScore(score + 7);
			case GREEN -> activePlayer.setScore(score + 3);
			case YELLOW -> activePlayer.setScore(score + 1);
			case RED -> activePlayer.setScore(score - 1);
		}
	}
}

