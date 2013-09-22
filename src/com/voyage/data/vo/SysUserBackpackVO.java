package com.voyage.data.vo;

import java.util.Date;

public class SysUserBackpackVO {
	private Integer subId;

	private Integer subUserId;

	private CfgGoodsVO subGoodsVO;

	private Integer subPos;
	private Date subTime;
	private Integer subType;
	private Date subMtime;
	private Integer subN;

	public SysUserBackpackVO() {
	}

	public SysUserBackpackVO(int subId) {
		this.subId = subId;
	}

	public Date getSubMtime() {
		return subMtime;
	}

	public void setSubMtime(Date subMtime) {
		this.subMtime = subMtime;
	}

	public Integer getSubN() {
		return subN;
	}

	public void setSubN(Integer subN) {
		this.subN = subN;
	}

	public Integer getSubType() {
		return subType;
	}

	public void setSubType(Integer subType) {
		this.subType = subType;
	}

	public Date getSubTime() {
		return subTime;
	}

	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	public Integer getSubUserId() {
		return subUserId;
	}

	public void setSubUserId(Integer subUserId) {
		this.subUserId = subUserId;
	}

	public CfgGoodsVO getSubGoodsVO() {
		return subGoodsVO;
	}

	public void setSubGoodsVO(CfgGoodsVO subGoodsVO) {
		this.subGoodsVO = subGoodsVO;
	}

	public Integer getSubPos() {
		return subPos;
	}

	public void setSubPos(Integer subPos) {
		this.subPos = subPos;
	}

}