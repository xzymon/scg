package com.xzymon.scg.communication.client;

public enum JsonKeys {
	PATH_ROOT("$"),
	SOME_ARRAY_ELEMENT("[]"),
	ARRAY_ELEMENT_0("[0]"),
	ARRAY_ELEMENT_1("[1]"),
	ARRAY_ELEMENT_2("[2]"),
	ARRAY_ELEMENT_3("[3]"),
	ARRAY_ELEMENT_4("[4]"),
	ARRAY_ELEMENT_5("[5]"),
	ARRAY_ELEMENT_6("[6]"),
	ARRAY_ELEMENT_7("[7]"),
	ARRAY_ELEMENT_8("[8]"),
	ARRAY_ELEMENT_9("[9]"),
	GAME_EVENT("gameEvent"),
	NAME("name"),
	CID("cid");


	private String key;

	public String getKey() {
		return key;
	}

	JsonKeys(String key) {
		this.key = key;
	}
}
