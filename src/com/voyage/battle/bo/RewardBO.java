package com.voyage.battle.bo;

import java.io.Serializable;
import java.util.List;

import com.voyage.data.bo.CorpsImgBO;

/**
 * 奖励相关
 * */
public class RewardBO implements Serializable {
	private static final long serialVersionUID = -2628502012615636933L;
	public int gold;//获得或损失的金币(正数为获得，负数为损失)
	public ExpBO oldExp;//原经验相关
	public ExpBO newExp;//最新经验相关
	public List<CorpsImgBO> corps;//掉落的兵种列表

	public RewardBO() {
	}

	public RewardBO(int gold) {
		this.gold = gold;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public ExpBO getOldExp() {
		return oldExp;
	}

	public void setOldExp(ExpBO oldExp) {
		this.oldExp = oldExp;
	}

	public ExpBO getNewExp() {
		return newExp;
	}

	public void setNewExp(ExpBO newExp) {
		this.newExp = newExp;
	}

	public List<CorpsImgBO> getCorps() {
		return corps;
	}

	public void setCorps(List<CorpsImgBO> corps) {
		this.corps = corps;
	}

}
