package com.voyage.battle.enums;

/**BUFF结算时机*/
public enum BuffCalcType {
	ROUND(0), //当前回合
	NOW(1); //瞬间
	private int v;

	public int getV() {
		return v;
	}

	private BuffCalcType(int v) {
		this.v = v;
	}
}
