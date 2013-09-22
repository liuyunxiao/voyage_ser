package com.voyage.data.bo;

/**
 * 赠送金币界面信息
 * */
public class GiveBO {
	public int maxS;//我的赠送上限
	public int maxR;//对方的接收上限

	public GiveBO(int maxS, int maxR) {
		super();
		this.maxS = maxS;
		this.maxR = maxR;
	}

	public int getMaxS() {
		return maxS;
	}

	public void setMaxS(int maxS) {
		this.maxS = maxS;
	}

	public int getMaxR() {
		return maxR;
	}

	public void setMaxR(int maxR) {
		this.maxR = maxR;
	}

}
