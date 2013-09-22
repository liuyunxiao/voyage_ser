package com.voyage.data.bo;

/**
 * 服务器信息
 * */
public class ServerBO {
	public int ssId;//服务器ID
	public String ssName;//名称
	public int ssState;//状态标志(1:新，2：流畅，3：爆满)

	public ServerBO(int ssId, String ssName, int ssState) {
		super();
		this.ssId = ssId;
		this.ssName = ssName;
		this.ssState = ssState;
	}

	public int getSsId() {
		return ssId;
	}

	public void setSsId(int ssId) {
		this.ssId = ssId;
	}

	public String getSsName() {
		return ssName;
	}

	public void setSsName(String ssName) {
		this.ssName = ssName;
	}

	public int getSsState() {
		return ssState;
	}

	public void setSsState(int ssState) {
		this.ssState = ssState;
	}

}
