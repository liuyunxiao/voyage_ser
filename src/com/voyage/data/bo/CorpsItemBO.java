package com.voyage.data.bo;

/**
 * 英雄府兵种列表单项
 * */
public class CorpsItemBO {
	public int sucId;//玩家兵种表主键
	public int corpsId;//兵种表主键
	public String shortAtk;//物理攻击(格式：x+y)
	public String shortDef;//物理防御加点
	public String magicAtk;//技能攻击加点
	public String soma;//体质加点
	public int hireGold;//招募金额

	public CorpsItemBO(int sucId, int corpsId, String shortAtk, String shortDef, String magicAtk, String soma, int hireGold) {
		super();
		this.sucId = sucId;
		this.corpsId = corpsId;
		this.shortAtk = shortAtk;
		this.shortDef = shortDef;
		this.magicAtk = magicAtk;
		this.soma = soma;
		this.hireGold = hireGold;
	}

	public int getSucId() {
		return sucId;
	}

	public void setSucId(int sucId) {
		this.sucId = sucId;
	}

	public int getCorpsId() {
		return corpsId;
	}

	public void setCorpsId(int corpsId) {
		this.corpsId = corpsId;
	}

	public String getShortAtk() {
		return shortAtk;
	}

	public void setShortAtk(String shortAtk) {
		this.shortAtk = shortAtk;
	}

	public String getShortDef() {
		return shortDef;
	}

	public void setShortDef(String shortDef) {
		this.shortDef = shortDef;
	}

	public String getMagicAtk() {
		return magicAtk;
	}

	public void setMagicAtk(String magicAtk) {
		this.magicAtk = magicAtk;
	}

	public String getSoma() {
		return soma;
	}

	public void setSoma(String soma) {
		this.soma = soma;
	}

	public int getHireGold() {
		return hireGold;
	}

	public void setHireGold(int hireGold) {
		this.hireGold = hireGold;
	}

}
