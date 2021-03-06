var constants = new Constants();
const gameLog = new GameLog();

var socket = new WebSocket("ws://localhost:8081/scg/gameDisplay");
socket.onmessage = onMessage;

let globalMessage = null;

document.body.addEventListener('click', GUI.playCardFromHand);

function onMessage(event) {
	console.log('onMessage');
	console.log(event.data);
	let jsonMsg = JSON.parse(event.data);
	console.log(jsonMsg);
	globalMessage = jsonMsg;
	if (jsonMsg.registeredPlayers != null) {
		let regPlayers = jsonMsg.registeredPlayers;
		let existingPlayerEl = null;
		for (let rP = 0; rP < jsonMsg.registeredPlayers.length; rP++) {
			existingPlayerEl = document.getElementById(`player${regPlayers[rP].sessionId}`);
			if (existingPlayerEl != null) {
				existingPlayerEl.innerText = registeredPlayerText(regPlayers[rP]);
				setElementClassFromRegisteredPlayer(existingPlayerEl, regPlayers[rP]);
			} else {
				createNewPlayerNode(regPlayers[rP]);
			}
		}
	}

	if (jsonMsg.newPlayer != null) {
		createNewPlayerNode(jsonMsg.newPlayer);
	}

	if (jsonMsg.removedPlayer != null) {
		removePlayer(jsonMsg.removedPlayer);
	}

	if (jsonMsg.topmostCard != null) {
		if (jsonMsg.topmostCard.category != null) {
			let tcCat = jsonMsg.topmostCard.category;
			let topmostCardDiv = document.getElementById("topmostCard");
			setCardClassBasedOnCategory(topmostCardDiv, tcCat);
		}
	}

	if (jsonMsg.frontState != null) {
		let pullNextCardBtn = document.getElementById('pullNextCardBtn');
		if (jsonMsg.frontState.activePullCard === true) {
			pullNextCardBtn.disabled = false;
		} else {
			pullNextCardBtn.disabled = true;
		}

		if (jsonMsg.frontState.activeHand === true) {
			GUI.activateHand(document.getElementById('handDiv'));
		} else {
			GUI.deactivateHand(document.getElementById('handDiv'));
		}

		if (jsonMsg.frontState.score != null) {
			const playerScore = Number.parseInt(jsonMsg.frontState.score);
			document.getElementById('score').innerText = playerScore;
		}

		if (jsonMsg.frontState.name != null) {
			document.getElementById('headerName').innerText = jsonMsg.frontState.name;
		}
	}

	if (jsonMsg.playerHand != null) {
		let playerHand = jsonMsg.playerHand;
		const handDiv = document.getElementById('handDiv');
		GUI.updateHand(handDiv, playerHand);
	}

	if (jsonMsg.newGameLogMessages != null) {
		let newGameLogMessages = jsonMsg.newGameLogMessages;
		const gameLogDomElement = document.getElementById('game-log-container');
		gameLog.createNewGameLogMessages(gameLogDomElement, newGameLogMessages);
	}
}

function invokeCreate() {
	const newGameLogMessages=
		[
			{
				"content": "$player plays :blue-category-card$playedCardDescription and gains $scoreChange",
				"vars": [
					{"name":"player","value":"First player","class":"initiator","color":"#00ff00"},
					{"name":"playedCardDescription","value":"1st blue card","class":"initiator","color":"#0000ff"},
					{"name":"scoreChange","value":"15","class":"scoreChangePositive","color":"#0f7f12"},
				]
			}
			//{"player": {"name":"First player", "color": "#00ff00"},"topmostCard":{"description":"1st blue card","id":2001,"category":"BLUE"},"playedCard":{"description":"1st blue card","id":2001,"category":"BLUE"},"score":{"change":15,"result":70}},
			//{"player": {"name":"Second player", "color": "#ff0000"},"topmostCard":{"description":"1st blue card","id":2001,"category":"BLUE"},"playedCard":{"description":"3rd green card","id":3003,"category":"GREEN"},"score":{"change":15,"result":70}},
			//{"player": {"name":"First player", "color": "#00ff00"},"topmostCard":{"description":"3rd green card","id":3003,"category":"GREEN"},"playedCard":{"description":"4th blue card","id":2004,"category":"BLUE"},"score":{"change":-15,"result":70}},
		]
	const gameLogDomElement = document.getElementById('game-log-container');
	gameLog.createNewGameLogMessages(gameLogDomElement, newGameLogMessages);
}

function setCardClassBasedOnCategory(cardDiv, category) {
	if (category == "RED") {
		setCardColor(cardDiv, Constants.cardRed());
	}
	if (category == "YELLOW") {
		setCardColor(cardDiv, Constants.cardYellow());
	}
	if (category == "GREEN") {
		setCardColor(cardDiv, Constants.cardGreen());
	}
	if (category == "BLUE") {
		setCardColor(cardDiv, Constants.cardBlue());
	}
	if (category == "BLACK") {
		setCardColor(cardDiv,Constants.cardBlack());
	}
}

function createNewPlayerNode(player) {
	console.log("Inside createNewPlayerNode");
	console.log(player);

	const li = document.createElement('li');

// Dodanie klasy, id i atrybutu title
	setElementClassFromRegisteredPlayer(li, player);
	li.id = `player${player.sessionId}`;
	li.setAttribute('title', 'New Item');

// Tekst do ??rodka wk??ada si?? poprzez w??o??enie w??z??a typu #text
	li.appendChild(document.createTextNode(registeredPlayerText(player)));

// pod????czenie nowo utworzonego elementu do istniej??cego drzewa dokumentu
	document.querySelector('ul.collection').appendChild(li);
}

function registeredPlayerText(player) {
	return `[id = ${player.sessionId}] ${player.name} -- score: ${player.score}`;
}

function setElementClassFromRegisteredPlayer(element, registeredPlayer) {
	if (registeredPlayer.active === true) {
		element.className = 'activePlayer';
	} else {
		element.className = 'player';
	}
}

function removePlayer(player) {
	console.log("Inside removePlayer");
	console.log(player);

	if (player.sessionId != null) {
		var playerId = `player${player.sessionId}`;
		const liPlayerToRemove = document.getElementById(playerId);
		const parent = liPlayerToRemove.parentNode;
		parent.removeChild(liPlayerToRemove);
		console.log("removePlayer : removed");
	}
}

function setCardColor(cardColor) {
	var content = document.getElementById("card");
	content.setAttribute('class', cardColor);
}

function setCardColor(card, color) {
	card.setAttribute('class', color);
}



//------------------------------

function pullNextCard() {
	var message = {
		"gameEvent" : {
			"name" : "pullNextCard"
		}
	};
	socket.send(JSON.stringify(message));
}