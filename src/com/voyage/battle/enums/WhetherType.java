package com.voyage.battle.enums;

public enum WhetherType {
	NO(0), //否
	YES(1);//是
	private int v;

	public int getV() {
		return v;
	}

	private WhetherType(int v) {
		this.v = v;
	}
}
