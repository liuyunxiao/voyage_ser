package com.voyage.data.bo;

import java.io.Serializable;

import com.voyage.data.vo.SysUserGoldVO;
import com.voyage.data.vo.SysUserVO;

/**
 * 排行榜单元信息
 * */
public class RankBO implements Serializable {
	private static final long serialVersionUID = -1874270805193288725L;
	public int userId;//玩家ID
	public int rank;//排名
	public int level;//玩家等级
	public int allGold;//总资产
	public int offset;//排名浮动(正数为上升、负数为下降、0为持平)
	public String name;//玩家名

	/**
	 * 新建角色不计算总资产
	 * @param newRole 是否新建角色
	 * */
	public RankBO(SysUserVO suVO, boolean newRole) {
		this.userId = suVO.getSuId();
		this.level = suVO.getSuLevel();
		this.name = suVO.getSuName();
		if (!newRole)
			this.allGold = getAllGold(suVO.getSuGoldVO());
	}

	public RankBO(int userId) {
		this.userId = userId;
	}

	/**
	 * 计算玩家总资产
	 * */
	private int getAllGold(SysUserGoldVO sugVO) {
		return sugVO.getSugStorage() + sugVO.getSugUpCorpsProp() + sugVO.getSugBattleCorps() + sugVO.getSugFarm() + sugVO.getSugMine() + sugVO.getSugGift();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAllGold() {
		return allGold;
	}

	public void setAllGold(int allGold) {
		this.allGold = allGold;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public int hashCode() {
		int h = 17 * 37 + this.userId;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RankBO)) {
			return false;
		}

		RankBO other = (RankBO) obj;

		return this.userId == other.userId;
	}
}
