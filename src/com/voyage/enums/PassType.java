package com.voyage.enums;

/**
 * 通过类别
 * */
public enum PassType {
	NOT_OPEN(0), //未开启
	NOT_PASS(1), //未通过
	PASSED(2);//已通过
	private int v;

	public int getV() {
		return this.v;
	}

	private PassType(int v) {
		this.v = v;
	}
}
