package com.xzymon.scg.engine;

import com.xzymon.scg.communication.client.ClientMessage;
import com.xzymon.scg.communication.client.JsonKeys;
import com.xzymon.scg.communication.server.FrontStateBuilder;
import com.xzymon.scg.communication.server.MessageBuilder;
import com.xzymon.scg.communication.server.MessageHelper;
import com.xzymon.scg.communication.server.PlayerListBuilder;
import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.CardCategory;
import com.xzymon.scg.domain.Game;
import com.xzymon.scg.domain.Player;
import com.xzymon.scg.engine.action.*;
import com.xzymon.scg.engine.action.input.GameActionInput;
import com.xzymon.scg.global.GlobalNames;
import com.xzymon.scg.websockets.GameDisplaySessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ClientMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

	private static final String SESSION_ID_MSG_PREFIX = "[sessionId: %1$s] ";
	private static final String POTENTIAL_FRONTEND_SECURITY_FAULT_SUFFIX = " Might be a frontend security problem.";

	private static final String UNKNOWN_GAME_EVENT_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Unknown " + JsonKeys.GAME_EVENT.getKey() + "." + JsonKeys.NAME.getKey() + ": %2$s";
	private static final String INVALID_PHASE_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Client request invalid. Player is not in PLAY_CARD phase!";
	private static final String INVALID_CARD_PLAYED_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Client request invalid. There's no such card { cid: %2$s } on player's hand!" + POTENTIAL_FRONTEND_SECURITY_FAULT_SUFFIX;
	private static final String PLAYER_TURN_SEQUENCE_BREACH_DETECTED_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Player tried to perform action, but has no right to perform it now!" + POTENTIAL_FRONTEND_SECURITY_FAULT_SUFFIX;
	private static final String SPECTATOR_ACTION_DETECTED_MSG_FORMAT = SESSION_ID_MSG_PREFIX + "Spectator tried to perform action, but spectators have no right to perform any action!" + POTENTIAL_FRONTEND_SECURITY_FAULT_SUFFIX;

	public static void handleClientMessage(ClientMessage clientMessage, GameDisplaySessionHandler sessionHandler, String sessionId) {
		GameManager gm = (GameManager) sessionHandler.getApplicationAttribute(GlobalNames.GAMES_REGISTER_NAME);
		Game currentGame = gm.getGameById(GlobalNames.DEVELOPMENT_DEFAULT_GAME_ID);
		if (defensiveCheckDoesInvocationComeFromPlayersSession(currentGame, sessionId)) {
			if (clientMessage.getGameEvent() != null) {
				if (defensiveCheckHasPlayerRightToPerformAction(currentGame, sessionId)) {
					Player activePlayer = currentGame.getActivePlayer();
					Map<String, MessageBuilder> sessionIdToMessageBuilderMap = initSessionMessageBuilderMap(sessionHandler);
					LOGGER.info("Game event: " + clientMessage.getGameEvent());

					switch (clientMessage.getGameEvent().getName()) {
						case PULL_NEXT_CARD:
							new PullNextCardAndActivateHandGameAction(sessionId, clientMessage.getGameEvent(), currentGame, sessionIdToMessageBuilderMap).execute();
							//new IncrementPlayerScoreGameAction(sessionId, clientMessage.getGameEvent(), currentGame, sessionIdToMessageBuilderMap).execute();
							break;
						case PLAY_CARD:
							if (isInPlayCardPhase(activePlayer)) {
								Long requestedCardId = clientMessage.getGameEvent().getCid();
								if (defensiveCheckDoesPlayerHaveCardOnHand(requestedCardId, activePlayer)) {
									Card requestedCard = activePlayer.getCardOnHandById(requestedCardId);
									Card topmostCardBeforePlayerAction = currentGame.getTopmostCard();
									GameActionInput gameActionInput = new GameActionInput(requestedCard, topmostCardBeforePlayerAction, sessionId, clientMessage.getGameEvent(), currentGame, sessionIdToMessageBuilderMap);
									switch (requestedCard.getCategory()) {
										case BLACK -> categoryCardConsumer.accept(gameActionInput, blackCategoryCardScoreGameActionConsumer);
										case BLUE -> categoryCardConsumer.accept(gameActionInput, blueCategoryCardScoreGameActionConsumer);
										case GREEN -> categoryCardConsumer.accept(gameActionInput, greenCategoryCardScoreGameActionConsumer);
										case YELLOW -> categoryCardConsumer.accept(gameActionInput, yellowCategoryCardScoreGameActionConsumer);
										case RED -> categoryCardConsumer.accept(gameActionInput, redCategoryCardScoreGameActionConsumer);
									}
								} else {
									LOGGER.info(String.format(INVALID_CARD_PLAYED_MSG_FORMAT, sessionId, requestedCardId));
									// akcja jest nieprawidlowa, a gracz ma zablokowana reke - trzeba odblokowac graczowi reke
									MessageBuilder activePlayerMB = sessionIdToMessageBuilderMap.get(sessionId);
									activePlayerMB.frontState(MessageHelper.frontStateActive(false, true));
									sessionHandler.sendToSessionById(activePlayerMB, sessionId);
									return;
								}
							} else {
								LOGGER.error(String.format(INVALID_PHASE_MSG_FORMAT, sessionId));
							}
							break;
						default:
							LOGGER.error(String.format(UNKNOWN_GAME_EVENT_MSG_FORMAT, sessionId, clientMessage.getGameEvent().getName()));
					}
					extendByFrontStateOfPlayers(sessionIdToMessageBuilderMap, currentGame);
					extendByStateOfRegisteredPlayers(sessionIdToMessageBuilderMap, currentGame);
					sessionHandler.sendMessages(sessionIdToMessageBuilderMap);
				} else {
					LOGGER.error(String.format(PLAYER_TURN_SEQUENCE_BREACH_DETECTED_MSG_FORMAT, sessionId));
				}
			}
		} else {
			LOGGER.error(String.format(SPECTATOR_ACTION_DETECTED_MSG_FORMAT, sessionId));
		}
	}

	public static boolean defensiveCheckHasPlayerRightToPerformAction(Game game, String sessionId) {
		return null != sessionId && null != game.getActivePlayer() && sessionId.equals(game.getActivePlayer().getSessionId());
	}

	public static boolean defensiveCheckDoesInvocationComeFromPlayersSession(Game game, String sessionId) {
		return null != sessionId && null != game.getPlayerByBoundSessionId(sessionId);
	}

	public static boolean defensiveCheckDoesPlayerHaveCardOnHand(Long cardId, Player activePlayer) {
		return null != cardId && null != activePlayer && activePlayer.hasCardOnHand(cardId);
	}

	public static boolean isInPlayCardPhase(Player activePlayer) {
		return null != activePlayer && activePlayer.isActive() && !activePlayer.canPullCard();
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

	public static void extendByFrontStateOfPlayers(Map<String, MessageBuilder> messageBuildersMap, Game game) {
		for (Map.Entry<String, MessageBuilder> entry : messageBuildersMap.entrySet()) {
			Player player = game.getPlayerByBoundSessionId(entry.getKey());
			if (null != player) {
				entry.getValue().frontState(MessageHelper.frontStateFromPlayer(player));
			}
		}
	}

	public static BiConsumer<GameActionInput, Consumer<GameActionInput>> categoryCardConsumer = (gai, categoryCardScoreGameActionConsumer) -> {
		new RemoveCardFromHandGameAction(gai.playedCard(), gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
		new DiscardTopmostCardGameAction(gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
		categoryCardScoreGameActionConsumer.accept(gai);
		new SetTopmostCardGameAction(gai.playedCard(), gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
		new PassTurnToNextPlayerGameAction(gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
	};

	public static Consumer<GameActionInput> blackCategoryCardScoreGameActionConsumer = gai -> {
		new BlackCategoryCardScoreGameAction(gai.cardToBeCovered(), gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
	};

	public static Consumer<GameActionInput> blueCategoryCardScoreGameActionConsumer = gai -> {
		new BlueCategoryCardScoreGameAction(gai.cardToBeCovered(), gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
	};

	public static Consumer<GameActionInput> greenCategoryCardScoreGameActionConsumer = gai -> {
		new GreenCategoryCardScoreGameAction(gai.cardToBeCovered(), gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
	};

	public static Consumer<GameActionInput> yellowCategoryCardScoreGameActionConsumer = gai -> {
		new YellowCategoryCardScoreGameAction(gai.cardToBeCovered(), gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
	};

	public static Consumer<GameActionInput> redCategoryCardScoreGameActionConsumer = gai -> {
		new RedCategoryCardScoreGameAction(gai.cardToBeCovered(), gai.sessionId(), gai.gameEvent(), gai.game(), gai.sessionIdToMessageBuilderMap()).execute();
	};
}
