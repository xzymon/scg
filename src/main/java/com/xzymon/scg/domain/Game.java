package com.xzymon.scg.domain;

import com.xzymon.scg.engine.PlayersCycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
	private Long id;
	private final AtomicInteger nextActionId = new AtomicInteger(1);
	private List<Card> ongoingCards;
	private Set<Player> players;
	private PlayersCycle playersCycle;
	private Card lastPulledCard;

	public Game() {
		this.playersCycle = new PlayersCycle();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer showNextActionId() {
		return nextActionId.get();
	}

	public Integer getNextActionId() {
		return nextActionId.getAndIncrement();
	}

	public List<Card> getOngoingCards() {
		return ongoingCards;
	}

	public void setOngoingCards(List<Card> ongoingCards) {
		this.ongoingCards = ongoingCards;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public Player getFirstUnoccupiedPlayer() {
		if (players != null && players.size() > 0) {
			for (Player player : players) {
				if (player.getSessionId() == null) {
					return player;
				}
			}
		}
		return null;
	}

	public Player getPlayerByBoundSessionId(String boundSessionId) {
		if (boundSessionId != null && players != null && players.size() > 0) {
			for (Player player : players) {
				if (boundSessionId.equals(player.getSessionId())) {
					return player;
				}
			}
		}
		return null;
	}

	public List<Player> getPlayersWithSessionIds() {
		if (players != null && players.size() > 0) {
			List<Player> result = new ArrayList<>();
			for (Player player : players) {
				if (player.getSessionId() != null) {
					result.add(player);
				}
			}
			return result;
		}
		return null;
	}

	public void makeNextPlayerActive() {
		playersCycle.next();
	}

	public Card getNextCard() {
		List<Card> newOngoingCards = new ArrayList<>();
		Card pulledCard = null;
		for (Card ongoingCard : ongoingCards) {
			if (null == pulledCard) {
				pulledCard = ongoingCard;
			} else {
				newOngoingCards.add(ongoingCard);
			}
		}
		setOngoingCards(newOngoingCards);
		if (null != pulledCard) {
			setLastPulledCard(pulledCard);
		}
		return pulledCard;
	}

	public PlayersCycle getPlayersCycle() {
		return playersCycle;
	}

	public void setPlayersCycle(PlayersCycle playersCycle) {
		this.playersCycle = playersCycle;
	}

	public Player getActivePlayer() {
		return playersCycle.getCurrent();
	}

	public Card getLastPulledCard() {
		return lastPulledCard;
	}

	public void setLastPulledCard(Card lastPulledCard) {
		this.lastPulledCard = lastPulledCard;
	}
}
