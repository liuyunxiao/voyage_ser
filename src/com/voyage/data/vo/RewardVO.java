package com.voyage.data.vo;

/**奖励信息*/
public class RewardVO {
	private int corpsId;//大概率掉落兵种ID
	private int minGold;//最小金额
	private int maxGold;//最大金额

	public RewardVO(int corpsId, int minGold, int maxGold) {
		super();
		this.corpsId = corpsId;
		this.minGold = minGold;
		this.maxGold = maxGold;
	}

	public int getCorpsId() {
		return corpsId;
	}

	public void setCorpsId(int corpsId) {
		this.corpsId = corpsId;
	}

	public int getMinGold() {
		return minGold;
	}

	public void setMinGold(int minGold) {
		this.minGold = minGold;
	}

	public int getMaxGold() {
		return maxGold;
	}

	public void setMaxGold(int maxGold) {
		this.maxGold = maxGold;
	}

}
