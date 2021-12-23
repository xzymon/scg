package com.xzymon.scg.engine;

import com.xzymon.scg.communication.client.ClientMessage;
import com.xzymon.scg.communication.client.JsonKeys;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.communication.server.PlayerListBuilder;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.engine.action.PassTurnToNextPlayerGameAction;
import com.xzymon.scg.engine.action.PullNextCardGameAction;
import com.xzymon.scg.global.GlobalNames;
import com.xzymon.scg.websockets.GameDisplaySessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ClientMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

	private static final String SESSION_ID_MSG_PREFIX = "[sessionId: %1$s] ";
	private static final String POTENTIAL_FRONTEND_SECURITY_FAULT_SUFFIX = " Might be a frontend security problem.";
	private static final String UNKNOWN_GAME_EVENT_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Unknown " + JsonKeys.GAME_EVENT.getKey() + "." + JsonKeys.NAME.getKey() + ": %2$s";
	private static final String PLAYER_TURN_SEQUENCE_BREACH_DETECTED_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Player tried to perform action, but has no right to perform it now!" + POTENTIAL_FRONTEND_SECURITY_FAULT_SUFFIX;
	private static final String SPECTATOR_ACTION_DETECTED_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Spectator tried to perform action, but spectators have no right to perform any action!" + POTENTIAL_FRONTEND_SECURITY_FAULT_SUFFIX;

	public static void handleClientMessage(ClientMessage clientMessage, GameDisplaySessionHandler sessionHandler, String sessionId) {
		GameManager gm = (GameManager) sessionHandler.getApplicationAttribute(GlobalNames.GAMES_REGISTER_NAME);
		Game currentGame = gm.getGameById(GlobalNames.DEVELOPMENT_DEFAULT_GAME_ID);
		if (defensiveCheckDoesInvocationComeFromPlayersSession(currentGame, sessionId)) {
			if (clientMessage.getGameEvent() != null) {
				if (defensiveCheckHasPlayerRightToPerformAction(currentGame, sessionId)) {
					Map<String, MessageBuilder> sessionIdToMessageBuilderMap = initSessionMessageBuilderMap(sessionHandler);
					switch (clientMessage.getGameEvent().getName()) {
						case "pullNextCard":
							new PullNextCardGameAction(sessionId, clientMessage.getGameEvent(), currentGame, sessionIdToMessageBuilderMap).execute();
							new PassTurnToNextPlayerGameAction(sessionId, clientMessage.getGameEvent(), currentGame, sessionIdToMessageBuilderMap).execute();
							//sessionHandler.getNextCardAndBroadcast(currentGame);
							break;
						default:
							LOGGER.error(String.format(UNKNOWN_GAME_EVENT_MSG_FORMAT, sessionId, clientMessage.getGameEvent().getName()));
					}
					extendByStateOfRegisteredPlayers(sessionIdToMessageBuilderMap, currentGame);
					sessionHandler.sendMessages(sessionIdToMessageBuilderMap);
				} else {
					LOGGER.info(String.format(PLAYER_TURN_SEQUENCE_BREACH_DETECTED_MSG_FORMAT, sessionId));
				}
			}
		} else {
			LOGGER.info(String.format(SPECTATOR_ACTION_DETECTED_MSG_FORMAT, sessionId));
		}
	}

	public static boolean defensiveCheckHasPlayerRightToPerformAction(Game game, String sessionId) {
		return null != sessionId && null != game.getActivePlayer() && sessionId.equals(game.getActivePlayer().getSessionId());
	}

	public static boolean defensiveCheckDoesInvocationComeFromPlayersSession(Game game, String sessionId) {
		return null != sessionId && null != game.getPlayerByBoundSessionId(sessionId);
	}

	public static Map<String, MessageBuilder> initSessionMessageBuilderMap(GameDisplaySessionHandler sessionHandler) {
		Map<String, MessageBuilder> result = new HashMap<>();
		for (String sessionId : sessionHandler.getSessionIds()) {
			result.put(sessionId, MessageBuilder.newInstance());
		}
		return result;
	}

	public static void extendByStateOfRegisteredPlayers(Map<String, MessageBuilder> messageBuildersMap, Game game) {
		PlayerListBuilder registeredPlayers = MessageHelper.registeredPlayersFromGame(game);
		for (Map.Entry<String, MessageBuilder> entry : messageBuildersMap.entrySet()) {
			entry.getValue().registeredPlayers(registeredPlayers);
		}
	}
}
