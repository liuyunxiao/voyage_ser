package com.voyage.enums;

/**
 * 任务类型
 * */
public enum TaskType {
	RECHARGE_GOLD(1), //累计充值金币
	LEVEL_UP(2), //公主达到某级
	MAP_PASS(3), //某副本通关
	GOLD_PEAK(4), //仓库金币峰值
	LOGIN_DAYS(5), //累计登录天数
	RECHARGE_TIMES(6), //充值累计次数
	VIP_LEVEL_UP(7);//VIP达到某级等级

	private int v;

	public int getV() {
		return this.v;
	}

	private TaskType(int v) {
		this.v = v;
	}

	public static TaskType parse(int value) {
		TaskType[] enums = TaskType.values();
		for (TaskType tempEnum : enums) {
			if (tempEnum.getV() == value) {
				return tempEnum;
			}
		}
		return null;
	}
}
