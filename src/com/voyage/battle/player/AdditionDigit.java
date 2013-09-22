package com.voyage.battle.player;

import java.io.Serializable;

/**数值加成*/
public class AdditionDigit implements Serializable {

	private static final long serialVersionUID = 2964336932800752329L;
	//伤害增加
	public double damAdd;
	//伤害减免
	public double damImmuneAdd;
	//反弹伤害
	public double damAntiAdd;
	//按最大生命增加
	public double hpAdd;
	//四基础属性
	public double somaAdd;
	public double shortAtkAdd;
	public double shortDefAdd;
	public double magicAtkAdd;

	public AdditionDigit add(AdditionDigit ad) {
		this.damAdd += ad.damAdd;
		this.damImmuneAdd += ad.damImmuneAdd;
		this.damAntiAdd += ad.damAntiAdd;
		this.hpAdd += ad.hpAdd;
		this.somaAdd += ad.somaAdd;
		this.shortAtkAdd += ad.shortAtkAdd;
		this.shortDefAdd += ad.shortDefAdd;
		this.magicAtkAdd += ad.magicAtkAdd;
		return this;
	}
}
