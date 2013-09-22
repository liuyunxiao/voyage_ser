package com.voyage.enums;

/**
 * 消息类别
 * */
public enum MsgType {
	SYSTEM(0), //系统
	PERSONAL(1), //个人
	FRIEND_APPLY(2), //好友申请
	SLOTS(3);//老虎机
	private int v;

	public int getV() {
		return this.v;
	}

	private MsgType(int v) {
		this.v = v;
	}
}
