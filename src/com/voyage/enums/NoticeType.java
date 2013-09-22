package com.voyage.enums;

/**
 * 系统公告类别
 * */
public enum NoticeType {
	ACTIVITY(1), //活动
	MAINTAIN(2), //维护
	REDRESS(3), //补偿
	UPDATE(4);//更新
	private int v;

	public int getV() {
		return this.v;
	}

	private NoticeType(int v) {
		this.v = v;
	}

	public static NoticeType parse(int value) {
		NoticeType[] enums = NoticeType.values();
		for (NoticeType tempEnum : enums) {
			if (tempEnum.getV() == value) {
				return tempEnum;
			}
		}
		return null;
	}
}
