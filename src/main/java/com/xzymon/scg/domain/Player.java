package com.xzymon.scg.domain;

import java.util.List;

public class Player {
	private Long id;
	private String name;
	private List<Card> hand;
	private boolean backOut;
	private String sessionId;
	private boolean active;
	private boolean canPullCard;
	private Integer score;

	public Player(Long id, String name, List<Card> hand, Integer initialScore) {
		this.id = id;
		this.name = name;
		this.hand = hand;
		this.score = initialScore;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Card> getHand() {
		return hand;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}

	public boolean hasCardOnHand(Long cardId) {
		for (Card card : this.hand) {
			if (card.getId().equals(cardId)) {
				return true;
			}
		}
		return false;
	}

	public Card getCardOnHandById(Long cardId) {
		for (Card card : this.hand) {
			if (card.getId().equals(cardId)) {
				return card;
			}
		}
		return null;
	}

	public boolean isBackOut() {
		return backOut;
	}

	public void setBackOut(boolean backOut) {
		this.backOut = backOut;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean canPullCard() {
		return canPullCard;
	}

	public void setCanPullCard(boolean canPullCard) {
		this.canPullCard = canPullCard;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
}
