class GameLog {
    constructor() {
        this.symbolSourceMap = this.getSymbolSourceMap();
    }

    getSymbolSourceMap() {
        const map = new Map();
        map.set('red-category-card','category-red.jpg');
        map.set('yellow-category-card','category-yellow.jpg');
        map.set('green-category-card','category-green.jpg');
        map.set('blue-category-card','category-blue.jpg');
        map.set('black-category-card','category-black.jpg');
        return map;
    }

    getSourceForSymbol(symbol) {
        return this.symbolSourceMap.get(symbol);
    }

    createNewGameLogMessages(gameLogDomElement, newGameLogMessages) {
        console.log('incomingMessage:');
        console.log(newGameLogMessages);
        console.log(gameLogDomElement);

        if (newGameLogMessages != null) {
            newGameLogMessages.forEach(newMessage => {
                this.createNewGameLogMessage(gameLogDomElement, newMessage);
            });
        }
    }

    createNewGameLogMessage(gameLogDomElement, newMessage) {
        if (gameLogDomElement != null && newMessage != null) {
            console.log("Inside createNewGameLogMessage");
            console.log(newMessage);

            const newMessageDiv = document.createElement('div');
            newMessageDiv.className = 'msgContainer';
            newMessageDiv.appendChild(GameLog.createCurrentTimestampDiv());
            newMessageDiv.appendChild(this.interpretMessage(newMessage));

            gameLogDomElement.appendChild(newMessageDiv);
        }
    }

    static createCurrentTimestampDiv() {
        const time = new Date();
        const div = document.createElement('div');
        div.className = 'msgTimestamp';
        div.innerText = `${this.time2pos(time.getHours())}:${this.time2pos(time.getMinutes())}:${this.time2pos(time.getSeconds())}`;
        return div;
    }

    static time2pos(timeElement) {
        if (timeElement < 10) {
            return `0${timeElement}`;
        }
        return timeElement;
    }

    createMessageBodyDiv(message) {
        const div = document.createElement('div');
        div.className = 'msgBody';
        if (message != null) {
            if (message.player != null) {
                div.appendChild(GameLog.createInlineSpanForNotEmptyText(message.player.name, 'initiator', message.player.color));
            }
            if (message.playedCard != null) {
                if (message.playedCard.category != null) {
                    div.appendChild(GameLog.createInlineSpanForNotEmptyText(' plays ', null, null));
                    div.appendChild(GameLog.createInlineImageForCardCategory(message.playedCard.category));
                    div.appendChild(GameLog.createInlineSpanForNotEmptyText(message.playedCard.description, null, null));
                }
            }
            if (message.score != null) {
                if (message.score.change != null && message.score.change != 0) {
                    div.appendChild(GameLog.createInlineSpanForNotEmptyText(' and gains '));
                    if (message.score.change > 0) {
                        div.appendChild(GameLog.createInlineSpanForNotEmptyText(`+${message.score.change}`, 'scoreChangePositive', null));
                    } else {
                        div.appendChild(GameLog.createInlineSpanForNotEmptyText(`${message.score.change}`, 'scoreChangeNegative', null));
                    }
                }
                div.appendChild(GameLog.createInlineSpanForNotEmptyText(' and this is a very long message to test text wrapping', null, null));
            }
        }
        return div;
    }

    interpretMessagePart(messagePart, message) {
        if (messagePart.length > 0) {
            if ((messagePart[0] == '$' || messagePart[0] == ':') && messagePart.length > 1) {
                const esseName = messagePart.substring(1);
                if (messagePart[0] == '$') {
                    if (message.vars != null) {
                        for (let i = 0; i < message.vars.length; i++) {
                            if (message.vars[i].name == esseName) {
                                return GameLog.createInlineSpanForNotEmptyText(message.vars[i].value, message.vars[i].cssClass, message.vars[i].color);
                            }
                        }
                    }
                } else {
                    const source = this.getSourceForSymbol(esseName);
                    if (source != null) {
                        return this.createInlineImageForSymbol(esseName);
                    }
                }
            }
            return GameLog.createInlineSpanForNotEmptyText(messagePart);
        }
        return null;
    }

