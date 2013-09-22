package com.voyage.battle.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 战报相关
 * */
public class ReplayBO implements Serializable {
	private static final long serialVersionUID = 1638836147504908692L;
	public String battleId;//战报ID
	public String battleScene;//战斗背景
	public int battleType;//战斗类别(见BattleType,101:玩家对战,201:挑战怪物,202:活动副本)
	public int winSide;//获胜方（见SideType,1:左侧 2：右侧)
	public PlayerBO atker;//左侧玩家信息
	public PlayerBO defer;//右侧玩家信息
	public int maxRounds;//最大回合数
	public int totalRounds;//本次战斗的总回合数
	public List<RoundBO> rounds = new ArrayList<RoundBO>(1);//回合信息
	public RewardBO reward = new RewardBO();//攻方奖励
	public RewardBO oppoReward = new RewardBO();//对方奖励(不生成JSON)
	public int cdSkip;//N秒后可以点跳过(0表示没有跳过功能）
	public int speedUp;//N倍加速 (0表示没有加速功能)

	public int getCdSkip() {
		return cdSkip;
	}

	public void setCdSkip(int cdSkip) {
		this.cdSkip = cdSkip;
	}

	public int getSpeedUp() {
		return speedUp;
	}

	public void setSpeedUp(int speedUp) {
		this.speedUp = speedUp;
	}

	public RewardBO getReward() {
		return reward;
	}

	public void setReward(RewardBO reward) {
		this.reward = reward;
	}

	public String getBattleId() {
		return battleId;
	}

	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}

	public String getBattleScene() {
		return battleScene;
	}

	public void setBattleScene(String battleScene) {
		this.battleScene = battleScene;
	}

	public int getBattleType() {
		return battleType;
	}

	public void setBattleType(int battleType) {
		this.battleType = battleType;
	}

	public int getWinSide() {
		return winSide;
	}

	public void setWinSide(int winSide) {
		this.winSide = winSide;
	}

	public PlayerBO getAtker() {
		return atker;
	}

	public void setAtker(PlayerBO atker) {
		this.atker = atker;
	}

	public PlayerBO getDefer() {
		return defer;
	}

	public void setDefer(PlayerBO defer) {
		this.defer = defer;
	}

	public List<RoundBO> getRounds() {
		return rounds;
	}

	public void setRounds(List<RoundBO> rounds) {
		this.rounds = rounds;
	}

	public int getMaxRounds() {
		return maxRounds;
	}

	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	public int getTotalRounds() {
		return totalRounds;
	}

	public void setTotalRounds(int totalRounds) {
		this.totalRounds = totalRounds;
	}
}
