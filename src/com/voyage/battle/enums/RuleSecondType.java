package com.voyage.battle.enums;

/**
 * 技能施放规则的第2位
 * */
public enum RuleSecondType {
	ROW(1), //单排
	COL(2), //单列
	ALL(3), //全体
	SELF(4), //自身
	TARGET(5), //物理目标
	SOURCE(6),//伤害来源
	LIVEROW(7);//第一不为空的排或列
	private int v;

	public int getV() {
		return v;
	}

	private RuleSecondType(int v) {
		this.v = v;
	}

	public static RuleSecondType parse(int value) {
		RuleSecondType[] enums = RuleSecondType.values();
		for (RuleSecondType tempEnum : enums) {
			if (tempEnum.getV() == value) {
				return tempEnum;
			}
		}
		return null;
	}
}
