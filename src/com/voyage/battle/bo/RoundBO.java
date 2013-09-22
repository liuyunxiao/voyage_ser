package com.voyage.battle.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 回合详细信息
 * */
public class RoundBO implements Serializable {
	private static final long serialVersionUID = -217297307876802564L;
	public int round;//当前回合
	public List<HalfRoundBO> items = new ArrayList<HalfRoundBO>(1);//行动信息

	public RoundBO(int round) {
		this.round = round;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public List<HalfRoundBO> getItems() {
		return items;
	}

	public void setItems(List<HalfRoundBO> items) {
		this.items = items;
	}
}
