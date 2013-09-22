package com.voyage.data.bo;

import java.util.List;

/**
 * 玩家对战列表单元
 * */
public class PkSingleBO {
	/**玩家*/
	public int suId;//玩家ID
	public String suName;//名字
	public int suLevel;//等级
	public String suImg;//头像
	public String suRemark;//签名
	public List<CorpsImgBO> captains;//队长单元列表
	public int suVipLevel;//VIP等级

	public PkSingleBO(int suId, String suName, int suLevel, int suVipLevel, String suImg, String suRemark, List<CorpsImgBO> captains) {
		super();
		this.suId = suId;
		this.suName = suName;
		this.suLevel = suLevel;
		this.suVipLevel = suVipLevel;
		this.suImg = suImg;
		this.suRemark = suRemark;
		this.captains = captains;
	}

	public int getSuVipLevel() {
		return suVipLevel;
	}

	public void setSuVipLevel(int suVipLevel) {
		this.suVipLevel = suVipLevel;
	}

	public int getSuId() {
		return suId;
	}

	public void setSuId(int suId) {
		this.suId = suId;
	}

	public String getSuName() {
		return suName;
	}

	public void setSuName(String suName) {
		this.suName = suName;
	}

	public int getSuLevel() {
		return suLevel;
	}

	public void setSuLevel(int suLevel) {
		this.suLevel = suLevel;
	}

	public String getSuImg() {
		return suImg;
	}

	public void setSuImg(String suImg) {
		this.suImg = suImg;
	}

	public String getSuRemark() {
		return suRemark;
	}

	public void setSuRemark(String suRemark) {
		this.suRemark = suRemark;
	}

	public List<CorpsImgBO> getCaptains() {
		return captains;
	}

	public void setCaptains(List<CorpsImgBO> captains) {
		this.captains = captains;
	}

}
