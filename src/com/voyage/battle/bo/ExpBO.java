package com.voyage.battle.bo;

import java.io.Serializable;

/**
 * 经验相关
 * */
public class ExpBO implements Serializable {
	private static final long serialVersionUID = -472343153672588343L;
	public int level;//当前等级
	public int exp;//当前经验
	public int expUp;//当前等级总经验(满级时该值为0)

	public ExpBO(int level, int exp, int expUp) {
		super();
		this.level = level;
		this.exp = exp;
		this.expUp = expUp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getExpUp() {
		return expUp;
	}

	public void setExpUp(int expUp) {
		this.expUp = expUp;
	}

}
