package com.voyage.battle.bo;

import java.io.Serializable;

import com.voyage.constant.Const;

/**
 *BUFF状态相关
 */
public class BuffBO implements Serializable {
	private static final long serialVersionUID = 7065667927939212431L;
	public int id;//buffId
	public String effect;//特效
	public int hpMf;//BUFF引起的伤害或治疗(负数为伤害）
	public int crit;//是否暴击(0:否；1：是）
	public int status;//具体形式见BuffStatusType

	public BuffBO(int id, String effect, int hpMf, int status, int crit) {
		super();
		this.id = id;
		this.effect = effect;
		this.hpMf = hpMf;
		this.status = status;
		this.crit = crit;
	}

	/*public BuffBO(int id, int status) {
		super();
		this.id = id;
		this.status = status;
	}*/

	public BuffBO(String effect, int status) {
		super();
		this.effect = effect;
		this.status = status;
	}

	public int getCrit() {
		return crit;
	}

	public void setCrit(int crit) {
		this.crit = crit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public int getHpMf() {
		return hpMf;
	}

	public void setHpMf(int hpMf) {
		this.hpMf = hpMf;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void merge(BuffBO buff) {
		this.hpMf += buff.hpMf;
	}

	/**是否有战斗资源*/
	private boolean hasBattleImg() {
		return !(this.effect == null || Const.NONE.equals(this.effect));
	}

	public boolean validable() {
		return hasBattleImg() || hpMf != 0;
	}
}
