package com.xzymon.scg.communication.server;

import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

public class MessageHelper {

	public static PlayerBuilder fromPlayer(Player player) {
		return PlayerBuilder.newInstance().name(player.getName()).sessionId(player.getSessionId());
	}

	public static PlayerListBuilder registeredPlayersFromGame(Game game) {
		PlayerListBuilder registeredPlayers = PlayerListBuilder.newInstance();
		for (Player playerWithSessionId : game.getPlayersWithSessionIds()) {
			registeredPlayers.add(fromPlayer(playerWithSessionId));
		}
		return registeredPlayers;
	}

	public static CardBuilder fromCard(Card card) {
		return CardBuilder.newInstance().id(card.getId()).description(card.getDescription()).category(card.getCategory().toString());
	}

	public static CardBuilder topmostCardFromGame(Game game) {
		return fromCard(game.getTopmostCard());
	}
}
