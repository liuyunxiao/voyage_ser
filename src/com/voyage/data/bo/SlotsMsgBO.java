package com.voyage.data.bo;

import java.io.Serializable;

import com.voyage.data.bo.MsgBO;

/**
 * 老虎机需要动态更新的信息
 * */
public class SlotsMsgBO implements Serializable {

	private static final long serialVersionUID = -819850473489370337L;
	public PondBO pond;//奖池
	public MsgBO msg;//消息

	public SlotsMsgBO() {
	}

	public SlotsMsgBO(PondBO pond, MsgBO msg) {
		super();
		this.pond = pond;
		this.msg = msg;
	}

	public PondBO getPond() {
		return pond;
	}

	public void setPond(PondBO pond) {
		this.pond = pond;
	}

	public MsgBO getMsg() {
		return msg;
	}

	public void setMsg(MsgBO msg) {
		this.msg = msg;
	}

}
