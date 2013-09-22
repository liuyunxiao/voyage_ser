package com.voyage.data.vo;

import java.util.Date;

import com.voyage.data.bo.MsgBO;

public class SysUserMsgVO {
	private Integer sumId;

	private Integer sumType;

	private Integer sumUserId;

	private Integer sumFromId;

	private String sumContent;

	private Date sumTime;

	private Integer sumState;

	public SysUserMsgVO() {
	}

	public SysUserMsgVO(MsgBO msgBO) {
		this.sumType = msgBO.type;
		this.sumUserId = msgBO.toId;
		this.sumFromId = msgBO.fromId;
		this.sumContent = msgBO.content;
	}

	public Integer getSumId() {
		return sumId;
	}

	public void setSumId(Integer sumId) {
		this.sumId = sumId;
	}

	public Integer getSumType() {
		return sumType;
	}

	public void setSumType(Integer sumType) {
		this.sumType = sumType;
	}

	public Integer getSumUserId() {
		return sumUserId;
	}

	public void setSumUserId(Integer sumUserId) {
		this.sumUserId = sumUserId;
	}

	public Integer getSumFromId() {
		return sumFromId;
	}

	public void setSumFromId(Integer sumFromId) {
		this.sumFromId = sumFromId;
	}

	public String getSumContent() {
		return sumContent;
	}

	public void setSumContent(String sumContent) {
		this.sumContent = sumContent;
	}

	public Date getSumTime() {
		return sumTime;
	}

	public void setSumTime(Date sumTime) {
		this.sumTime = sumTime;
	}

	public Integer getSumState() {
		return sumState;
	}

	public void setSumState(Integer sumState) {
		this.sumState = sumState;
	}
}