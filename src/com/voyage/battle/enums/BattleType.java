package com.voyage.battle.enums;

/**
 * 战斗类别
 */
public enum BattleType {
	PK_SINGLE(101, true), //玩家对战
	MONSTER(201, false), //挑战怪物
	ACTIVITY_MAP(202, false);//活动副本
	private int v;
	private boolean pvp;

	public int getV() {
		return v;
	}

	public boolean isPvp() {
		return pvp;
	}

	private BattleType(int v, boolean pvp) {
		this.v = v;
		this.pvp = pvp;
	}

	public static BattleType parse(int value) {
		BattleType[] enums = BattleType.values();
		for (BattleType tempEnum : enums) {
			if (tempEnum.getV() == value) {
				return tempEnum;
			}
		}
		return null;
	}
}
