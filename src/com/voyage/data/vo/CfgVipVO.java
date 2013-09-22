package com.voyage.data.vo;

import com.voyage.constant.Const;

public class CfgVipVO {
	private Integer cvId;

	private Integer cvPerCost;

	private Integer cvTotalCost;

	private Double cvProtect;

	private String cvDesc;
	private Integer cvCdSkip;
	private Integer cvSpeedUp;
	private Integer cvDaySubsidy;
	private Double cvGiftRebate;
	private Integer cvSpeedUpLevel;

	public Integer getCvSpeedUpLevel() {
		return cvSpeedUpLevel;
	}

	public void setCvSpeedUpLevel(Integer cvSpeedUpLevel) {
		this.cvSpeedUpLevel = cvSpeedUpLevel;
	}

	public Integer getCvCdSkip() {
		return cvCdSkip;
	}

	public void setCvCdSkip(Integer cvCdSkip) {
		this.cvCdSkip = cvCdSkip;
	}

	public Integer getCvSpeedUp() {
		return cvSpeedUp;
	}

	public void setCvSpeedUp(Integer cvSpeedUp) {
		this.cvSpeedUp = cvSpeedUp;
	}

	public Integer getCvDaySubsidy() {
		return cvDaySubsidy;
	}

	public void setCvDaySubsidy(Integer cvDaySubsidy) {
		this.cvDaySubsidy = cvDaySubsidy;
	}

	public Double getCvGiftRebate() {
		return cvGiftRebate;
	}

	public void setCvGiftRebate(Double cvGiftRebate) {
		this.cvGiftRebate = cvGiftRebate;
	}

	public Integer getCvId() {
		return cvId;
	}

	public void setCvId(Integer cvId) {
		this.cvId = cvId;
	}

	public Integer getCvPerCost() {
		return cvPerCost;
	}

	public void setCvPerCost(Integer cvPerCost) {
		this.cvPerCost = cvPerCost;
	}

	public Integer getCvTotalCost() {
		return cvTotalCost;
	}

	public void setCvTotalCost(Integer cvTotalCost) {
		this.cvTotalCost = cvTotalCost;
	}

	public Double getCvProtect() {
		return cvProtect;
	}

	public void setCvProtect(Double cvProtect) {
		this.cvProtect = cvProtect;
	}

	public String getCvDesc() {
		return cvDesc;
	}

	public void setCvDesc(String cvDesc) {
		this.cvDesc = cvDesc;
	}

	/**计算当前经验值
	 * @param all 是否总进度显示
	 * @exp 累计经验
	 * */
	public int getExp(boolean all, int exp) {
		if (cvPerCost == Const.INFINITY)
			return 0;
		return all ? exp : (cvPerCost - (cvTotalCost - exp));
	}

	/**计算当前等级升级所需经验值
	 * @param all 是否总进度显示
	 * */
	public int getExpUp(boolean all) {
		if (cvPerCost == Const.INFINITY)
			return 0;
		return all ? cvTotalCost : cvPerCost;
	}
}