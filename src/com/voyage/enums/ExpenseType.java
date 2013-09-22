package com.voyage.enums;

/**
 * 消费类别
 * */
public enum ExpenseType {
	RECHARGE(1), //人民币充值
	MARKET(2);//使用金币购买
	private int v;

	public int getV() {
		return this.v;
	}

	private ExpenseType(int v) {
		this.v = v;
	}
}
