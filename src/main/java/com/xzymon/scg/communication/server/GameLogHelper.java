package com.xzymon.scg.communication.server;

import com.xzymon.scg.domain.Card;
import com.xzymon.scg.domain.CardCategory;
import com.xzymon.scg.domain.Player;

import java.util.Locale;

public class GameLogHelper {
	public static final String CONTENT_STANDARD_PLAY_CARD_ACTION_MSG_FORMAT = "$player plays :%1$s-category-card$playedCardDescription and gains $scoreChange";
	public static final String NAME_PLAYER = "player";
	public static final String NAME_PLAYED_CARD_DESCRIPTION = "playedCardDescription";
	public static final String NAME_SCORE_CHANGE = "scoreChange";
	public static final String CSS_CLASS_BOLD = "bold";
	public static final String CSS_CLASS_1ST = "player1st";
	public static final String CSS_CLASS_2ND = "player2nd";
	public static final String CSS_CLASS_3RD = "player3rd";
	public static final String CSS_CLASS_4TH = "player4th";
	public static final String CSS_CLASS_5TH = "player5th";
	public static final String CSS_CLASS_SCORE_CHANGE_POSITIVE = "scoreChangePositive";
	public static final String CSS_CLASS_SCORE_CHANGE_ZERO = "scoreChangeZero";
	public static final String CSS_CLASS_SCORE_CHANGE_NEGATIVE = "scoreChangeNegative";
	public static final String COLOR_ONLY_FULL_RED_HEX = "#ff0000";
	public static final String COLOR_ONLY_FULL_GREEN_HEX = "#00ff00";
	public static final String COLOR_ONLY_FULL_BLUE_HEX = "#0000ff";
	public static final String COLOR_YELLOW_HEX = "#ffff00";
	public static final String COLOR_BLACK_HEX = "#000000";
	public static final String COLOR_POSITIVE_SCORE_GREEN_HEX = "#0f7f12";
	public static final String COLOR_NEGATIVE_SCORE_RED_HEX = "#990000";

	public static String contentStandardPlayCardAction(CardCategory cardCategory) {
		return String.format(CONTENT_STANDARD_PLAY_CARD_ACTION_MSG_FORMAT, cardCategory.name().toLowerCase(Locale.ROOT));
	}

	public static GameLogVariableBuilder varPlayedCardDescription(Card playedCard) {
		String color = COLOR_BLACK_HEX;
		switch (playedCard.getCategory()) {
			case BLACK -> color = COLOR_BLACK_HEX;
			case BLUE -> color = COLOR_ONLY_FULL_BLUE_HEX;
			case GREEN -> color = COLOR_ONLY_FULL_GREEN_HEX;
			case YELLOW -> color = COLOR_YELLOW_HEX;
			case RED -> color = COLOR_ONLY_FULL_RED_HEX;
		}
		return GameLogVariableBuilder.newInstance()
				       .name(GameLogHelper.NAME_PLAYED_CARD_DESCRIPTION)
				       .value(playedCard.getDescription())
				       .cssClass(CSS_CLASS_BOLD)
				       .color(color);
	}

	public static GameLogVariableBuilder varPlayer(Player player) {
		StringBuilder cssClassSB = new StringBuilder();
		cssClassSB.append(CSS_CLASS_BOLD);
		if ("First player".equals(player.getName())) {
			cssClassSB.append(" ").append(CSS_CLASS_1ST);
		} else if ("Second player".equals(player.getName())) {
				cssClassSB.append(" ").append(CSS_CLASS_2ND);
		} else if ("Third player".equals(player.getName())) {
			cssClassSB.append(" ").append(CSS_CLASS_3RD);
		}
		return GameLogVariableBuilder.newInstance()
				       .name(NAME_PLAYER)
				       .value(player.getName())
				       .cssClass(cssClassSB.toString());
	}

	public static GameLogVariableBuilder varScoreChange(Integer scoreChange) {
		String cssClass;
		String scoreChangeText = String.format("%1$d", scoreChange);
		if (scoreChange == 0) {
			cssClass = CSS_CLASS_SCORE_CHANGE_ZERO;
		} else {
			if (scoreChange > 0) {
				cssClass = CSS_CLASS_SCORE_CHANGE_POSITIVE;
				scoreChangeText = String.format("+%1$d", scoreChange);
			} else {
				cssClass = CSS_CLASS_SCORE_CHANGE_NEGATIVE;
			}
		}
		return GameLogVariableBuilder.newInstance()
				.name(GameLogHelper.NAME_SCORE_CHANGE)
				.value(scoreChangeText)
				.cssClass(cssClass);
	}
}
