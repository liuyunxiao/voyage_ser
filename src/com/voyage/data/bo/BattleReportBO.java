package com.voyage.data.bo;


/**
 * 战报列表单元
 * */
public class BattleReportBO {
	/**攻击方*/
	public String battleId;//战报ID
	public String battleTime;//战斗时间，格式：2013-05-30 16:49
	public int suId;//玩家ID
	public String suName;//名字
	public int suLevel;//等级
	public int suVipLevel;//VIP等级
	public String suImg;//头像
	public int winSide;//胜利方（1：攻击；2：防守方）
	/**守方*/
	public int defendGold;//守方金额变化

	public BattleReportBO(String battleId, String battleTime, int suId, String suName, int suLevel, int suVipLevel, String suImg, int winSide, int defendGold) {
		super();
		this.battleId = battleId;
		this.battleTime = battleTime;
		this.suId = suId;
		this.suName = suName;
		this.suLevel = suLevel;
		this.suVipLevel = suVipLevel;
		this.suImg = suImg;
		this.winSide = winSide;
		this.defendGold = defendGold;
	}

	public String getBattleTime() {
		return battleTime;
	}

	public void setBattleTime(String battleTime) {
		this.battleTime = battleTime;
	}

	public String getBattleId() {
		return battleId;
	}

	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}

	public int getSuId() {
		return suId;
	}

	public void setSuId(int suId) {
		this.suId = suId;
	}

	public String getSuName() {
		return suName;
	}

	public void setSuName(String suName) {
		this.suName = suName;
	}

	public int getSuLevel() {
		return suLevel;
	}

	public void setSuLevel(int suLevel) {
		this.suLevel = suLevel;
	}

	public String getSuImg() {
		return suImg;
	}

	public void setSuImg(String suImg) {
		this.suImg = suImg;
	}

	public int getSuVipLevel() {
		return suVipLevel;
	}

	public void setSuVipLevel(int suVipLevel) {
		this.suVipLevel = suVipLevel;
	}

	public int getWinSide() {
		return winSide;
	}

	public void setWinSide(int winSide) {
		this.winSide = winSide;
	}

	public int getDefendGold() {
		return defendGold;
	}

	public void setDefendGold(int defendGold) {
		this.defendGold = defendGold;
	}

}
