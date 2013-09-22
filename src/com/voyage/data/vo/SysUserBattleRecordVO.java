package com.voyage.data.vo;

import java.util.Date;

public class SysUserBattleRecordVO {
	private Integer subrId;

	private Integer subrType;

	private String subrReplayId;

	private Date subrTime;

	private SysUserVO subrUserVO;

	private Integer subrOppoId;

	private Integer subrWinside;

	private Integer subrOppoGold;
	private Integer subrState;

	public Integer getSubrState() {
		return subrState;
	}

	public void setSubrState(Integer subrState) {
		this.subrState = subrState;
	}

	public Integer getSubrId() {
		return subrId;
	}

	public void setSubrId(Integer subrId) {
		this.subrId = subrId;
	}

	public Integer getSubrType() {
		return subrType;
	}

	public void setSubrType(Integer subrType) {
		this.subrType = subrType;
	}

	public String getSubrReplayId() {
		return subrReplayId;
	}

	public void setSubrReplayId(String subrReplayId) {
		this.subrReplayId = subrReplayId;
	}

	public Date getSubrTime() {
		return subrTime;
	}

	public void setSubrTime(Date subrTime) {
		this.subrTime = subrTime;
	}

	public SysUserVO getSubrUserVO() {
		return subrUserVO;
	}

	public void setSubrUserVO(SysUserVO subrUserVO) {
		this.subrUserVO = subrUserVO;
	}

	public Integer getSubrOppoId() {
		return subrOppoId;
	}

	public void setSubrOppoId(Integer subrOppoId) {
		this.subrOppoId = subrOppoId;
	}

	public Integer getSubrWinside() {
		return subrWinside;
	}

	public void setSubrWinside(Integer subrWinside) {
		this.subrWinside = subrWinside;
	}

	public Integer getSubrOppoGold() {
		return subrOppoGold;
	}

	public void setSubrOppoGold(Integer subrOppoGold) {
		this.subrOppoGold = subrOppoGold;
	}
}