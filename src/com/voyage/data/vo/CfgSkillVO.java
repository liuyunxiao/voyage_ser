package com.voyage.data.vo;

public class CfgSkillVO {
	private Integer csId;

	private String csName;

	private String csDesc;

	private String csImg;

	private String csBattleImg;

	private String csRule;

	private Integer csBuffRate;

	private CfgBuffVO csBuffVO;
	private CfgBuffVO csBuffVO2;

	private Integer csBattleSide;
	private String csBattleBubble;

	public String getCsBattleBubble() {
		return csBattleBubble;
	}

	public void setCsBattleBubble(String csBattleBubble) {
		this.csBattleBubble = csBattleBubble;
	}

	public CfgBuffVO getCsBuffVO2() {
		return csBuffVO2;
	}

	public void setCsBuffVO2(CfgBuffVO csBuffVO2) {
		this.csBuffVO2 = csBuffVO2;
	}

	public Integer getCsBattleSide() {
		return csBattleSide;
	}

	public void setCsBattleSide(Integer csBattleSide) {
		this.csBattleSide = csBattleSide;
	}

	public Integer getCsId() {
		return csId;
	}

	public void setCsId(Integer csId) {
		this.csId = csId;
	}

	public String getCsName() {
		return csName;
	}

	public void setCsName(String csName) {
		this.csName = csName;
	}

	public String getCsDesc() {
		return csDesc;
	}

	public void setCsDesc(String csDesc) {
		this.csDesc = csDesc;
	}

	public String getCsImg() {
		return csImg;
	}

	public void setCsImg(String csImg) {
		this.csImg = csImg;
	}

	public String getCsBattleImg() {
		return csBattleImg;
	}

	public void setCsBattleImg(String csBattleImg) {
		this.csBattleImg = csBattleImg;
	}

	public String getCsRule() {
		return csRule;
	}

	public void setCsRule(String csRule) {
		this.csRule = csRule;
	}

	public Integer getCsBuffRate() {
		return csBuffRate;
	}

	public void setCsBuffRate(Integer csBuffRate) {
		this.csBuffRate = csBuffRate;
	}

	public CfgBuffVO getCsBuffVO() {
		return csBuffVO;
	}

	public void setCsBuffVO(CfgBuffVO csBuffVO) {
		this.csBuffVO = csBuffVO;
	}

	public boolean hasBuff() {
		return getCsBuffVO().getCbId() + getCsBuffVO2().getCbId() > 0;
	}
}