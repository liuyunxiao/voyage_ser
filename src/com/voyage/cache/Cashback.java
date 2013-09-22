package com.voyage.cache;

/**
 * 返金区
 * */
public class Cashback {
	private int lastGold;//上一次的返金
	private int nGolds;//累计猜中的金币

	/**
	 * 更新返金区
	 * */
	public void udpate(int nGold) {
		if (nGold > 0) {
			this.lastGold = nGold;
			this.nGolds += nGold;
		} else {
			this.lastGold = 0;
			this.nGolds = 0;
		}
	}

	public int getLastGold() {
		return lastGold;
	}

	public void setLastGold(int lastGold) {
		this.lastGold = lastGold;
	}

	public int getnGolds() {
		return nGolds;
	}

	public void setnGolds(int nGolds) {
		this.nGolds = nGolds;
	}
}
