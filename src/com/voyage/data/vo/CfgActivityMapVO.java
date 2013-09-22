package com.voyage.data.vo;

import java.util.List;

public class CfgActivityMapVO {
	private Integer camId;

	private Integer camLevel;

	private String camImg;

	private String camName;

	private Integer camN;

	private Integer camMonsterId;

	private String camReward;
	//临时属性
	private List<RewardVO> rewards;

	public List<RewardVO> getRewards() {
		return rewards;
	}

	public void setRewards(List<RewardVO> rewards) {
		this.rewards = rewards;
	}

	public Integer getCamId() {
		return camId;
	}

	public void setCamId(Integer camId) {
		this.camId = camId;
	}

	public Integer getCamLevel() {
		return camLevel;
	}

	public void setCamLevel(Integer camLevel) {
		this.camLevel = camLevel;
	}

	public String getCamImg() {
		return camImg;
	}

	public void setCamImg(String camImg) {
		this.camImg = camImg;
	}

	public String getCamName() {
		return camName;
	}

	public void setCamName(String camName) {
		this.camName = camName;
	}

	public Integer getCamN() {
		return camN;
	}

	public void setCamN(Integer camN) {
		this.camN = camN;
	}

	public Integer getCamMonsterId() {
		return camMonsterId;
	}

	public void setCamMonsterId(Integer camMonsterId) {
		this.camMonsterId = camMonsterId;
	}

	public String getCamReward() {
		return camReward;
	}

	public void setCamReward(String camReward) {
		this.camReward = camReward;
	}
}