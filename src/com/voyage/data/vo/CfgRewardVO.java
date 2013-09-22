package com.voyage.data.vo;

public class CfgRewardVO {
	private Integer crId;

	private Integer crGold;

	private String crGoods;

	private String crCorps;

	private String crDesc;
	private Integer crType;

	public Integer getCrType() {
		return crType;
	}

	public void setCrType(Integer crType) {
		this.crType = crType;
	}

	public Integer getCrId() {
		return crId;
	}

	public void setCrId(Integer crId) {
		this.crId = crId;
	}

	public Integer getCrGold() {
		return crGold;
	}

	public void setCrGold(Integer crGold) {
		this.crGold = crGold;
	}

	public String getCrGoods() {
		return crGoods;
	}

	public void setCrGoods(String crGoods) {
		this.crGoods = crGoods;
	}

	public String getCrCorps() {
		return crCorps;
	}

	public void setCrCorps(String crCorps) {
		this.crCorps = crCorps;
	}

	public String getCrDesc() {
		return crDesc;
	}

	public void setCrDesc(String crDesc) {
		this.crDesc = crDesc;
	}
}