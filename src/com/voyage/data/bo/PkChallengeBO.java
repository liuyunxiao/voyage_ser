package com.voyage.data.bo;

import java.util.List;

import com.voyage.battle.bo.ReplayBO;

/**
 * 对战返回信息
 * */
public class PkChallengeBO {
	public ReplayBO replay;//战报
	public List<PkSingleBO> users;//对战列表
	public ReplayBO getReplay() {
		return replay;
	}
	public void setReplay(ReplayBO replay) {
		this.replay = replay;
	}
	public List<PkSingleBO> getUsers() {
		return users;
	}
	public void setUsers(List<PkSingleBO> users) {
		this.users = users;
	}
}
