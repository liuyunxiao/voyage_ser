package com.voyage.data.vo;

import java.util.List;

public class CfgMonsterVO {
	private Integer cmId;

	private String cmName;

	private String cmDesc;

	private Integer cmLevel;

	private String cmCorps;
	private String cmImg;
	private Integer cmType;
	//临时属性
	private List<SysUserBattleCorpsVO> battleCorpsList;//出战阵容
	private int battleCorpsGold;//出战阵容金额

	public int getBattleCorpsGold() {
		return battleCorpsGold;
	}

	public void setBattleCorpsGold(int battleCorpsGold) {
		this.battleCorpsGold = battleCorpsGold;
	}

	public Integer getCmType() {
		return cmType;
	}

	public void setCmType(Integer cmType) {
		this.cmType = cmType;
	}

	public String getCmImg() {
		return cmImg;
	}

	public void setCmImg(String cmImg) {
		this.cmImg = cmImg;
	}

	public List<SysUserBattleCorpsVO> getBattleCorpsList() {
		return battleCorpsList;
	}

	public void setBattleCorpsList(List<SysUserBattleCorpsVO> battleCorpsList) {
		this.battleCorpsList = battleCorpsList;
	}

	public Integer getCmId() {
		return cmId;
	}

	public void setCmId(Integer cmId) {
		this.cmId = cmId;
	}

	public String getCmName() {
		return cmName;
	}

	public void setCmName(String cmName) {
		this.cmName = cmName;
	}

	public String getCmDesc() {
		return cmDesc;
	}

	public void setCmDesc(String cmDesc) {
		this.cmDesc = cmDesc;
	}

	public Integer getCmLevel() {
		return cmLevel;
	}

	public void setCmLevel(Integer cmLevel) {
		this.cmLevel = cmLevel;
	}

	public String getCmCorps() {
		return cmCorps;
	}

	public void setCmCorps(String cmCorps) {
		this.cmCorps = cmCorps;
	}

}