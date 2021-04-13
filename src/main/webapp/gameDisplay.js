var socket = new WebSocket("ws://localhost:8081/scg/gameDisplay");
socket.onmessage = onMessage;

function onMessage(event) {

	var card = JSON.parse(event.data);
	if (card.category == "RED") {
		setCardColor('redCard');
	}
	if (card.category == "YELLOW") {
		setCardColor('yellowCard');
	}
	if (card.category == "GREEN") {
		setCardColor('greenCard');
	}
	if (card.category == "BLUE") {
		setCardColor('blueCard');
	}
	if (card.category == "BLACK") {
		setCardColor('blackCard');
	}
}

function setCardColor(cardColor) {
	var content = document.getElementById("card");
	content.setAttribute('class', cardColor);
}

//------------------------------

function pullNextCard() {
	var message = {
		"gameEventType" : "pullNextCard"
	};
	socket.send(JSON.stringify(message));
}