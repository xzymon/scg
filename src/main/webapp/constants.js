class Constants {
	constructor() {
		this.handActivity = [Constants.handActive(), Constants.handInactive()];
		this.cardClickability = [Constants.cardClickable(), Constants.cardUnclickable()];
		this.cardCategoryColor = [Constants.cardRed(), Constants.cardYellow(), Constants.cardGreen(), Constants.cardBlue(), Constants.cardBlack()];
	}

	static handActivityPool() {
		return [Constants.handActive(), Constants.handInactive()];
	}

	static cardClickabilityPool() {
		return [Constants.cardClickable(), Constants.cardUnclickable()];
	}

	static categoryColorPool() {
		return [Constants.cardRed(), Constants.cardYellow(), Constants.cardGreen(), Constants.cardBlue(), Constants.cardBlack()];
	}

	static handActive() {
		return "handActive";
	}

	static handInactive() {
		return "handInactive";
	}

	static cardClickable() {
		return "cardClickable";
	}

	static cardUnclickable() {
		return "cardUnclickable";
	}

	static cardRed() {
		return "redCard";
	}

	static cardYellow() {
		return "yellowCard";
	}

	static cardGreen() {
		return "greenCard";
	}

	static cardBlue() {
		return "blueCard";
	}

	static cardBlack() {
		return "blackCard";
	}

	static cardOnHandPrefix() {
		return "cardOnHand";
	}
}