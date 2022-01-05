package com.xzymon.scg.websockets;

import com.xzymon.scg.communication.server.*;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;
import com.xzymon.scg.engine.CardManager;
import com.xzymon.scg.engine.ClientMessageHandler;
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
	private static final String SEND_ONLY_TO_SESSION_LOG_MSG_FORMAT = "sendOnlyToSession: sessionId=[%2$s], message=[%1$s]";

	private final Map<String, Object> applicationAttributesMap = new HashMap<>();
	private final Map<String, Session> sessions = new HashMap<>();

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
		sessions.put(session.getId(), session);
		bindUnoccupiedPlayerToSession(session);
	}

	public void removeSession(Session session) {
		sessions.remove(session.getId());
		unbindPlayerSession(session);
	}

	private void broadcast(JSONObject message) {
		String messageText = message.toString();
		LOGGER.info(String.format(BROADCAST_LOG_MSG_FORMAT, messageText));
		for (Session session : sessions.values()) {
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
		for (Session actualSession : sessions.values()) {
			if (!actualSession.equals(session)) {
				try {
					actualSession.getBasicRemote().sendText(messageText);
				} catch (IOException e) {
					removeSession(actualSession);
				}
			}
		}
	}

	public void sendToSessionById(MessageBuilder messageBuilder, String sessionId) {
		Session entrySession = sessions.get(sessionId);
		JSONObject json = messageBuilder.build();
		if (null != entrySession) {
			sendOnlyToSession(json, entrySession);
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
		CardManager cardManager = game.getCardManager();
		Player unoccupiedPlayer = game.getFirstUnoccupiedPlayer();
		if (null != unoccupiedPlayer) {
			unoccupiedPlayer.setSessionId(session.getId());
			Map<String, MessageBuilder> messageBuildersMap = ClientMessageHandler.initSessionMessageBuilderMap(this);
			game.getPlayersCycle().add(unoccupiedPlayer);
			if (game.getActivePlayer() == null) {
				game.makeNextPlayerActive();
				Player activePlayer = game.getActivePlayer();
				activePlayer.setCanPullCard(true);
				messageBuildersMap.get(session.getId()).frontState(MessageHelper.frontStateFromPlayer(activePlayer));
			} else {
				messageBuildersMap.get(session.getId()).frontState(MessageHelper.frontStateActive(false, false));
			}

			//MessageBuilder broadcastButNewPlayerMB = MessageBuilder.newInstance().newPlayer(MessageHelper.fromPlayer(unoccupiedPlayer));
			MessageBuilder newPlayerMB = messageBuildersMap.get(session.getId());

			topmostCard = game.getLastPulledCard();
			if (topmostCard != null) {
				CardBuilder topmostCardCB = MessageHelper.topmostCardFromGame(game);
				//broadcastButNewPlayerMB.topmostCard(topmostCardCB);
				newPlayerMB.topmostCard(topmostCardCB);
			}

			// give player 3 cards - as its hand
			List<Card> playerHand = new LinkedList<>();
			playerHand.add(cardManager.enhancedNext());
			playerHand.add(cardManager.enhancedNext());
			playerHand.add(cardManager.enhancedNext());
			unoccupiedPlayer.setHand(playerHand);

			newPlayerMB.playerHand(HandBuilder.newInstance().newCards(MessageHelper.fromCards(playerHand)));

			ClientMessageHandler.extendByStateOfRegisteredPlayers(messageBuildersMap, game);
			sendMessages(messageBuildersMap);
		}
	}

	public void unbindPlayerSession(Session session) {
		GameDisplaySessionHandler handler = GameDisplaySessionHandlerFactory.getHandler();
		GameManager gm = (GameManager) handler.getApplicationAttribute(GAMES_REGISTER_NAME);
		Game game = gm.getGameById(GlobalNames.DEVELOPMENT_DEFAULT_GAME_ID);
		Player playerToRemove = game.getPlayerByBoundSessionId(session.getId());
		boolean passTurnToNextPlayer = false;
		String newActivePlayerSessionId = null;
		Player newActivePlayer = null;
		if (null != playerToRemove) {
			Player activePlayer = game.getActivePlayer();
			game.getPlayersCycle().remove(playerToRemove);
			if (playerToRemove.equals(activePlayer)) {
				game.makeNextPlayerActive();
				newActivePlayer = game.getActivePlayer();
				if (null != newActivePlayer) {
					newActivePlayerSessionId = newActivePlayer.getSessionId();
					passTurnToNextPlayer = true;
				}
			}

			List<Card> playersCardsToDiscard = playerToRemove.getHand();
			if (null != playersCardsToDiscard && !playersCardsToDiscard.isEmpty()) {
				CardManager cardManager = game.getCardManager();
				for (Card card : playersCardsToDiscard) {
					cardManager.discard(card);
				}
			}

			Map<String, MessageBuilder> messageBuildersMap = ClientMessageHandler.initSessionMessageBuilderMap(this);
			messageBuildersMap.remove(playerToRemove.getSessionId());
			for (Map.Entry<String, MessageBuilder> entry : messageBuildersMap.entrySet()) {
				entry.getValue().removedPlayer(MessageHelper.fromPlayer(playerToRemove));
				if (passTurnToNextPlayer && entry.getKey().equals(newActivePlayerSessionId)) {
					newActivePlayer.setCanPullCard(true);
					entry.getValue().frontState(MessageHelper.frontStateFromPlayer(newActivePlayer));
				}
			}
			sendMessages(messageBuildersMap);
			playerToRemove.setSessionId(null);
		}
	}

	public void showTopmostCard(Game game) {
		Card topmostCard = game.getLastPulledCard();
		if (null != topmostCard) {
			MessageBuilder topmostCardMB = MessageBuilder.newInstance().topmostCard(MessageHelper.topmostCardFromGame(game));
			broadcast(topmostCardMB.build());
		}
	}

	public void sendMessages(Map<String, MessageBuilder> sessionIdToMessageBuilderMap) {
		for (Map.Entry<String, MessageBuilder> entry : sessionIdToMessageBuilderMap.entrySet()) {
			Session entrySession = sessions.get(entry.getKey());
			JSONObject json = entry.getValue().build();
			if (null != entrySession) {
				sendOnlyToSession(json, entrySession);
			}
		}
	}

	public Set<String> getSessionIds() {
		Set<String> result = new HashSet<>();
		if (null != sessions && !sessions.isEmpty()) {
			for (String sessionId : sessions.keySet()) {
				result.add(sessionId);
			}
		}
		return result;
	}
}
