package com.xzymon.scg.websockets;

import com.google.gson.Gson;
import com.xzymon.scg.communication.client.ClientMessage;
import com.xzymon.scg.communication.client.JsonValidationWrapper;
import com.xzymon.scg.communication.client.validator.ClientMessageValidator;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.engine.ClientMessageHandler;
import com.xzymon.scg.engine.GameManager;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.xzymon.scg.global.GlobalNames.GAMES_REGISTER_NAME;

@ServerEndpoint("/gameDisplay")
public class GameDisplayWebsocket {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameDisplayWebsocket.class);
	private static final Gson PREDEFINED_GSON = new Gson();

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
		List<JsonValidationError> validationErrors = ClientMessageValidator.validate(json);
		if (!validationErrors.isEmpty()) {
			for (JsonValidationError error : validationErrors) {
				LOGGER.error(String.format("sessionId: [%1$s], Error while handling message from client: %2$s", session.getId(), error.getDescription()));
			}
		} else {
			ClientMessage clientMessage = PREDEFINED_GSON.fromJson(message, ClientMessage.class);
			GameDisplaySessionHandler handler = GameDisplaySessionHandlerFactory.getHandler();
			ClientMessageHandler.handleClientMessage(clientMessage, handler, session.getId());
		}
	}
}
