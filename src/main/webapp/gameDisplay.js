var socket = new WebSocket("ws://localhost:8081/scg/gameDisplay");
socket.onmessage = onMessage;

let globalMessage = null;

function onMessage(event) {
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
			if (tcCat == "RED") {
				setCardColor('redCard');
			}
			if (tcCat == "YELLOW") {
				setCardColor('yellowCard');
			}
			if (tcCat == "GREEN") {
				setCardColor('greenCard');
			}
			if (tcCat == "BLUE") {
				setCardColor('blueCard');
			}
			if (tcCat == "BLACK") {
				setCardColor('blackCard');
			}
		}
	}

	if (jsonMsg.frontState != null) {
		let pullNextCardBtn = document.getElementById('pullNextCardBtn');
		if (jsonMsg.frontState.active === true) {
			pullNextCardBtn.disabled = false;
		} else {
			pullNextCardBtn.disabled = true;
		}
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

// Tekst do środka wkłada się poprzez włożenie węzła typu #text
	li.appendChild(document.createTextNode(`[id = ${player.sessionId}] ${player.name}`));

// podłączenie nowo utworzonego elementu do istniejącego drzewa dokumentu
	document.querySelector('ul.collection').appendChild(li);
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

//------------------------------

function pullNextCard() {
	var message = {
		"gameEvent" : {
			"name" : "pullNextCard"
		}
	};
	socket.send(JSON.stringify(message));
}