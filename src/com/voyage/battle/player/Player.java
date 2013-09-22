package com.voyage.battle.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.voyage.battle.bo.BuffBO;
import com.voyage.battle.board.Battleboard;
import com.voyage.battle.board.BoardPoint;
import com.voyage.battle.buff.Buff;
import com.voyage.battle.enums.BuffStatusType;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.enums.WhetherType;
import com.voyage.battle.exception.GameOverException;
import com.voyage.config.BattleConfig;
import com.voyage.constant.Const;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.util.CloneUtil;

public class Player implements Serializable {
	private static final long serialVersionUID = 7991184112921016214L;
	public int pId;
	public int pType;//PlayerType
	public int pos;
	public String pImg;
	public String bgImg;
	public int pHp;
	public int pHpMax;
	public int pCaptain;
	//四基础属性
	private double pShortAtk;
	private double pShortDef;
	private double pMagicAtk;
	private double pSoma;
	//加成
	//private AdditionFactor fixedAdd = new AdditionFactor();//固定加成(装备等产生的加成 )
	public AdditionFactor addition = new AdditionFactor();// 战斗中产生的加成

	public List<Buff> buffs = new ArrayList<Buff>(1);//BUFF列表

	public Player(SideType side, int pos, int captain, CfgCorpsVO corpsVO, AdditionFactor fixedAddi, double allFactor) {
		this.pos = side == null ? pos : (side.getV() * Battleboard.OFFSET_BASE + pos);
		this.pId = corpsVO.getCcId();
		this.pImg = corpsVO.getCcImg();
		this.bgImg = corpsVO.getCcCorpsGradeVO().getCcgCorpsBg();
		this.pShortAtk = fixedAddi.digit.shortAtkAdd * (1 + allFactor);
		this.pShortDef = fixedAddi.digit.shortDefAdd * (1 + allFactor);
		this.pMagicAtk = fixedAddi.digit.magicAtkAdd * (1 + allFactor);
		this.pSoma = fixedAddi.digit.somaAdd * (1 + allFactor);
		this.pHpMax = new Double(BattleConfig.getInstance().getHpFactor() * this.pSoma).intValue();
		this.pHp = this.pHpMax;
		this.pCaptain = captain;
	}

	/**重置加成*/
	public void resetAddition() {
		//addition = new AdditionFactor().add(fixedAdd);

		addition = new AdditionFactor();
	}

	/*public double getpShortAtkFixed() {
		return pShortAtk * (1 + fixedAdd.factor.shortAtkAdd) + fixedAdd.digit.shortAtkAdd;
	}*/

	public double getpShortAtk() {
		return pShortAtk * (1 + addition.factor.shortAtkAdd) + addition.digit.shortAtkAdd;
	}

	/*public double getpShortDefFixed() {
		return pShortDef * (1 + fixedAdd.factor.shortDefAdd) + fixedAdd.digit.shortDefAdd;
	}*/

	public double getpShortDef() {
		return pShortDef * (1 + addition.factor.shortDefAdd) + addition.digit.shortDefAdd;
	}

	/*public double getpMagicAtkFixed() {
		return pMagicAtk * (1 + fixedAdd.factor.magicAtkAdd) + fixedAdd.digit.magicAtkAdd;
	}*/

	public double getpMagicAtk() {
		return pMagicAtk * (1 + addition.factor.magicAtkAdd) + addition.digit.magicAtkAdd;
	}

	/*public double getpSomaFixed() {
		return pSoma * (1 + fixedAdd.factor.somaAdd) + fixedAdd.digit.somaAdd;
	}*/

	public double getpSoma() {
		return pSoma * (1 + addition.factor.somaAdd) + addition.digit.somaAdd;
	}

	/**获得需要重新播放特效的BUFF*/
	public List<BuffBO> getBeforeBuffs() {
		List<BuffBO> bList = new ArrayList<BuffBO>(1);
		for (Buff buff : buffs) {
			mergeBuff(new BuffBO(buff.bId, buff.bKeep == WhetherType.NO.getV() ? buff.bBattleImg : Const.NONE, getHpMf(buff), BuffStatusType.NOW.getV(),
					(buff.advanced ? WhetherType.YES : WhetherType.NO).getV()), bList);
		}
		return bList.size() > 0 ? bList : null;
	}

	/**合并相同BUFF*/
	private void mergeBuff(BuffBO buff, List<BuffBO> bList) {
		int i = bList.indexOf(buff);
		if (i == -1) {
			bList.add(buff);
		} else {
			bList.get(i).merge(buff);
		}
	}

	/**
	 * 添加一个BUFF
	 * */
	public BuffBO addBuff(Buff buff) {
		if (buff == null)
			return null;
		int i = buffs.indexOf(buff);
		if (i != -1) {
			buffs.remove(i);
		} else {
			addition.add(buff.addi);
		}
		this.buffs.add((Buff) CloneUtil.deepClone(buff));
		//System.out.println("addBuff->pos:" + this.pos + ",bid:" + buff.bId + ",continues:" + buff.bContinues);
		return new BuffBO(buff.bId, buff.bBattleImg, getHpMf(buff), buff.bKeep == WhetherType.YES.getV() ? BuffStatusType.KEEP.getV() : BuffStatusType.NOW
				.getV(), (buff.advanced ? WhetherType.YES : WhetherType.NO).getV());
	}

	/**获得BUFF引起的血量变化*/
	private int getHpMf(Buff buff) {
		int hpMf = 0;
		if (buff.addi.digit.hpAdd + buff.addi.factor.hpAdd > 0) {
			hpMf = new Double(pHpMax * buff.addi.factor.hpAdd + buff.addi.digit.hpAdd).intValue();
		}
		return hpMf;
	}

	/**
	 * BUFF加成
	 */
	private void buff() throws Exception {
		Iterator<Buff> buffIt = buffs.iterator();
		Buff nextBuff = null;
		while (buffIt.hasNext()) {
			nextBuff = buffIt.next();
			addition.add(nextBuff.addi);
		}
	}

	/**刷新加成*/
	public void refreshAddition() throws Exception {
		resetAddition();
		buff();
	}

	/**是否失去行动能力*/
	public boolean isDizzy() {
		return addition.dizzy;
	}

	/**是否能反弹伤害*/
	public boolean isAntiAble() {
		return addition.factor.damAntiAdd + addition.digit.damAntiAdd > 0;
	}

	public void ajustHp(int hpMf, Battleboard board) throws Exception {
		if (hpMf == 0)
			return;
		this.pHp = Math.min(Math.max(0, this.pHp + hpMf), this.pHpMax);
		BoardPoint target = board.getBoardPoint(this.pos);
		target.hp = this.pHp;
		if (this.pHp <= 0) {
			target.dead();
			tryOverGame(board);
		}
	}

	/**尝试结束游戏*/
	private void tryOverGame(Battleboard board) {
		if (-1 == board.findAlive(this.pos / Battleboard.OFFSET_BASE, this.pos % Battleboard.OFFSET_BASE)) {
			throw new GameOverException();
		}
	}
}
