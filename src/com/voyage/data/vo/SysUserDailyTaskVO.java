package com.voyage.data.vo;

import java.util.Date;

public class SysUserDailyTaskVO {
	private Integer sudtId;

	private Integer sudtUserId;

	private Integer sudtType;

	private Integer sudtPro;

	private Date sudtTime;

	public SysUserDailyTaskVO() {
	}

	public SysUserDailyTaskVO(int sudtUserId) {
		this.sudtUserId = sudtUserId;
	}

	public Integer getSudtId() {
		return sudtId;
	}

	public void setSudtId(Integer sudtId) {
		this.sudtId = sudtId;
	}

	public Integer getSudtUserId() {
		return sudtUserId;
	}

	public void setSudtUserId(Integer sudtUserId) {
		this.sudtUserId = sudtUserId;
	}

	public Integer getSudtType() {
		return sudtType;
	}

	public void setSudtType(Integer sudtType) {
		this.sudtType = sudtType;
	}

	public Integer getSudtPro() {
		return sudtPro;
	}

	public void setSudtPro(Integer sudtPro) {
		this.sudtPro = sudtPro;
	}

	public Date getSudtTime() {
		return sudtTime;
	}

	public void setSudtTime(Date sudtTime) {
		this.sudtTime = sudtTime;
	}
}