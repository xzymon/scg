package com.xzymon.scg.engine.action;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.Map;

public class PullNextCardAndActivateHandGameAction extends AbstractGameAction {

	public PullNextCardAndActivateHandGameAction(String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		super(sessionId, gameEvent, game, sessionIdToMessageBuilderMap);
	}

	public void execute() {
		Player activePlayer = game.getActivePlayer();
		MessageBuilder playerMB = sessionIdToMessageBuilderMap.get(sessionId);
		if (activePlayer.canPullCard()) {
			Card nextCard = game.getNextCard();
			if (null != nextCard) {
				game.getActivePlayer().getHand().add(nextCard);
				playerMB.newOnPlayerHand(MessageHelper.fromCard(nextCard));
			}
			activePlayer.setCanPullCard(false);
		}
		playerMB.frontState(MessageHelper.frontStateFromPlayer(activePlayer));
	}
}
