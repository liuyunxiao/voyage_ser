package com.voyage.battle.enums;

public enum SideType {
	LEFT(1), //左侧
	RIGHT(2);//右侧
	private int v;

	public int getV() {
		return v;
	}

	private SideType(int v) {
		this.v = v;
	}
}
