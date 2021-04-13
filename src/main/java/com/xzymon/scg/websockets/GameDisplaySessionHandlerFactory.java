package com.xzymon.scg.websockets;

public class GameDisplaySessionHandlerFactory {

	private static GameDisplaySessionHandler handler;

	public static GameDisplaySessionHandler getHandler() {
		if (handler == null) {
			handler = new GameDisplaySessionHandler();
		}
		return handler;
	}

}
