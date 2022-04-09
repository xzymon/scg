package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public class BlackCategoryCardScoreGameAction extends AbstractCategoryCardScoreGameAction {
	public BlackCategoryCardScoreGameAction(Card cardToBeCovered, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(cardToBeCovered, sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	@Override
	public void execute() {
		Player activePlayer = game.getPlayerByBoundSessionId(sessionId);
		Integer score = activePlayer.getScore();
		switch (cardToBeCovered.getCategory()) {
			case BLACK -> { /*imposible, only one such card in game*/}
			case BLUE -> activePlayer.setScore(score + 28);
			case GREEN -> activePlayer.setScore(score + 10);
			case YELLOW -> activePlayer.setScore(score + 1);
			case RED -> activePlayer.setScore(score - 8);
		}
	}
}
