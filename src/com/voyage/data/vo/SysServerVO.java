package com.voyage.data.vo;

/**服务器信息*/
public class SysServerVO {
	private Integer ssId;//ID
	private String ssName;//名称
	private Integer ssState;//状态标志

	public Integer getSsId() {
		return ssId;
	}

	public void setSsId(Integer ssId) {
		this.ssId = ssId;
	}

	public String getSsName() {
		return ssName;
	}

	public void setSsName(String ssName) {
		this.ssName = ssName;
	}

	public Integer getSsState() {
		return ssState;
	}

	public void setSsState(Integer ssState) {
		this.ssState = ssState;
	}

}