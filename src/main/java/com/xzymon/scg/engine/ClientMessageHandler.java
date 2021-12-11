package com.xzymon.scg.engine;

import com.xzymon.scg.communication.client.ClientMessage;
import com.xzymon.scg.communication.client.JsonKeys;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.global.GlobalNames;
import com.xzymon.scg.websockets.GameDisplaySessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

	private static final String SESSION_ID_MSG_PREFIX = "[sessionId: %1$s] ";
	private static final String UNKNOWN_GAME_EVENT_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Unknown " + JsonKeys.GAME_EVENT.getKey() + "." + JsonKeys.NAME.getKey() + ": %2$s";

	public static void handleClientMessage(ClientMessage clientMessage, GameDisplaySessionHandler sessionHandler, String sessionId) {
		GameManager gm = (GameManager) sessionHandler.getApplicationAttribute(GlobalNames.GAMES_REGISTER_NAME);
		Game currentGame = gm.getGameById(GlobalNames.DEVELOPMENT_DEFAULT_GAME_ID);
		if (clientMessage.getGameEvent() != null) {
			switch (clientMessage.getGameEvent().getName()) {
				case "pullNextCard":
					sessionHandler.getNextCardAndBroadcast(currentGame);
					break;
				default:
					LOGGER.error(String.format(UNKNOWN_GAME_EVENT_MSG_FORMAT, sessionId, clientMessage.getGameEvent().getName()));
			}
		}
	}
}
