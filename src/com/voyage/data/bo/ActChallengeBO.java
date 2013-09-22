package com.voyage.data.bo;

import java.io.Serializable;
import java.util.List;

import com.voyage.battle.bo.ReplayBO;

/**
 * 打活动副本返回的信息
 * */
public class ActChallengeBO implements Serializable {
	private static final long serialVersionUID = 3830498809162373200L;
	public ReplayBO replay;//战报
	public List<ActivityMapBO> acts;//最新活动列表（有新活动副本开启时传）

	public ReplayBO getReplay() {
		return replay;
	}

	public void setReplay(ReplayBO replay) {
		this.replay = replay;
	}

	public List<ActivityMapBO> getActs() {
		return acts;
	}

	public void setActs(List<ActivityMapBO> acts) {
		this.acts = acts;
	}
}
