package com.voyage.data.vo;

public class CfgMapNodeTypeVO {
	private Integer cmntId;

	private String cmntName;
	private Integer cmntNextId;

	public Integer getCmntNextId() {
		return cmntNextId;
	}

	public void setCmntNextId(Integer cmntNextId) {
		this.cmntNextId = cmntNextId;
	}

	public Integer getCmntId() {
		return cmntId;
	}

	public void setCmntId(Integer cmntId) {
		this.cmntId = cmntId;
	}

	public String getCmntName() {
		return cmntName;
	}

	public void setCmntName(String cmntName) {
		this.cmntName = cmntName;
	}
}