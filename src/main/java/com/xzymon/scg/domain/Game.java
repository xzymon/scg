package com.xzymon.scg.domain;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
	private Long id;
	private final AtomicInteger nextActionId = new AtomicInteger(1);
	private List<Card> ongoingCards;
	private Set<Player> players;
	private List<Player> playerTurnSequence;
	private Card topmostCard;

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

	public List<Player> getPlayerTurnSequence() {
		return playerTurnSequence;
	}

	public void setPlayerTurnSequence(List<Player> playerTurnSequence) {
		this.playerTurnSequence = playerTurnSequence;
	}

	public Card getTopmostCard() {
		return topmostCard;
	}

	public void setTopmostCard(Card topmostCard) {
		this.topmostCard = topmostCard;
	}
}