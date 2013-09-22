package com.voyage.data.vo;

import java.util.Date;

public class SysUserRelationVO {
	private Integer surId;

	private Integer surUserId;

	private Integer surOppoId;

	private Date surTime;

	public Integer getSurId() {
		return surId;
	}

	public void setSurId(Integer surId) {
		this.surId = surId;
	}

	public Integer getSurUserId() {
		return surUserId;
	}

	public void setSurUserId(Integer surUserId) {
		this.surUserId = surUserId;
	}

	public Integer getSurOppoId() {
		return surOppoId;
	}

	public void setSurOppoId(Integer surOppoId) {
		this.surOppoId = surOppoId;
	}

	public Date getSurTime() {
		return surTime;
	}

	public void setSurTime(Date surTime) {
		this.surTime = surTime;
	}
}