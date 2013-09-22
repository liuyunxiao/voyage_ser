package com.voyage.data.vo;

/**
 * 充值价格信息
 * */
public class CfgRechargeVO {
	private Integer crId;
	private Integer crGold;
	private Integer crRmb;
	private String crDesc;
	private String crImg;
	private String crProId;

	public String getCrProId() {
		return crProId;
	}

	public void setCrProId(String crProId) {
		this.crProId = crProId;
	}

	public String getCrImg() {
		return crImg;
	}

	public void setCrImg(String crImg) {
		this.crImg = crImg;
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

	public Integer getCrRmb() {
		return crRmb;
	}

	public void setCrRmb(Integer crRmb) {
		this.crRmb = crRmb;
	}

	public String getCrDesc() {
		return crDesc;
	}

	public void setCrDesc(String crDesc) {
		this.crDesc = crDesc;
	}
}