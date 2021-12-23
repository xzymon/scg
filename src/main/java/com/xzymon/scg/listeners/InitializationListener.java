package com.xzymon.scg.listeners;

import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.CardCategory;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;
import com.xzymon.scg.engine.GameManager;
import com.xzymon.scg.global.GlobalNames;
import com.xzymon.scg.websockets.GameDisplaySessionHandler;
import com.xzymon.scg.websockets.GameDisplaySessionHandlerFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.xzymon.scg.global.GlobalNames.GAMES_REGISTER_NAME;

public class InitializationListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.info(String.format("Context initialization invoked!"));
		GameDisplaySessionHandler handler = GameDisplaySessionHandlerFactory.getHandler();
		GameManager gm = new GameManager();
		sce.getServletContext().setAttribute(GAMES_REGISTER_NAME, gm);
		handler.setApplicationAttribute(GAMES_REGISTER_NAME, gm);
		//TODO: zamienic na wersje bez podawania id - tj. na wersje z automatycznym przydzielaniem id
		gm.addGame(helloGame(), GlobalNames.DEVELOPMENT_DEFAULT_GAME_ID);
		LOGGER.info(String.format("Context initialization finished!"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	private Game helloGame() {
		Game game = new Game();
		List<Card> ongoingCards = new ArrayList<>();
		ongoingCards.add(new Card(1L, "Red card", CardCategory.RED));
		ongoingCards.add(new Card(2L, "Red card", CardCategory.RED));
		ongoingCards.add(new Card(3L, "Blue card", CardCategory.BLUE));
		ongoingCards.add(new Card(4L, "Black card", CardCategory.BLACK));
		ongoingCards.add(new Card(5L, "Green card", CardCategory.GREEN));
		ongoingCards.add(new Card(6L, "Yellow card", CardCategory.YELLOW));
		game.setOngoingCards(ongoingCards);
		Set<Player> players = new HashSet<>();
		Player firstPlayer = new Player(1L, "First player", new ArrayList<>());
		Player secondPlayer = new Player(2L, "Second player", new ArrayList<>());
		Player thirdPlayer = new Player(3L, "Third player", new ArrayList<>());
		players.add(firstPlayer);
		players.add(secondPlayer);
		players.add(thirdPlayer);
		game.setPlayers(players);
		return game;
	}
}
