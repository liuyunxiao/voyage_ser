package com.voyage.enums;

/**
 * 日常任务类型
 * */
public enum DailyTaskType {
	LOGIN(1), //登录
	PVP(2), //pvp
	PVE(3), //PVE
	BORROW(4), //借公主
	REAP_FARM(5), //收农场
	REAP_MINE(6), //收矿石
	APPLY_FRIEND(7), //添加好友
	GIVE_GOLD(8), //赠送金币
	GIVE_GIFT(9);//赠送礼物

	private int v;

	public int getV() {
		return this.v;
	}

	private DailyTaskType(int v) {
		this.v = v;
	}

	public static DailyTaskType parse(int value) {
		DailyTaskType[] enums = DailyTaskType.values();
		for (DailyTaskType tempEnum : enums) {
			if (tempEnum.getV() == value) {
				return tempEnum;
			}
		}
		return null;
	}
}
