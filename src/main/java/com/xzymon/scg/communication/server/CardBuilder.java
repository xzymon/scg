package com.xzymon.scg.communication.server;

import org.json.JSONObject;

public class CardBuilder {
	public static final String KEY_ID = "id";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_CATEGORY = "category";

	private Long id;
	private String description;
	private String category;

	private CardBuilder() {
	}

	public static CardBuilder newInstance() {
		return new CardBuilder();
	}

	public CardBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public CardBuilder description(String description) {
		this.description = description;
		return this;
	}

	public CardBuilder category(String category) {
		this.category = category;
		return this;
	}

	public JSONObject build() {
		boolean notNull = false;
		JSONObject builder = new JSONObject();
		if (id != null) {
			builder.put(KEY_ID, id);
			notNull = true;
		}
		if (description != null) {
			builder.put(KEY_DESCRIPTION, description);
			notNull = true;
		}
		if (category != null) {
			builder.put(KEY_CATEGORY, category);
			notNull = true;
		}
		if (notNull) {
			return builder;
		}
		return null;
	}

}
