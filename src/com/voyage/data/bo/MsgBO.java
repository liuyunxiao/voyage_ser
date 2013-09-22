package com.voyage.data.bo;

import java.io.Serializable;
import java.text.DateFormat;

import com.voyage.data.vo.SysUserMsgVO;

/**
 * 单条消息
 * */
public class MsgBO implements Serializable {
	private static final long serialVersionUID = 840900585369728810L;
	public String content;//内容
	public int fromId;//发送者ID
	public int toId;//接收者ID
	public int type;//消息类别(0:系统，1：个人，2：好友申请,3:老虎机
	public String ftime;//发送时间,离线消息才有该属性(yyyy-MM-dd HH:mm:ss)

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public MsgBO(int type, int fromId, int toId, String content, String ftime) {
		super();
		this.type = type;
		this.fromId = fromId;
		this.toId = toId;
		this.content = content;
		this.ftime = ftime;
	}

	/**离线消息*/
	public MsgBO(SysUserMsgVO sumVO, DateFormat df) {
		this(sumVO.getSumType(), sumVO.getSumFromId(), sumVO.getSumUserId(), sumVO.getSumContent(), df.format(sumVO.getSumTime()));
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
