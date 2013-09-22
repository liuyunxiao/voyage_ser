package com.voyage.battle.enums;

/**匹配类别*/
public enum MatchType {
	ARENA(0); //竞技场
	private int v;

	public int getV() {
		return v;
	}

	private MatchType(int v) {
		this.v = v;
	}
}
