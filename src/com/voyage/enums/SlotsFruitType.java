package com.voyage.enums;

/**
 * 老虎机的水果类别（与数据库保持一致）
 * */
public enum SlotsFruitType {
	SF_1(1), //樱桃
	SF_2(2), //苹果
	SF_3(3), //橘子
	SF_4(4), //青柠
	SF_5(5), //铃铛
	SF_6(6), //西瓜
	SF_7(7), //双星
	SF_8(8), //77
	SF_9(9), //小bar
	SF_10(10), //大bar
	SF_11(11), //送灯
	SF_12(12), //大三元
	SF_13(13), //小三元
	SF_14(14), //开火车
	SF_15(15), //送黑灯
	SF_16(16), //小四喜
	SF_17(17);//大满贯
	private int v;

	public int getV() {
		return this.v;
	}

	private SlotsFruitType(int v) {
		this.v = v;
	}
}
