package com.voyage.enums;

/**
 * 系统参数类别
 * */
public enum SysParamType {
	POND(1);//奖池

	private int v;

	public int getV() {
		return this.v;
	}

	private SysParamType(int v) {
		this.v = v;
	}

}
