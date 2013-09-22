package com.voyage.battle.bo;

import java.io.Serializable;

import com.voyage.battle.enums.AccidentType;

/**
 *伤害或治疗相关
 */
public class DamageBO implements Serializable {
	private static final long serialVersionUID = -597891853537885995L;
	public int accident;//见AccidentType
	public int dam;//伤害或治疗量（负数为伤害）
	public int type;//伤害类型,见DamageType

	public DamageBO(int type, int dam, boolean isCrit) {
		super();
		this.type = type;
		this.dam = dam;
		setAcci(isCrit);
	}

	private void setAcci(boolean isCrit) {
		if (this.dam == 0)
			this.accident = AccidentType.BLOCK.getV();
		else if (isCrit)
			this.accident = AccidentType.CRIT.getV();
		else
			this.accident = AccidentType.HIT.getV();
	}

	public int getAccident() {
		return accident;
	}

	public void setAccident(int accident) {
		this.accident = accident;
	}

	public int getDam() {
		return dam;
	}

	public void setDam(int dam) {
		this.dam = dam;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
