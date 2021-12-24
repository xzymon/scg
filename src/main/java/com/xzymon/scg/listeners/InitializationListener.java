package com.xzymon.scg.listeners;

import com.xzymon.scg.domain.*;
import com.xzymon.scg.engine.CardManager;
import com.xzymon.scg.engine.GameManager;
import com.xzymon.scg.global.GlobalNames;
import com.xzymon.scg.websockets.GameDisplaySessionHandler;
import com.xzymon.scg.websockets.GameDisplaySessionHandlerFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
		CardManager cardManager = initCardManager();
		game.setCardManager(cardManager);

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

	private CardManager initCardManager() {
		CardManager cardManager = new CardManager();

		Set<Card> blackCards = new HashSet<>();
		blackCards.add(new Card(1L, "1st black card", CardCategory.BLACK));
		cardManager.addCardSubset(CardSetName.BLACK, blackCards);

		Set<Card> blueCards = new HashSet<>();
		blueCards.add(new Card(1L, "1st blue card", CardCategory.BLUE));
		blueCards.add(new Card(2L, "2nd blue card", CardCategory.BLUE));
		blueCards.add(new Card(3L, "3rd blue card", CardCategory.BLUE));
		blueCards.add(new Card(4L, "4th blue card", CardCategory.BLUE));
		blueCards.add(new Card(5L, "5th blue card", CardCategory.BLUE));
		cardManager.addCardSubset(CardSetName.BLUE, blueCards);

		Set<Card> greenCards = new HashSet<>();
		greenCards.add(new Card(1L, "1st green card", CardCategory.GREEN));
		greenCards.add(new Card(2L, "2nd green card", CardCategory.GREEN));
		greenCards.add(new Card(3L, "3rd green card", CardCategory.GREEN));
		cardManager.addCardSubset(CardSetName.GREEN, greenCards);

		Set<Card> yellowCards = new HashSet<>();
		yellowCards.add(new Card(1L, "1st yellow card", CardCategory.YELLOW));
		yellowCards.add(new Card(1L, "2nd yellow card", CardCategory.YELLOW));
		yellowCards.add(new Card(1L, "3rd yellow card", CardCategory.YELLOW));
		yellowCards.add(new Card(1L, "4th yellow card", CardCategory.YELLOW));
		yellowCards.add(new Card(1L, "5th yellow card", CardCategory.YELLOW));
		yellowCards.add(new Card(1L, "6th yellow card", CardCategory.YELLOW));
		yellowCards.add(new Card(1L, "7th yellow card", CardCategory.YELLOW));
		cardManager.addCardSubset(CardSetName.YELLOW, yellowCards);

		Set<Card> redCards = new HashSet<>();
		redCards.add(new Card(1L, "1st red card", CardCategory.RED));
		redCards.add(new Card(2L, "2nd red card", CardCategory.RED));
		redCards.add(new Card(3L, "3rd red card", CardCategory.RED));
		redCards.add(new Card(4L, "4th red card", CardCategory.RED));
		cardManager.addCardSubset(CardSetName.RED, redCards);

		cardManager.initStackFromSubsets(Arrays.asList(CardSetName.BLACK, CardSetName.BLUE, CardSetName.YELLOW, CardSetName.RED));

		return cardManager;
	}
}
