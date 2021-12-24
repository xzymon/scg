package com.xzymon.scg.engine;

import com.xzymon.scg.domain.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CardStack {
	private static final Logger LOGGER = LoggerFactory.getLogger(CardStack.class);
	private static final String APPEND_CARD_LOG_MSG_FORMAT = "[CardStack name: %1$s] Appending card: %2$s";

	private List<Card> stack;
	private int nextCardIndex;
	private String name;

	public CardStack(String name) {
		this.name = name;
		stack = new ArrayList<Card>();
	}

	public String getName() {
		return name;
	}

	public void append(Card card) {
		LOGGER.info(String.format(APPEND_CARD_LOG_MSG_FORMAT, name, card.getDescription()));
		stack.add(card);
	}

	public void append(List<Card> cards) {
		if (cards != null) {
			for (Card card : cards) {
				append(card);
			}
		}
	}

	public int getCurrentStackDepth() {
		return stack.size() - nextCardIndex;
	}

	public boolean canPull() {
		return getCurrentStackDepth() > 0;
	}

	public Card pull() {
		if (canPull()) {
			return stack.get(nextCardIndex++);
		}
		return null;
	}
}
