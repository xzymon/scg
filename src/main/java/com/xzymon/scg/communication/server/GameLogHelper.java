package com.xzymon.scg.communication.server;

import com.xzymon.scg.domain.CardCategory;

import java.util.Locale;

public class GameLogHelper {
	public static final String CONTENT_STANDARD_PLAY_CARD_ACTION_MSG_FORMAT = "$player plays :%1$s-category-card$playedCardDescription and gains $scoreChange";
	public static final String VAR_NAME_PLAYER = "player";
	public static final String VAR_NAME_PLAY_CARD_DESCRIPTION = "playedCardDescription";
	public static final String VAR_NAME_SCORE_CHANGE = "scoreChange";
	public static final String VAR_CSS_CLASS_INITIATOR = "initiator";
	public static final String VAR_CSS_CLASS_SCORE_CHANGE_POSITIVE = "scoreChangePositive";
	public static final String VAR_CSS_CLASS_SCORE_CHANGE_ZERO = "scoreChangeZero";
	public static final String VAR_CSS_CLASS_SCORE_CHANGE_NEGATIVE = "scoreChangeNegative";
	public static final String VAR_COLOR_ONLY_FULL_RED_HEX = "#ff0000";
	public static final String VAR_COLOR_ONLY_FULL_GREEN_HEX = "#00ff00";
	public static final String VAR_COLOR_ONLY_FULL_BLUE_HEX = "#0000ff";
	public static final String VAR_COLOR_POSITIVE_SCORE_GREEN_HEX = "#0f7f12";
	public static final String VAR_COLOR_NEGATIVE_SCORE_RED_HEX = "#990000";

	public static String contentStandardPlayCardAction(CardCategory cardCategory) {
		return String.format(CONTENT_STANDARD_PLAY_CARD_ACTION_MSG_FORMAT, cardCategory.name().toLowerCase(Locale.ROOT));
	}

	public static GameLogVariableBuilder varScoreChange(Integer scoreChange) {
		String cssClass;
		String scoreChangeText = String.format("%1$d", scoreChange);
		if (scoreChange == 0) {
			cssClass = VAR_CSS_CLASS_SCORE_CHANGE_ZERO;
		} else {
			if (scoreChange > 0) {
				cssClass = VAR_CSS_CLASS_SCORE_CHANGE_POSITIVE;
				scoreChangeText = String.format("+%1$d", scoreChange);
			} else {
				cssClass = VAR_CSS_CLASS_SCORE_CHANGE_NEGATIVE;
			}
		}
		return GameLogVariableBuilder.newInstance()
				.name(GameLogHelper.VAR_NAME_SCORE_CHANGE)
				.value(scoreChangeText)
				.cssClass(cssClass);
	}
}
