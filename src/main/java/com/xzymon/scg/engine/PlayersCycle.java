package com.xzymon.scg.engine;

import com.xzymon.scg.domain.Player;

import java.util.Iterator;
import java.util.LinkedList;

public class PlayersCycle {
	private LinkedList<Player> players;
	private Player currentPlayer;

	public PlayersCycle() {
		players = new LinkedList<Player>();
	}

	public Player next() {
		if (currentPlayer != null) {
			currentPlayer.setActive(false);
		}
		Player result;
		Iterator<Player> it = nextIteratorOnCurrentPlayer();
		if (null != it) {
			if (it.hasNext()) {
				result = it.next();
			} else {
				result = players.iterator().next();
			}
			result.setActive(true);
			currentPlayer = result;
		}
		return null;
	}

	public Player previous() {
		if (currentPlayer != null) {
			currentPlayer.setActive(false);
		}
		Player result;
		Iterator<Player> it = previousIteratorOnCurrentPlayer();
		if (null != it) {
			if (it.hasNext()) {
				result = it.next();
			} else {
				result = players.descendingIterator().next();
			}
			result.setActive(true);
			currentPlayer = result;
		}
		return null;

	}

	public Player getNext() {
		Iterator<Player> it = nextIteratorOnCurrentPlayer();
		if (null != it) {
			if (it.hasNext()) {
				return it.next();
			} else {
				return players.iterator().next();
			}
		}
		return null;
	}

	public Player getPrevious() {
		Iterator<Player> it = previousIteratorOnCurrentPlayer();
		if (null != it) {
			if (it.hasNext()) {
				return it.next();
			} else {
				return players.descendingIterator().next();
			}
		}
		return null;
	}

	public Player getCurrent() {
		return this.currentPlayer;
	}

	public void add(Player newInCycle) {
		if (players.size() == 0) {
			players.add(newInCycle);
			return;
		}
		for (Player player : players) {
			if (player.equals(newInCycle)) {
				return;
			}
		}
		players.add(newInCycle);
	}

	/*
	public void addNext(Player newInCycle) {
		if (players.size() == 0) {
			players.add(newInCycle);
			currentPlayer = newInCycle;
		}

	}

	public void addPrevious(Player newInCycle) {

	}

	public void addFirst(Player newInCycle) {

	}

	public void addLast(Player newInCycle) {

	}

	public void addAfter(Player newInCycle, Player afterWhichToAdd) {

	}

	public void addBefore(Player newInCycle, Player beforeWhichToAdd) {

	}*/

	public void remove(Player player) {
		if (player != null) {
			if (player.equals(currentPlayer)) {
				currentPlayer = next();
			}
			players.remove(player);
		}
	}

	public int getCurrentCycleLength() {
		return players.size();
	}

	private Iterator<Player> nextIteratorOnCurrentPlayer() {
		if (players.size() > 0) {
			Iterator<Player> iterator = players.iterator();
			if (currentPlayer == null) {
				return iterator;
			} else {
				Player test;
				while(iterator.hasNext()) {
					test = iterator.next();
					if (test.equals(currentPlayer)) {
						return iterator;
					}
				}
			}
		}
		return null;
	}

	private Iterator<Player> previousIteratorOnCurrentPlayer() {
		if (players.size() > 0) {
			Iterator<Player> iterator = players.descendingIterator();
			if (currentPlayer == null) {
				return iterator;
			} else {
				Player test;
				while(iterator.hasNext()) {
					test = iterator.next();
					if (test.equals(currentPlayer)) {
						return iterator;
					}
				}
			}
		}
		return null;
	}
}
