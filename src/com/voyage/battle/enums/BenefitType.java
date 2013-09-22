package com.voyage.battle.enums;

/**
 * 有益状态
 * */
public enum BenefitType {
	COMMON(0), //普通
	GAINFUL(1), //增益
	HARMFUL(2);//减益

	private int v;

	public int getV() {
		return v;
	}

	private BenefitType(int v) {
		this.v = v;
	}
}
