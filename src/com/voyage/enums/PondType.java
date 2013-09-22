package com.voyage.enums;

/**
 * 奖池类别
 * */
public enum PondType {
	BIG(1), //大奖池
	SMALL(2);//小奖池
	private int v;

	public int getV() {
		return this.v;
	}

	private PondType(int v) {
		this.v = v;
	}

}
