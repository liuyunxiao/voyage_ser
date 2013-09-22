package com.voyage.data.vo;

import com.voyage.battle.enums.CorpsType;
import com.voyage.constant.Const;

public class CfgCorpsVO {
	private Integer ccId;

	private String ccName;

	private String ccDesc;

	private CfgCorpsTypeVO ccCorpsTypeVO;

	private String ccImg;

	private Double ccAtkFactor;

	private Double ccDefFactor;

	private Integer ccAtkSkillRate;

	private CfgSkillVO ccAtkSkillVO;
	private CfgSkillVO ccDefSkillVO;

	private Integer ccDefSkillRate;

	private CfgCorpsGradeVO ccCorpsGradeVO;

	public CfgCorpsVO() {
	}

	public CfgCorpsVO(Integer ccId) {
		this.ccId = ccId;
	}

	public CfgCorpsGradeVO getCcCorpsGradeVO() {
		return ccCorpsGradeVO;
	}

	public void setCcCorpsGradeVO(CfgCorpsGradeVO ccCorpsGradeVO) {
		this.ccCorpsGradeVO = ccCorpsGradeVO;
	}

	public Integer getCcId() {
		return ccId;
	}

	public void setCcId(Integer ccId) {
		this.ccId = ccId;
	}

	public String getCcName() {
		return ccName;
	}

	public void setCcName(String ccName) {
		this.ccName = ccName;
	}

	public String getCcDesc() {
		return ccDesc;
	}

	public void setCcDesc(String ccDesc) {
		this.ccDesc = ccDesc;
	}

	public CfgCorpsTypeVO getCcCorpsTypeVO() {
		return ccCorpsTypeVO;
	}

	public void setCcCorpsTypeVO(CfgCorpsTypeVO ccCorpsTypeVO) {
		this.ccCorpsTypeVO = ccCorpsTypeVO;
	}

	public String getCcImg() {
		return ccImg;
	}

	public void setCcImg(String ccImg) {
		this.ccImg = ccImg;
	}

	public Double getCcAtkFactor() {
		return ccAtkFactor;
	}

	public void setCcAtkFactor(Double ccAtkFactor) {
		this.ccAtkFactor = ccAtkFactor;
	}

	public Double getCcDefFactor() {
		return ccDefFactor;
	}

	public void setCcDefFactor(Double ccDefFactor) {
		this.ccDefFactor = ccDefFactor;
	}

	public Integer getCcAtkSkillRate() {
		return ccAtkSkillRate;
	}

	public void setCcAtkSkillRate(Integer ccAtkSkillRate) {
		this.ccAtkSkillRate = ccAtkSkillRate;
	}

	public CfgSkillVO getCcAtkSkillVO() {
		return ccAtkSkillVO;
	}

	public void setCcAtkSkillVO(CfgSkillVO ccAtkSkillVO) {
		this.ccAtkSkillVO = ccAtkSkillVO;
	}

	public CfgSkillVO getCcDefSkillVO() {
		return ccDefSkillVO;
	}

	public void setCcDefSkillVO(CfgSkillVO ccDefSkillVO) {
		this.ccDefSkillVO = ccDefSkillVO;
	}

	public Integer getCcDefSkillRate() {
		return ccDefSkillRate;
	}

	public void setCcDefSkillRate(Integer ccDefSkillRate) {
		this.ccDefSkillRate = ccDefSkillRate;
	}

	public boolean isSpecialCorps() {
		return CorpsType.isSpecailCorps(getCcCorpsTypeVO().getCctId());
	}

	public boolean hasSkill() {
		return getCcAtkSkillVO().getCsId() + getCcDefSkillVO().getCsId() > 0;
	}

	/**计算兵种等级
	 * @param total 总加点数
	 * */
	public int getCorpsLevel(int total) {
		return total / Const.CORPS_LEVEL_DOTS + (total % Const.CORPS_LEVEL_DOTS == 0 ? 0 : 1);
	}

	/**计算兵种的基础属性*/
	public int getPropBase(int total) {
		return getCorpsLevel(total) / ccCorpsGradeVO.getCcgPropBase() + ccCorpsGradeVO.getCcgPropDot();
	}

}