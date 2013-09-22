package com.voyage.data.vo;

public class CfgCorpsGradeVO {
	private Integer ccgId;

	private String ccgName;

	private Double ccgFactor;
	private String ccgCorpsBg;
	private Integer ccgPropBase;
	private Integer ccgPropDot;

	public Integer getCcgPropBase() {
		return ccgPropBase;
	}

	public void setCcgPropBase(Integer ccgPropBase) {
		this.ccgPropBase = ccgPropBase;
	}

	public Integer getCcgPropDot() {
		return ccgPropDot;
	}

	public void setCcgPropDot(Integer ccgPropDot) {
		this.ccgPropDot = ccgPropDot;
	}

	public String getCcgCorpsBg() {
		return ccgCorpsBg;
	}

	public void setCcgCorpsBg(String ccgCorpsBg) {
		this.ccgCorpsBg = ccgCorpsBg;
	}

	public Integer getCcgId() {
		return ccgId;
	}

	public void setCcgId(Integer ccgId) {
		this.ccgId = ccgId;
	}

	public String getCcgName() {
		return ccgName;
	}

	public void setCcgName(String ccgName) {
		this.ccgName = ccgName;
	}

	public Double getCcgFactor() {
		return ccgFactor;
	}

	public void setCcgFactor(Double ccgFactor) {
		this.ccgFactor = ccgFactor;
	}
}