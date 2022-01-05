package com.xzymon.scg.communication.server;

import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;

import java.util.List;

public class MessageHelper {

	public static PlayerBuilder fromPlayer(Player player) {
		return PlayerBuilder.newInstance().name(player.getName()).sessionId(player.getSessionId()).backOut(player.isBackOut()).active(player.isActive()).pullCard(player.canPullCard());
	}

	public static PlayerListBuilder registeredPlayersFromGame(Game game) {
		PlayerListBuilder registeredPlayers = PlayerListBuilder.newInstance();
		for (Player playerWithSessionId : game.getPlayersWithSessionIds()) {
			registeredPlayers.add(fromPlayer(playerWithSessionId));
		}
		return registeredPlayers;
	}

	public static CardBuilder idFromCard(Card card) {
		return CardBuilder.newInstance().id(card.getId()).description(card.getDescription()).category(card.getCategory().toString());
	}

	public static CardListBuilder idsFromCards(List<Card> cards) {
		CardListBuilder cardListBuilder = CardListBuilder.newInstance();
		for (Card card : cards) {
			cardListBuilder.add(idFromCard(card));
		}
		return cardListBuilder;
	}

	public static CardBuilder fromCard(Card card) {
		return CardBuilder.newInstance().id(card.getId()).description(card.getDescription()).category(card.getCategory().toString());
	}

	public static CardListBuilder fromCards(List<Card> cards) {
		CardListBuilder cardListBuilder = CardListBuilder.newInstance();
		for (Card card : cards) {
			cardListBuilder.add(fromCard(card));
		}
		return cardListBuilder;
	}

	public static CardBuilder topmostCardFromGame(Game game) {
		return fromCard(game.getTopmostCard());
	}

	public static FrontStateBuilder frontStateActive(Boolean pullCard, Boolean hand) {
		return FrontStateBuilder.newInstance().activePullCard(pullCard).activeHand(hand);
	}

	public static FrontStateBuilder frontStateFromPlayer(Player player) {
		boolean pullCard = player.isActive() && player.canPullCard();
		boolean hand = player.isActive() && !player.canPullCard();
		return FrontStateBuilder.newInstance().activePullCard(pullCard).activeHand(hand);
	}
}
