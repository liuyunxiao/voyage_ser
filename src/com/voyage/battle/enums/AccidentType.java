package com.voyage.battle.enums;

public enum AccidentType {
	MISS(0),//未命中
	HIT(1), //命中
	BLOCK(2), //格挡
	CRIT(3);//暴击
	private int v;

	public int getV() {
		return v;
	}

	private AccidentType(int v) {
		this.v = v;
	}
}
