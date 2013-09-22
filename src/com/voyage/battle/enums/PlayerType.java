package com.voyage.battle.enums;

public enum PlayerType {
	ROLE(1), //公主
	CORPS(2), //兵种
	MONSTER(3);//怪物
	private int v;

	public int getV() {
		return v;
	}

	private PlayerType(int v) {
		this.v = v;
	}
}
