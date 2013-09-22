package com.voyage.enums;

public enum MoneyType {
	GOLD(1);//金币
	private int v;

	public int getV() {
		return this.v;
	}

	private MoneyType(int v) {
		this.v = v;
	}

	public static MoneyType parse(int value) {
		MoneyType[] enums = MoneyType.values();
		for (MoneyType tempEnum : enums) {
			if (tempEnum.getV() == value) {
				return tempEnum;
			}
		}
		return null;
	}
}
