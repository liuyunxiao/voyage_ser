package com.voyage.enums;

/**
 * 服务器状态
 * */
public enum ServerStateType {
	NEW(1), //新
	FLUENCY(2), //流畅
	FULL_HOUSE(3);//爆满
	private int v;

	public int getV() {
		return this.v;
	}

	private ServerStateType(int v) {
		this.v = v;
	}
}
