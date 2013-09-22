package com.voyage.battle.enums;

/**
 * 伤害类别
 * */
public enum DamageType {
	PHY(1), //物理伤害    
	SKI(2); //技能伤害
	private int v;

	public int getV() {
		return v;
	}

	private DamageType(int v) {
		this.v = v;
	}
}
