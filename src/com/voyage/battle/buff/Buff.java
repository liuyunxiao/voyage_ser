package com.voyage.battle.buff;

import java.io.Serializable;

import com.voyage.battle.player.AdditionFactor;

public class Buff implements Serializable {
	private static final long serialVersionUID = -497997297771073410L;
	public int bId;
	public int bContinues;//持续回合
	public int bCalcType;//结算时机
	public int bType;//增益类别
	public int bKeep;//是否持续显示BUFF特效
	public String bBattleImg;//BUFF特效
	public AdditionFactor addi = new AdditionFactor();//加成
	public boolean advanced;//是否高级BUFF
	public boolean isNew;//是否新添加

	public Buff(int bId, int bContinues, int bCalcType, int bType, int bKeep, String bBattleImg, AdditionFactor addi, boolean advanced) {
		super();
		this.bId = bId;
		this.bContinues = bContinues;
		this.bCalcType = bCalcType;
		this.bType = bType;
		this.bKeep = bKeep;
		this.bBattleImg = bBattleImg;
		this.addi = addi;
		this.advanced = advanced;
		this.isNew = true;
	}

	@Override
	public int hashCode() {
		int h = 17 * 37 + this.bId;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Buff)) {
			return false;
		}

		Buff other = (Buff) obj;

		return this.bId == other.bId;
	}
}
