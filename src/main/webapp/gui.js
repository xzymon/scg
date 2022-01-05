class GUI {

	static playCardFromHand(e) {
		console.log('playCardFromHand:');
		console.log(e.target);
		if(e.target.parentElement.id === 'handDiv'){
			const handDiv = e.target.parentElement;
			GUI.deactivateHand(handDiv);
			console.log(e.target);
			const targetId = e.target.id;
			const expPrefixLen = Constants.cardOnHandPrefix().length;
			if (targetId.length > expPrefixLen && targetId.substring(0, expPrefixLen) === Constants.cardOnHandPrefix()) {
				const cardIdString = targetId.substring(expPrefixLen);
				const cardId = Number.parseInt(cardIdString);
				console.log(`cardId: ${cardId}`);
				var message = {
					"gameEvent" : {
						"name" : "playCard",
						"cid" : cardId
					}
				};
				console.log('message to be send:');
				console.log(message);
				socket.send(JSON.stringify(message));
			} else {
				console.log(`Problem with obtaining cardId - targetId was: ${targetId}`);
				GUI.activateHand(handDiv);
			}
		}
	}

	static deactivateHand(handDiv) {
		GUI.changeElementClass(handDiv, Constants.handActivityPool(), Constants.handInactive());
	}

	static activateHand(handDiv) {
		GUI.changeElementClass(handDiv, Constants.handActivityPool(), Constants.handActive());
	}

	static changeElementClass(domElement, classNameListToRemove, classNameToAdd) {
		let newClassList = [];
		console.log('changeElementClass : old class list:');
		console.log(domElement.classList);
		domElement.classList.forEach(className => {
			if (! GUI.arrayContains(classNameListToRemove, className)) {
				console.log(`pushing ${className}`);
				newClassList.push(className);
			}
		});
		let result = "";
		newClassList.forEach(className => result += `${className} `);
		result += `${classNameToAdd}`;
		//domElement. classList = result;
		domElement.setAttribute('class', result);
		console.log('changeElementClass : new class list:');
		console.log(domElement.classList);
	}

	static updateHand(handDomElement, backendHandResponse) {
		console.log('backendHandResponse:');
		console.log(backendHandResponse);
		console.log(handDomElement);

		if (backendHandResponse != null) {
			if (backendHandResponse.removedCards != null) {
				backendHandResponse.removedCards.forEach(card => {
					const removeElementId = `${Constants.cardOnHandPrefix()}${card.id}`;
					document.getElementById(removeElementId).remove();
				});
			}
			if (backendHandResponse.maintainedCards != null) {}
			if (backendHandResponse.newCards != null) {
				backendHandResponse.newCards.forEach(card => {
					GUI.createNewCardOnHand(handDomElement, card);
				});
			}
		}
	}

	static createNewCardOnHand(handDomElement, card) {
		if (handDomElement != null && card != null) {
			console.log("Inside createNewCardOnHand");
			console.log(card);

			const cardDiv = document.createElement('div');

			cardDiv.id = `${Constants.cardOnHandPrefix()}${card.id}`;
			setCardClassBasedOnCategory(cardDiv, card.category);

			handDomElement.appendChild(cardDiv);
		}
	}

	static arrayContains(array, element) {
		console.log('enter arrayContains');
		let contains = false;
		if (array != null && Array.isArray(array)) {
			for (let loop = 0; loop < array.length; loop++) {
				if (array[loop] === element) {
					contains = true;
				}
			}
		}
		return contains;
	}

	static getCardIdFromDomElement(domElement) {
		const domElementId = domElement.id;
		const expPrefixLen = Constants.cardOnHandPrefix().length;
		if (domElementId.length > expPrefixLen && el.substring(0, expPrefixLen) === Constants.cardOnHandPrefix()) {
			const elIdString = domElementId.substring(expPrefixLen);
			return Number.parseInt(elIdString);
		}
		return null;
	}
}