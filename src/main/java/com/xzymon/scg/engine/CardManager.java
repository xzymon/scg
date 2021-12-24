package com.xzymon.scg.engine;

import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.CardSetName;

import java.util.*;

public class CardManager {
	private Map<CardSetName, Set<Card>> cardSubsetMap;

	private CardStack cardStack;
	private CardStack deadCardStack;

	public CardManager() {
		cardSubsetMap = new HashMap<>();
		cardStack = new CardStack("Available");
		deadCardStack = new CardStack("Sheol");
	}

	public void addCardSubset(CardSetName subsetName, Set<Card> subsetCards) {
		cardSubsetMap.put(subsetName, subsetCards);
	}

	public void initStackFromSubsets(List<CardSetName> subsetNames) {
		if (subsetNames != null && !subsetNames.isEmpty()) {
			List<Card> futureStackContent = new ArrayList<>();
			Set<Card> workingSubset;
			for (CardSetName subsetName : subsetNames) {
				workingSubset = cardSubsetMap.get(subsetName);
				if (workingSubset != null) {
					futureStackContent.addAll(workingSubset);
				}
			}
			// break order of cards
			Collections.shuffle(futureStackContent);
			cardStack.append(futureStackContent);
		}
	}

	public Card next() {
		return cardStack.pull();
	}

	public void discard(Card card) {
		deadCardStack.append(card);
	}

	public Card enhancedNext() {
		Card result = next();
		if (null == result && deadCardStack.getCurrentStackDepth() > 0) {
			List<Card> newSupplyForStack = new ArrayList<>();
			while (deadCardStack.canPull()) {
				newSupplyForStack.add(deadCardStack.pull());
			}
			Collections.shuffle(newSupplyForStack);
			cardStack.append(newSupplyForStack);
			result = next();
		}
		return result;
	}
}
