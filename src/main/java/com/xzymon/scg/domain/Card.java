package com.xzymon.scg.domain;

public class Card {
	private Long id;
	private String description;
	private CardCategory category;

	public Card() {
	}

	public Card(Long id, String description, CardCategory category) {
		this.id = id;
		this.description = description;
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CardCategory getCategory() {
		return category;
	}

	public void setCategory(CardCategory category) {
		this.category = category;
	}
}
