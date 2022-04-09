package com.xzymon.scg.engine.action.input;

import com.xzymon.scg.communication.client.GameEvent;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;

import java.util.Map;

public record GameActionInput(Card playedCard, Card cardToBeCovered, String sessionId, GameEvent gameEvent, Game game, Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
}
