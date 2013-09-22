package com.voyage.data.bo;

/**
 * 兵种单元
 * */
public class CorpsUnitBO {
	public int subcId;//玩家出战兵种表主键
	public CorpsImgBO corpsImg;//兵种头像
	public int hireGold;//招募金额
	public int captain;//是否为队长(0:否；1：是）
	public int pos;//位置

	public CorpsUnitBO() {

	}

	public CorpsUnitBO(int subcId, int captain, int hireGold, CorpsImgBO corpsImg, int pos) {
		super();
		this.subcId = subcId;
		this.captain = captain;
		this.hireGold = hireGold;
		this.corpsImg = corpsImg;
		this.pos = pos;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getCaptain() {
		return captain;
	}

	public void setCaptain(int captain) {
		this.captain = captain;
	}

	public int getSubcId() {
		return subcId;
	}

	public void setSubcId(int subcId) {
		this.subcId = subcId;
	}

	public CorpsImgBO getCorpsImg() {
		return corpsImg;
	}

	public void setCorpsImg(CorpsImgBO corpsImg) {
		this.corpsImg = corpsImg;
	}

	public int getHireGold() {
		return hireGold;
	}

	public void setHireGold(int hireGold) {
		this.hireGold = hireGold;
	}

}
