package com.voyage.battle.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**一次行动的影响(半回合）*/
public class HalfRoundBO implements Serializable {
	private static final long serialVersionUID = -4181211946203407822L;
	public int pos;//当前行动者位置
	public List<BuffBO> beforeBuffs;//战斗前的BUFF状态变化
	public EffectBO effectBO;//技能特效
	public List<TargetBO> targets = new ArrayList<TargetBO>(1);//目标相关
	public List<BuffBO> afterBuffs;//战后的BUFF状态变化
	public Integer dizzy;//是否丧失行动能力（属性不存在或为0：否; 1：是）

	public void addBeforeBuff(BuffBO bo) {
		if (bo == null || !bo.validable())
			return;
		if (beforeBuffs == null)
			beforeBuffs = new ArrayList<BuffBO>(1);
		beforeBuffs.add(bo);
	}

	public void addAfterBuff(BuffBO bo) {
		if (bo == null)
			return;
		if (afterBuffs == null)
			afterBuffs = new ArrayList<BuffBO>(1);
		afterBuffs.add(bo);
	}

	public List<BuffBO> getBeforeBuffs() {
		return beforeBuffs;
	}

	public void setBeforeBuffs(List<BuffBO> beforeBuffs) {
		this.beforeBuffs = beforeBuffs;
	}

	public List<BuffBO> getAfterBuffs() {
		return afterBuffs;
	}

	public void setAfterBuffs(List<BuffBO> afterBuffs) {
		this.afterBuffs = afterBuffs;
	}

	public HalfRoundBO(int pos) {
		this.pos = pos;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public EffectBO getEffectBO() {
		return effectBO;
	}

	public void setEffectBO(EffectBO effectBO) {
		this.effectBO = effectBO;
	}

	public List<TargetBO> getTargets() {
		return targets;
	}

	public void setTargets(List<TargetBO> targets) {
		this.targets = targets;
	}

	public Integer getDizzy() {
		return dizzy;
	}

	public void setDizzy(Integer dizzy) {
		this.dizzy = dizzy;
	}

}
