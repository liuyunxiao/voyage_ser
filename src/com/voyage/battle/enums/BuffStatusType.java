package com.voyage.battle.enums;

/**BUFF状态*/
public enum BuffStatusType {
	NOW(0),//瞬间
	KEEP(1),//保持
	VANISH(2);//消失
	

	private int v;

	public int getV() {
		return v;
	}

	private BuffStatusType(int v) {
		this.v = v;
	}
}
