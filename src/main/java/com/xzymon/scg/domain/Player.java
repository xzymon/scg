package com.xzymon.scg.domain;

import java.util.List;

public class Player {
	private Long id;
	private String name;
	private List<Card> hand;

	public Player(Long id, String name, List<Card> hand) {
		this.id = id;
		this.name = name;
		this.hand = hand;
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
}
