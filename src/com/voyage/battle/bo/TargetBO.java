package com.voyage.battle.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.voyage.battle.board.Battleboard;
import com.voyage.battle.player.Player;

/**
 *目标影响相关
 */
public class TargetBO implements Serializable {
	private static final long serialVersionUID = -5418589957842237702L;
	public int pos;//目标位置
	public List<BuffBO> buffs;//BUFF状态相关
	public DamageBO damage;//伤害
	public HalfRoundBO anti;//被攻击时触发的技能

	public TargetBO(int pos) {
		super();
		this.pos = pos;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	/**添加BUFF且更新血量*/
	public void addBuff(BuffBO bo, Player player, Battleboard board) throws Exception {
		if (bo == null || !bo.validable())
			return;
		if (buffs == null)
			buffs = new ArrayList<BuffBO>();
		buffs.add(bo);
		player.ajustHp(bo.hpMf, board);
	}

	public List<BuffBO> getBuffs() {
		return buffs;
	}

	public void setBuffs(List<BuffBO> buffs) {
		this.buffs = buffs;
	}

	public DamageBO getDamage() {
		return damage;
	}

	public void setDamage(DamageBO damage) {
		this.damage = damage;
	}

	public HalfRoundBO getAnti() {
		return anti;
	}

	public void setAnti(HalfRoundBO anti) {
		this.anti = anti;
	}

}
