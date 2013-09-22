package com.voyage.data.vo;

import java.util.Date;

public class SysUserExpenseVO {
	private Integer sueId;

	private Integer sueUserId;

	private Integer sueType;

	private Integer sueExpenseId;

	private Date sueTime;
	private Integer sueCost;
	private String sueDesc;

	public String getSueDesc() {
		return sueDesc;
	}

	public void setSueDesc(String sueDesc) {
		this.sueDesc = sueDesc;
	}

	public Integer getSueCost() {
		return sueCost;
	}

	public void setSueCost(Integer sueCost) {
		this.sueCost = sueCost;
	}

	public Integer getSueId() {
		return sueId;
	}

	public void setSueId(Integer sueId) {
		this.sueId = sueId;
	}

	public Integer getSueUserId() {
		return sueUserId;
	}

	public void setSueUserId(Integer sueUserId) {
		this.sueUserId = sueUserId;
	}

	public Integer getSueType() {
		return sueType;
	}

	public void setSueType(Integer sueType) {
		this.sueType = sueType;
	}

	public Integer getSueExpenseId() {
		return sueExpenseId;
	}

	public void setSueExpenseId(Integer sueExpenseId) {
		this.sueExpenseId = sueExpenseId;
	}

	public Date getSueTime() {
		return sueTime;
	}

	public void setSueTime(Date sueTime) {
		this.sueTime = sueTime;
	}

}