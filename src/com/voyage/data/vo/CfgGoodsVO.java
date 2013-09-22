package com.voyage.data.vo;

public class CfgGoodsVO {
	private Integer cgId;

	private Integer cgType;

	private String cgName;

	private String cgDesc;

	private Integer cgPrice;

	private String cgImg;
	private Integer cgMax;
	private Integer cgUnit;
	private Integer cgGain;

	public Integer getCgMax() {
		return cgMax;
	}

	public void setCgMax(Integer cgMax) {
		this.cgMax = cgMax;
	}

	public Integer getCgUnit() {
		return cgUnit;
	}

	public void setCgUnit(Integer cgUnit) {
		this.cgUnit = cgUnit;
	}

	public Integer getCgGain() {
		return cgGain;
	}

	public void setCgGain(Integer cgGain) {
		this.cgGain = cgGain;
	}

	public CfgGoodsVO() {

	}

	public CfgGoodsVO(int cgId) {
		this.cgId = cgId;
	}

	public Integer getCgId() {
		return cgId;
	}

	public void setCgId(Integer cgId) {
		this.cgId = cgId;
	}

	public Integer getCgType() {
		return cgType;
	}

	public void setCgType(Integer cgType) {
		this.cgType = cgType;
	}

	public String getCgName() {
		return cgName;
	}

	public void setCgName(String cgName) {
		this.cgName = cgName;
	}

	public String getCgDesc() {
		return cgDesc;
	}

	public void setCgDesc(String cgDesc) {
		this.cgDesc = cgDesc;
	}

	public Integer getCgPrice() {
		return cgPrice;
	}

	public void setCgPrice(Integer cgPrice) {
		this.cgPrice = cgPrice;
	}

	public String getCgImg() {
		return cgImg;
	}

	public void setCgImg(String cgImg) {
		this.cgImg = cgImg;
	}
}