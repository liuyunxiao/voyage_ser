package com.voyage.data.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 老虎机界面信息
 * */
public class SlotsBO implements Serializable {
	private static final long serialVersionUID = 1407135388089949880L;
	public List<SlotsRankBO> ranks;//排行榜
	public int score;//周得分
	public PondBO pond;//奖池
	public List<MsgBO> msgs = new ArrayList<MsgBO>(10);//消息列表

	public List<MsgBO> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<MsgBO> msgs) {
		this.msgs = msgs;
	}

	public List<SlotsRankBO> getRanks() {
		return ranks;
	}

	public void setRanks(List<SlotsRankBO> ranks) {
		this.ranks = ranks;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public PondBO getPond() {
		return pond;
	}

	public void setPond(PondBO pond) {
		this.pond = pond;
	}
}
