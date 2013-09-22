package com.voyage.data.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * 新手教程奖励
 * */
public class NewComerBO {
	public int rewardId;//奖励ID
	public Integer gold;//金币
	public List<String> goods = new ArrayList<String>(1);//矿物、作物或兵种奖励图片

	public NewComerBO(int rewardId) {
		this.rewardId = rewardId;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public List<String> getGoods() {
		return goods;
	}

	public void setGoods(List<String> goods) {
		this.goods = goods;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}
}
