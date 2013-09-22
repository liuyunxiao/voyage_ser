package com.voyage.data.bo;

/**
 * 副本节点
 * */
public class MapNodeBO {
	public int cmnId;//副本节点表主键
	public String name;//节点名
	public int level;//节点等级
	public String img;//节点图片
	public int gold;//出战金额
	public int state;//0:未开启；1：未通关;2：已通关

	public MapNodeBO(int cmnId, String name, int level, String img) {
		super();
		this.cmnId = cmnId;
		this.name = name;
		this.level = level;
		this.img = img;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCmnId() {
		return cmnId;
	}

	public void setCmnId(int cmnId) {
		this.cmnId = cmnId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
}
