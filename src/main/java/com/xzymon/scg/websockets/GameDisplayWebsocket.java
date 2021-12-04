package com.xzymon.scg.websockets;

import com.xzymon.scg.domain.Game;
import com.xzymon.scg.engine.GameManager;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.xzymon.scg.global.GlobalNames.GAMES_REGISTER_NAME;

@ServerEndpoint("/gameDisplay")
public class GameDisplayWebsocket {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameDisplayWebsocket.class);

	@OnOpen
	public void open(Session session) {
		GameDisplaySessionHandler handler = GameDisplaySessionHandlerFactory.getHandler();
		LOGGER.info("Session opened: " + session.getId());
		handler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		GameDisplaySessionHandler handler = GameDisplaySessionHandlerFactory.getHandler();
		LOGGER.info("Session closed: " + session.getId());
		handler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		throw new RuntimeException(error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		JSONObject json = new JSONObject(message);
		String gameEventType = json.getString("gameEventType");
		GameDisplaySessionHandler handler = GameDisplaySessionHandlerFactory.getHandler();
		if (gameEventType.equals("pullNextCard")) {
			GameManager gm = (GameManager) handler.getApplicationAttribute(GAMES_REGISTER_NAME);
			Game currentGame = gm.getGameById(1L);
			if (null != currentGame) {
				handler.getNextCardAndBroadcast(currentGame);
			}
		}
	}
}
