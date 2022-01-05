package com.xzymon.scg.communication.client;

import com.xzymon.scg.communication.client.constants.GameEventName;

public class GameEvent {
	GameEventName name;
	Long cid;

	public GameEventName getName() {
		return name;
	}

	public void setName(GameEventName name) {
		this.name = name;
	}

	public void setName(String name) {
		GameEventName[] names = GameEventName.values();
		for (GameEventName gen : names) {
			if (gen.getValue().equals(name)) {
				this.name = gen;
				return;
			}
		}
		this.name = null;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	@Override
	public String toString() {
		return "GameEvent{" +
				       "name=" + name +
				       ", cid=" + cid +
				       '}';
	}
}
