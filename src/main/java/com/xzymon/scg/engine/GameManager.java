package com.xzymon.scg.engine;

import com.xzymon.scg.domain.Game;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class GameManager {
	private final AtomicLong nextId = new AtomicLong(new Date().getTime());
	private final Map<Long, Game> gamesMap = new HashMap<>();

	public Game getGameById(Long gameId) {
		return gamesMap.get(gameId);
	}

	public Long addGame(Game game) {
		if (null != game) {
			Long newGameId = getNextId();
			game.setId(newGameId);
			gamesMap.put(newGameId, game);
			return newGameId;
		} else {
			throw new RuntimeException("Can't add game which is null");
		}
	}

	/**
	 * Wersja tymczasowa, bardziej lopatologiczna, ale dajaca wieksza kontrole "manualna"
	 * @param game
	 * @param id
	 * @return
	 */
	public Long addGame(Game game, Long id) {
		if (null != game) {
			Long newGameId = id;
			game.setId(newGameId);
			gamesMap.put(newGameId, game);
			return newGameId;
		} else {
			throw new RuntimeException("Can't add game which is null");
		}
	}

	private Long getNextId() {
		return nextId.getAndIncrement();
	}
}
