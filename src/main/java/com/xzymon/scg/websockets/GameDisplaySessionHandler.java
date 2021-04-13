package com.xzymon.scg.websockets;

import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.engine.GameManager;
import jakarta.websocket.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static com.xzymon.scg.global.GlobalNames.GAMES_REGISTER_NAME;

public class GameDisplaySessionHandler {
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
		showTopmostCard();
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	private void sendMessage(JSONObject message) {
		for (Session session : sessions) {
			try {
				session.getBasicRemote().sendText(message.toString());
			} catch (IOException e) {
				removeSession(session);
			}
		}
	}

	private void sendMessage(JSONObject message, Session session) {
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException e) {
			removeSession(session);
		}
	}

	public void showTopmostCard() {
		GameDisplaySessionHandler handler = GameDisplaySessionHandlerFactory.getHandler();
		GameManager gm = (GameManager) handler.getApplicationAttribute(GAMES_REGISTER_NAME);
		Game game = gm.getGameById(1L);
		Card topmostCard = game.getTopmostCard();
		if (null != topmostCard) {
			JSONObject json = new JSONObject();
			json.append("id", topmostCard.getId().toString());
			json.append("description", topmostCard.getDescription());
			json.append("category", topmostCard.getCategory().toString());
			sendMessage(json);
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
			showTopmostCard();
		}
	}

}
