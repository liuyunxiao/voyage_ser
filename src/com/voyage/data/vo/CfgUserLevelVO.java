package com.voyage.data.vo;

import com.voyage.constant.Const;

public class CfgUserLevelVO {
	private Integer culId;

	private Integer culPerCost;

	private Integer culTotalCost;

	public Integer getCulId() {
		return culId;
	}

	public void setCulId(Integer culId) {
		this.culId = culId;
	}

	public Integer getCulPerCost() {
		return culPerCost;
	}

	public void setCulPerCost(Integer culPerCost) {
		this.culPerCost = culPerCost;
	}

	public Integer getCulTotalCost() {
		return culTotalCost;
	}

	public void setCulTotalCost(Integer culTotalCost) {
		this.culTotalCost = culTotalCost;
	}

	/**计算当前经验值*/
	public int getExp(int exp) {
		if (culPerCost == Const.INFINITY)
			return 0;
		return culPerCost - (culTotalCost - exp);
	}

	/**计算当前等级升级所需经验值*/
	public int getExpUp() {
		return culPerCost == Const.INFINITY ? 0 : culPerCost;
	}
}