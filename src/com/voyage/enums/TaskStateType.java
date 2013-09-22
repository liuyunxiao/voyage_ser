package com.voyage.enums;

/**
 * 任务状态
 * */
public enum TaskStateType {
	NOT_FINISH(0), //未完成
	FINISHED(1), //已完成
	RECEIVED(2);//已领取
	private int v;

	public int getV() {
		return this.v;
	}

	TaskStateType(int v) {
		this.v = v;
	}
}
