package com.voyage.data.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 老虎机返回的游戏结果
 * */
public class SlotsPlayBO implements Serializable {

	private static final long serialVersionUID = 1283524958937131207L;
	public int csId;//中的是什么水果(先判断是否为goodluck)
	public List<Integer> fruits = new ArrayList<Integer>(2);//goodluck时才有效
	public int gold;//获得的金币
	public int inPond;//奖池命中信息，0:未中，1：大奖池，2:小奖池

	public int getCsId() {
		return csId;
	}

	public void setCsId(int csId) {
		this.csId = csId;
	}

	public List<Integer> getFruits() {
		return fruits;
	}

	public void setFruits(List<Integer> fruits) {
		this.fruits = fruits;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getInPond() {
		return inPond;
	}

	public void setInPond(int inPond) {
		this.inPond = inPond;
	}

}
