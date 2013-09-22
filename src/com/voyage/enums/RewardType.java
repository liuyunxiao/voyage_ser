package com.voyage.enums;

/**
 * 奖励类别
 * */
public enum RewardType {
	DEFAULT(0), //默认
	NEWCOMER(1), //新手教程
	CREATE_ROLE(2), //建号初始物品
	FIRST_CHARGE(3);//首充
	private int v;

	public int getV() {
		return this.v;
	}

	private RewardType(int v) {
		this.v = v;
	}
}
