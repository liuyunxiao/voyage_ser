package com.voyage.enums;

/**
 *物品类别
 * */
public enum GoodsType {
	ALL(0), //所有
	GIFT(1), //礼物
	MEDAL(2), //勋章  
	FARM(4), //农场
	MINE(8);//矿场
	private int v;

	public int getV() {
		return this.v;
	}

	private GoodsType(int v) {
		this.v = v;
	}

	public static GoodsType parse(int value) {
		GoodsType[] enums = GoodsType.values();
		for (GoodsType tempEnum : enums) {
			if (tempEnum.getV() == value) {
				return tempEnum;
			}
		}
		return null;
	}
}
