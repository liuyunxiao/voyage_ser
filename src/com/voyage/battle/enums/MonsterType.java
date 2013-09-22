package com.voyage.battle.enums;

/**
 * 怪物类别
 * */
public enum MonsterType {
	DEFAULT(0); //默认
	private int v;

	public int getV() {
		return v;
	}

	private MonsterType(int v) {
		this.v = v;
	}
}
