package com.voyage.data.vo;

import java.util.Date;
import java.util.List;

public class SysUserVO {
	private Integer suId;

	private SysAccountVO suAccountVO;

	private String suName;

	private Integer suSex;

	private String suImg;

	private Integer suLevel;

	private Integer suVipLevel;

	private String suRemark;

	private Date suCreate;
	private Date suLogin;

	private Date suLogout;

	private Date suProtect;
	private SysUserGoldVO suGoldVO;
	private String suIdAlias;
	private String suContact;
	private String suCaptain;
	private Integer suExp;
	private Integer suVipExp;
	private Integer suLoginDays;
	private String suArmy;
	private Integer suNewer;
	private String suDaily;
	private Integer suFirstCharge;

	public Integer getSuFirstCharge() {
		return suFirstCharge;
	}

	public void setSuFirstCharge(Integer suFirstCharge) {
		this.suFirstCharge = suFirstCharge;
	}

	public String getSuDaily() {
		return suDaily;
	}

	public void setSuDaily(String suDaily) {
		this.suDaily = suDaily;
	}

	public Integer getSuNewer() {
		return suNewer;
	}

	public void setSuNewer(Integer suNewer) {
		this.suNewer = suNewer;
	}

	public String getSuArmy() {
		return suArmy;
	}

	public void setSuArmy(String suArmy) {
		this.suArmy = suArmy;
	}

	public Integer getSuLoginDays() {
		return suLoginDays;
	}

	public void setSuLoginDays(Integer suLoginDays) {
		this.suLoginDays = suLoginDays;
	}

	//临时属性
	private List<SysUserBattleCorpsVO> battleCorpsList;
	private Integer zg;//分时间段登录时系统送的钱

	public Integer getZg() {
		return zg;
	}

	public void setZg(Integer zg) {
		this.zg = zg;
	}

	public Integer getSuExp() {
		return suExp;
	}

	public void setSuExp(Integer suExp) {
		this.suExp = suExp;
	}

	public Integer getSuVipExp() {
		return suVipExp;
	}

	public void setSuVipExp(Integer suVipExp) {
		this.suVipExp = suVipExp;
	}

	public Date getSuCreate() {
		return suCreate;
	}

	public void setSuCreate(Date suCreate) {
		this.suCreate = suCreate;
	}

	public String getSuCaptain() {
		return suCaptain;
	}

	public void setSuCaptain(String suCaptain) {
		this.suCaptain = suCaptain;
	}

	public String getSuContact() {
		return suContact;
	}

	public void setSuContact(String suContact) {
		this.suContact = suContact;
	}

	public List<SysUserBattleCorpsVO> getBattleCorpsList() {
		return battleCorpsList;
	}

	public void setBattleCorpsList(List<SysUserBattleCorpsVO> battleCorpsList) {
		this.battleCorpsList = battleCorpsList;
	}

	public SysUserVO(int suId) {
		this.suId = suId;
	}

	public SysUserVO() {
	}

	public String getSuIdAlias() {
		return suIdAlias;
	}

	public void setSuIdAlias(String suIdAlias) {
		this.suIdAlias = suIdAlias;
	}

	public Date getSuLogin() {
		return suLogin;
	}

	public void setSuLogin(Date suLogin) {
		this.suLogin = suLogin;
	}

	public Date getSuLogout() {
		return suLogout;
	}

	public void setSuLogout(Date suLogout) {
		this.suLogout = suLogout;
	}

	public Date getSuProtect() {
		return suProtect;
	}

	public void setSuProtect(Date suProtect) {
		this.suProtect = suProtect;
	}

	public String getSuRemark() {
		return suRemark;
	}

	public void setSuRemark(String suRemark) {
		this.suRemark = suRemark;
	}

	public SysUserGoldVO getSuGoldVO() {
		return suGoldVO;
	}

	public void setSuGoldVO(SysUserGoldVO suGoldVO) {
		this.suGoldVO = suGoldVO;
	}

	public Integer getSuId() {
		return suId;
	}

	public void setSuId(Integer suId) {
		this.suId = suId;
	}

	public SysAccountVO getSuAccountVO() {
		return suAccountVO;
	}

	public void setSuAccountVO(SysAccountVO suAccountVO) {
		this.suAccountVO = suAccountVO;
	}

	public String getSuName() {
		return suName;
	}

	public void setSuName(String suName) {
		this.suName = suName;
	}

	public Integer getSuSex() {
		return suSex;
	}

	public void setSuSex(Integer suSex) {
		this.suSex = suSex;
	}

	public String getSuImg() {
		return suImg;
	}

	public void setSuImg(String suImg) {
		this.suImg = suImg;
	}

	public Integer getSuLevel() {
		return suLevel;
	}

	public void setSuLevel(Integer suLevel) {
		this.suLevel = suLevel;
	}

	public Integer getSuVipLevel() {
		return suVipLevel;
	}

	public void setSuVipLevel(Integer suVipLevel) {
		this.suVipLevel = suVipLevel;
	}

	@Override
	public int hashCode() {
		int h = 17 * 37 + this.suId;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SysUserVO)) {
			return false;
		}

		SysUserVO other = (SysUserVO) obj;

		return this.suId.intValue() == other.suId;
	}
}