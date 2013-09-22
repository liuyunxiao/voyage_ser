package com.voyage.enums;

/**
 * 收入来源
 * */
public enum IncomeType {
	DEFAULT(0), //默认
	RECHARGE(1), //充值
	BATTLE(2), //战斗
	BREED(3), //养殖
	TASK(4), //任务
	GIVE(5), //赠送
	DAILY_TASK(6);//日常任务
	private int v;

	public int getV() {
		return this.v;
	}

	private IncomeType(int v) {
		this.v = v;
	}
}
