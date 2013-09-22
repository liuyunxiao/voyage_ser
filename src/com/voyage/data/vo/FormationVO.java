package com.voyage.data.vo;

/**
 * 阵型信息
 * */
public class FormationVO {
	private int subcId;//出战兵种表主键
	private int corpsId;//兵种表主键
	private int captain;//是否队长（0：否，1：是）
	private int pos;//阵型中的位置

	public FormationVO(int subcId, int corpsId, int captain, int pos) {
		super();
		this.subcId = subcId;
		this.corpsId = corpsId;
		this.captain = captain;
		this.pos = pos;
	}

	public int getSubcId() {
		return subcId;
	}

	public void setSubcId(int subcId) {
		this.subcId = subcId;
	}

	public int getCorpsId() {
		return corpsId;
	}

	public void setCorpsId(int corpsId) {
		this.corpsId = corpsId;
	}

	public int getCaptain() {
		return captain;
	}

	public void setCaptain(int captain) {
		this.captain = captain;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
}
