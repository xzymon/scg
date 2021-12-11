package com.xzymon.scg.websockets;

import com.xzymon.scg.communication.server.*;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;
import com.xzymon.scg.engine.GameManager;
import com.xzymon.scg.global.GlobalNames;
import jakarta.websocket.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.xzymon.scg.global.GlobalNames.GAMES_REGISTER_NAME;

public class GameDisplaySessionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameDisplaySessionHandler.class);

	private static final String BROADCAST_LOG_MSG_FORMAT = "broadcast: message=[%1$s]";
	private static final String BROADCAST_BUT_SESSION_LOG_MSG_FORMAT = "broadcastButSession: sessionId=[%2$s], message=[%1$s]";
	private static final String SEND_ONLY_TO_SESSION_LOG_MSG_FORMAT = "setOnlyToSession: sessionId=[%2$s], message=[%1$s]";

	private final Map<String, Object> applicationAttributesMap = new HashMap<>();
	private final List<Session> sessions = new ArrayList<>();

	public void addApplicationAttribute(String attributeName, Object value) {
		if (!applicationAttributesMap.containsKey(attributeName)) {
			applicationAttributesMap.put(attributeName, value);
		}
	}

	public Object getApplicationAttribute(String key) {
		return applicationAttributesMap.get(key);
	}

	public Object removeApplicationAttribute(String key) {
		return applicationAttributesMap.remove(key);
	}

	public void setApplicationAttribute(String attributeName, Object value) {
		applicationAttributesMap.put(attributeName, value);
	}

	public void addSession(Session session) {
		sessions.add(session);
		bindUnoccupiedPlayerToSession(session);
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	private void broadcast(JSONObject message) {
		String messageText = message.toString();
		LOGGER.info(String.format(BROADCAST_LOG_MSG_FORMAT, messageText));
		for (Session session : sessions) {
			try {
				session.getBasicRemote().sendText(message.toString());
			} catch (IOException e) {
				removeSession(session);
			}
		}
	}

	private void broadcastButSession(JSONObject message, Session session) {
		String messageText = message.toString();
		LOGGER.info(String.format(BROADCAST_BUT_SESSION_LOG_MSG_FORMAT, messageText, session.getId()));
		for (Session actualSession : sessions) {
			if (!actualSession.equals(session)) {
				try {
					actualSession.getBasicRemote().sendText(messageText);
				} catch (IOException e) {
					removeSession(actualSession);
				}
			}
		}
	}

	private void sendOnlyToSession(JSONObject message, Session session) {
		String messageText = message.toString();
		LOGGER.info(String.format(SEND_ONLY_TO_SESSION_LOG_MSG_FORMAT, messageText, session.getId()));
		try {
			session.getBasicRemote().sendText(messageText);
		} catch (IOException e) {
			removeSession(session);
		}
	}

	public void bindUnoccupiedPlayerToSession(Session session) {
		GameDisplaySessionHandler handler = GameDisplaySessionHandlerFactory.getHandler();
		GameManager gm = (GameManager) handler.getApplicationAttribute(GAMES_REGISTER_NAME);
		Card topmostCard = null;
		Game game = gm.getGameById(GlobalNames.DEVELOPMENT_DEFAULT_GAME_ID);
		Player unoccupiedPlayer = game.getFirstUnoccupiedPlayer();
		if (null != unoccupiedPlayer) {
			unoccupiedPlayer.setSessionId(session.getId());
			MessageBuilder broadcastButNewPlayerMB = MessageBuilder.newInstance().newPlayer(MessageHelper.fromPlayer(unoccupiedPlayer));
			MessageBuilder newPlayerMB = MessageBuilder.newInstance().registeredPlayers(MessageHelper.registeredPlayersFromGame(game));

			topmostCard = game.getTopmostCard();
			if (topmostCard != null) {
				CardBuilder topmostCardCB = MessageHelper.topmostCardFromGame(game);
				broadcastButNewPlayerMB.topmostCard(topmostCardCB);
				newPlayerMB.topmostCard(topmostCardCB);
			}

			broadcastButSession(broadcastButNewPlayerMB.build(), session);
			sendOnlyToSession(newPlayerMB.build(), session);
		}
	}

	public void showTopmostCard(Game game) {
		Card topmostCard = game.getTopmostCard();
		if (null != topmostCard) {
			MessageBuilder topmostCardMB = MessageBuilder.newInstance().topmostCard(MessageHelper.topmostCardFromGame(game));
			broadcast(topmostCardMB.build());
		}
	}

	public void getNextCardAndBroadcast(Game game) {
		List<Card> oldOngoingCards = game.getOngoingCards();
		List<Card> newOngoingCards = new ArrayList<>();
		Card pulledCard = null;
		for (Card ongoingCard : oldOngoingCards) {
			if (null == pulledCard) {
				pulledCard = ongoingCard;
			} else {
				newOngoingCards.add(ongoingCard);
			}
		}
		game.setOngoingCards(newOngoingCards);
		if (null != pulledCard) {
			game.setTopmostCard(pulledCard);
			showTopmostCard(game);
		}
	}

}
