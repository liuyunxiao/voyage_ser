package com.voyage.data.vo;

import java.util.List;

public class CfgMapNodeVO {
	private Integer cmnId;

	private Integer cmnType;

	private Integer cmnLevel;

	private String cmnImg;

	private String cmnName;

	private Integer cmnNextId;

	private Integer cmnMonsterId;

	private String cmnReward;
	//临时属性
	private List<RewardVO> rewards;

	public String getCmnReward() {
		return cmnReward;
	}

	public void setCmnReward(String cmnReward) {
		this.cmnReward = cmnReward;
	}

	public List<RewardVO> getRewards() {
		return rewards;
	}

	public void setRewards(List<RewardVO> rewards) {
		this.rewards = rewards;
	}

	public Integer getCmnId() {
		return cmnId;
	}

	public void setCmnId(Integer cmnId) {
		this.cmnId = cmnId;
	}

	public Integer getCmnType() {
		return cmnType;
	}

	public void setCmnType(Integer cmnType) {
		this.cmnType = cmnType;
	}

	public Integer getCmnLevel() {
		return cmnLevel;
	}

	public void setCmnLevel(Integer cmnLevel) {
		this.cmnLevel = cmnLevel;
	}

	public String getCmnImg() {
		return cmnImg;
	}

	public void setCmnImg(String cmnImg) {
		this.cmnImg = cmnImg;
	}

	public String getCmnName() {
		return cmnName;
	}

	public void setCmnName(String cmnName) {
		this.cmnName = cmnName;
	}

	public Integer getCmnNextId() {
		return cmnNextId;
	}

	public void setCmnNextId(Integer cmnNextId) {
		this.cmnNextId = cmnNextId;
	}

	public Integer getCmnMonsterId() {
		return cmnMonsterId;
	}

	public void setCmnMonsterId(Integer cmnMonsterId) {
		this.cmnMonsterId = cmnMonsterId;
	}

}