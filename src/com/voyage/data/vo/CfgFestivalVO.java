package com.voyage.data.vo;

import java.util.Date;

public class CfgFestivalVO {
	private Integer cfId;

	private Date cfStart;

	private Date cfEnd;

	private String cfDesc;

	private CfgRewardVO cfRewardVO;

	public Integer getCfId() {
		return cfId;
	}

	public void setCfId(Integer cfId) {
		this.cfId = cfId;
	}

	public Date getCfStart() {
		return cfStart;
	}

	public void setCfStart(Date cfStart) {
		this.cfStart = cfStart;
	}

	public Date getCfEnd() {
		return cfEnd;
	}

	public void setCfEnd(Date cfEnd) {
		this.cfEnd = cfEnd;
	}

	public String getCfDesc() {
		return cfDesc;
	}

	public void setCfDesc(String cfDesc) {
		this.cfDesc = cfDesc;
	}

	public CfgRewardVO getCfRewardVO() {
		return cfRewardVO;
	}

	public void setCfRewardVO(CfgRewardVO cfRewardVO) {
		this.cfRewardVO = cfRewardVO;
	}

}