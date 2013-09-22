package com.voyage.data.bo;

import java.io.Serializable;

import com.voyage.data.vo.SysUserSlotsVO;

/**
 * 老虎机排行榜单元信息
 * */
public class SlotsRankBO implements Serializable {

	private static final long serialVersionUID = -3427078394388847560L;
	public int userId;//玩家ID
	public String name;//玩家名
	public int score;//玩家得分

	public SlotsRankBO() {
	}

	public SlotsRankBO(SysUserSlotsVO susVO, String name) {
		this.userId = susVO.getSusUserId();
		this.score = susVO.getSusScore();
		this.name = name;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
