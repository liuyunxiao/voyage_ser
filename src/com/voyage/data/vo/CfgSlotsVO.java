package com.voyage.data.vo;

public class CfgSlotsVO {
	private Integer csId;

	private String csName;

	private Integer csLuck;

	private Integer csWeight;

	private Double csTimes;

	private String csEquals;
	private String csSeed;
	private Integer csN;

	public String getCsSeed() {
		return csSeed;
	}

	public void setCsSeed(String csSeed) {
		this.csSeed = csSeed;
	}

	public Integer getCsN() {
		return csN;
	}

	public void setCsN(Integer csN) {
		this.csN = csN;
	}

	public Integer getCsId() {
		return csId;
	}

	public void setCsId(Integer csId) {
		this.csId = csId;
	}

	public String getCsName() {
		return csName;
	}

	public void setCsName(String csName) {
		this.csName = csName;
	}

	public Integer getCsLuck() {
		return csLuck;
	}

	public void setCsLuck(Integer csLuck) {
		this.csLuck = csLuck;
	}

	public Integer getCsWeight() {
		return csWeight;
	}

	public void setCsWeight(Integer csWeight) {
		this.csWeight = csWeight;
	}

	public Double getCsTimes() {
		return csTimes;
	}

	public void setCsTimes(Double csTimes) {
		this.csTimes = csTimes;
	}

	public String getCsEquals() {
		return csEquals;
	}

	public void setCsEquals(String csEquals) {
		this.csEquals = csEquals;
	}
}