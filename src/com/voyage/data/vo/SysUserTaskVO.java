package com.voyage.data.vo;

import java.util.Date;

public class SysUserTaskVO {
	private Integer sutId;

	private Integer sutUserId;

	private Integer sutTaskId;

	private Integer sutState;

	private Date sutTime;

	private Date sutMtime;
	private Integer sutType;

	public Integer getSutType() {
		return sutType;
	}

	public void setSutType(Integer sutType) {
		this.sutType = sutType;
	}

	public SysUserTaskVO() {
	}

	public SysUserTaskVO(int sutId) {
		this.sutId = sutId;
	}

	public Integer getSutId() {
		return sutId;
	}

	public void setSutId(Integer sutId) {
		this.sutId = sutId;
	}

	public Integer getSutUserId() {
		return sutUserId;
	}

	public void setSutUserId(Integer sutUserId) {
		this.sutUserId = sutUserId;
	}

	public Integer getSutTaskId() {
		return sutTaskId;
	}

	public void setSutTaskId(Integer sutTaskId) {
		this.sutTaskId = sutTaskId;
	}

	public Integer getSutState() {
		return sutState;
	}

	public void setSutState(Integer sutState) {
		this.sutState = sutState;
	}

	public Date getSutTime() {
		return sutTime;
	}

	public void setSutTime(Date sutTime) {
		this.sutTime = sutTime;
	}

	public Date getSutMtime() {
		return sutMtime;
	}

	public void setSutMtime(Date sutMtime) {
		this.sutMtime = sutMtime;
	}
}