    interpretMessage(message) {
        const div = document.createElement('div');
        div.className = 'msgBody';
        if (message != null) {
            if (message.content != null && message.content.length > 0) {
                const msgMap = this.createMapOfMessage(message.content);
                for (let i = 0; i < msgMap.size; i++) {
                    let element = this.interpretMessagePart(msgMap.get(i), message);
                    if (element != null) {
                        console.log(`[${i}] : adding element: `);
                        console.log(element);
                        div.appendChild(element);
                    }
                }
            }
        }
        return div;
    }

    static createInlineImageForCardCategory(cardCategory) {
        if (cardCategory != null) {
            const imageSrcPath = 'http://localhost/dev/scg/img';
            let imageName;
            let imageTitle;
            if (cardCategory === "RED") {
                imageName = 'category-red.jpg';
                imageTitle = ':red-category-card';
            }
            if (cardCategory === "YELLOW") {
                imageName = 'category-yellow.jpg';
                imageTitle = ':yellow-category-card';
            }
            if (cardCategory === "GREEN") {
                imageName = 'category-green.jpg';
                imageTitle = ':green-category-card';
            }
            if (cardCategory === "BLUE") {
                imageName = 'category-blue.jpg';
                imageTitle = ':blue-category-card';
            }
            if (cardCategory === "BLACK") {
                imageName = 'category-black.jpg';
                imageTitle = ':black-category-card';
            }
            const imageSrc = `${imageSrcPath}/${imageName}`;
            const newImage = document.createElement('img');
            newImage.src = imageSrc;
            newImage.title = imageTitle;
            newImage.width = 20;
            newImage.height = 20;
            newImage.setAttribute('style', 'cursor: pointer; vertical-align: top;');
            return newImage;
        }
        return null;
    }

    createInlineImageForSymbol(symbol) {
        const imageSrcPath = 'http://localhost/dev/scg/img';
        const imageTitle = `:${symbol}`;
        const imageName = this.getSourceForSymbol(symbol);
        const imageSrc = `${imageSrcPath}/${imageName}`;
        const newImage = document.createElement('img');
        newImage.src = imageSrc;
        newImage.title = imageTitle;
        newImage.width = 20;
        newImage.height = 20;
        newImage.setAttribute('style', 'cursor: pointer; vertical-align: top;');
        return newImage;
    }

    static createInlineSpanForNotEmptyText(text, classForElement, color) {
        if (text != null) {
            const span = document.createElement('span');
            if (classForElement != null) {
                span.className = classForElement;
            }
            if (color != null) {
                span.style.color = color;
            }
            span.innerText = text;
            return span;
        }
        return null;
    }

    createMapOfMessage(template) {
        const msgMap = new Map();
        let mapIterator = 0;
        let symbolStart = null;
        let textStart = null;
        let variableStart = null;
        for (let i = 0; i < template.length; i++) {
            if (template[i] === '$') {
                if (variableStart != null) {
                    msgMap.set(mapIterator++, template.substring(variableStart, i));
                }
                if (symbolStart != null) {
                    msgMap.set(mapIterator++, template.substring(symbolStart, i));
                    symbolStart = null;
                }
                if (textStart != null) {
                    msgMap.set(mapIterator++, template.substring(textStart, i));
                    textStart = null;
                }
                variableStart = i;
            }
            if (template[i] === ':') {
                if (variableStart != null) {
                    msgMap.set(mapIterator++, template.substring(variableStart, i));
                    variableStart = null;
                }
                if (symbolStart != null) {
                    msgMap.set(mapIterator++, template.substring(symbolStart, i));
                }
                if (textStart != null) {
                    msgMap.set(mapIterator++, template.substring(textStart, i));
                    textStart = null;
                }
                symbolStart = i;
            }
            if (template[i] === ' ') {
                if (variableStart != null) {
                    msgMap.set(mapIterator++, template.substring(variableStart, i));
                    variableStart = null;
                }
                if (symbolStart != null) {
                    msgMap.set(mapIterator++, template.substring(symbolStart, i));
                    symbolStart = null;
                }
                if (textStart == null) {
                    textStart = i;
                }
            }
        }
        if (variableStart != null) {
            msgMap.set(mapIterator++, template.substring(variableStart));
        }
        if (symbolStart != null) {
            msgMap.set(mapIterator++, template.substring(symbolStart));
        }
        if (textStart != null) {
            msgMap.set(mapIterator++, template.substring(textStart));
        }
        return msgMap;
    }
}