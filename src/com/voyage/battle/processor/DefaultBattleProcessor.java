package com.voyage.battle.processor;

import java.util.Iterator;
import java.util.List;

import com.voyage.battle.bo.BuffBO;
import com.voyage.battle.bo.DamageBO;
import com.voyage.battle.bo.EffectBO;
import com.voyage.battle.bo.HalfRoundBO;
import com.voyage.battle.bo.PlayerBO;
import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.bo.RoundBO;
import com.voyage.battle.bo.TargetBO;
import com.voyage.battle.board.Battleboard;
import com.voyage.battle.board.BoardPoint;
import com.voyage.battle.buff.Buff;
import com.voyage.battle.buff.BuffProxy;
import com.voyage.battle.enums.BenefitType;
import com.voyage.battle.enums.BuffCalcType;
import com.voyage.battle.enums.BuffStatusType;
import com.voyage.battle.enums.CorpsType;
import com.voyage.battle.enums.DamageType;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.enums.WhetherType;
import com.voyage.battle.exception.BattleException;
import com.voyage.battle.exception.GameOverException;
import com.voyage.battle.player.Player;
import com.voyage.battle.round.RoundContext;
import com.voyage.battle.rule.TargetRule;
import com.voyage.cache.CfgDataMgr;
import com.voyage.config.BattleConfig;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.data.vo.CfgCorpsTypeVO;
import com.voyage.data.vo.CfgCorpsVO;
import com.voyage.data.vo.CfgSceneVO;
import com.voyage.data.vo.CfgSkillVO;
import com.voyage.exception.NotifyException;
import com.voyage.util.CommonUtil;

public class DefaultBattleProcessor implements BattleProcessor {
	private ReplayBO rb;
	private BattleConfig bc;
	private int lastActPos;//上一次行动者的位置
	private int lastLeftActPos;//左侧上一次行动时的位置
	private int lastRightActPos;//右侧上一次行动时的位置
	private int lastActDam;//上一次行动者造成的伤害(带符号)
	private boolean newRound;//是否新回合
	private int totalActs;//行动次数累计
	private HalfRoundBO actBO;//本次行动信息
	private RoundContext context;

	public DefaultBattleProcessor() {
		rb = new ReplayBO();
		bc = BattleConfig.getInstance();
		context = new RoundContext(new Battleboard(), new TargetRule());
	}

	@Override
	public ReplayBO fight(PlayerBO atker, List<Player> atkers, PlayerBO defer, List<Player> defers) throws Exception {
		if (init(atker, atkers, defer, defers)) {
			try {
				while (hasRound() && nextActor() != -1) {
					beforeAct();
					//System.out.println("round:" + context.round + ",lastActPos:" + lastActPos);
					toAct();
					afterAct();
				}
			} catch (Exception e) {
				if (!(e instanceof GameOverException)) {
					throw e;
				}
				rb.rounds.get(rb.rounds.size() - 1).items.add(actBO);
			}
		}
		destory();
		return rb;
	}

	/**是否还有剩余回合*/
	private boolean hasRound() {
		return totalActs < rb.maxRounds * 2;
	}

	private void destory() {
		rb.atker.hp = getLeftHp(SideType.LEFT);
		rb.defer.hp = getLeftHp(SideType.RIGHT);
		rb.totalRounds = context.round;
	}

	/**获得某测总剩余血量*/
	private int getLeftHp(SideType side) {
		int hp = 0;
		List<Player> players = side == SideType.LEFT ? context.atkers : context.defers;
		if (players != null) {
			for (Player player : players) {
				hp += player.pHp;
			}
		}
		return hp;

	}

	/**行动前*/
	private void beforeAct() throws Exception {
		newRound = totalActs % 2 == 0;
		if (newRound) {
			context.round++;
			rb.rounds.add(new RoundBO(context.round));
		}
		totalActs++;
		context.resetContext(lastActPos);
		lastActDam = 0;
	}

	/**行动*/
	private void toAct() throws Exception {
		actBO = makeHalfRound(context.source, false);
		//去掉战前BUFF
		/*List<BuffBO> beforeBuffs = context.atk.getBeforeBuffs();
		if (beforeBuffs != null) {
			for (BuffBO buff : beforeBuffs) {
				actBO.addBeforeBuff(buff);
				context.atk.ajustHp(buff.hpMf, context.board);
			}
		}*/
		if (context.atk.isDizzy()) {//眩晕时无法行动
			actBO.dizzy = WhetherType.YES.getV();
		} else {
			shortAtk(actBO, context.source, context.atk, context.target, context.def);
			skillAtk(actBO, context.source, context.atk, context.target, context.def, false, null, true);
		}
	}

	/**行动后*/
	private void afterAct() throws Exception {
		rb.rounds.get(rb.rounds.size() - 1).items.add(actBO);
		decBuff();
	}

	/**初始化一个HalfRoundBO*/
	private HalfRoundBO makeHalfRound(BoardPoint source, boolean isAnti) throws Exception {
		HalfRoundBO actBO = new HalfRoundBO(source.pos);
		CfgCorpsVO atkCorps = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, source.id);
		actBO.effectBO = new EffectBO(isAnti ? Const.NONE : atkCorps.getCcCorpsTypeVO().getCctStartImg(), isAnti ? Const.NONE : atkCorps.getCcCorpsTypeVO()
				.getCctBulletImg(), atkCorps.getCcCorpsTypeVO().getCctEndImg());//设置起手特效、弹道、目标特效
		return actBO;
	}

	/**物理攻击*/
	private void shortAtk(HalfRoundBO actBO, BoardPoint source, Player atk, BoardPoint target, Player def) throws Exception {
		if (!source.qAlive())
			return;
		if (atk.isDizzy())
			return;

		CfgCorpsVO atkCorps = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, source.id);
		if (atkCorps.isSpecialCorps())//特殊兵种没有物理攻击
			return;

		TargetBO targetBO = new TargetBO(target.pos);
		CfgCorpsVO defCorps = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, target.id);
		//是否触发守方的技能
		boolean defTriggerSkill = !def.isDizzy() && defCorps.hasSkill() && CommonUtil.isCrit(defCorps.getCcDefSkillRate());//要触发技能守方必须具有行动能力
		CfgSkillVO defSkill = defCorps.getCcDefSkillVO();
		Buff defBuff = null;
		if (defTriggerSkill && defSkill.hasBuff()) {//触发守方技能携带的BUFF
			boolean isCrit = !CommonUtil.isCrit(defSkill.getCsBuffRate());
			defBuff = (!isCrit ? defSkill.getCsBuffVO() : defSkill.getCsBuffVO2()).getBuff(isCrit);
			if (!defCorps.isSpecialCorps())//非特殊兵种只给自己加BUFF,且不展现
				def.addBuff(defBuff);
		}
		//是否暴击
		boolean isCrit = CommonUtil.isCrit(bc.getCritRate());
		//伤害计算
		lastActDam = -1
				* Math.max(0, new Double(((1 + (isCrit ? bc.getCritFactor() : 0)) * bc.getShortAtkFactor() * atk.getpShortAtk() - bc.getShortDefFactor()
						* (1 + (defTriggerSkill ? 1 : 0) * defCorps.getCcCorpsGradeVO().getCcgFactor() * defCorps.getCcDefFactor()) * def.getpShortDef())
						* getRestrainFactor(atkCorps.getCcCorpsTypeVO(), defCorps.getCcCorpsTypeVO())
						* (1 + atk.addition.factor.damAdd - def.addition.factor.damImmuneAdd) + atk.addition.digit.damAdd - def.addition.digit.damImmuneAdd)
						.intValue());

		applyShortMf(actBO, targetBO, def, lastActDam, isCrit);
		if (defTriggerSkill) {
			HalfRoundBO hr = makeHalfRound(target, true);
			if (skillAtk(hr, target, def, source, atk, true, defBuff, false)) {
				targetBO.anti = hr;
			}
		}
	}

	/**技能攻击
	 * @param sb 伤害等格式串
	 * @param forceTrigger 强制触发攻方技能
	 * @param atkBuff
	 * @param antiAble 守方是否可反射
	 * @return 是否触发了攻方技能
	 * */
	public boolean skillAtk(HalfRoundBO actBO, BoardPoint source, Player atk, BoardPoint target, Player def, boolean forceTriggerSki, Buff atkBuff,
			boolean antiAble) throws Exception {
		if (!source.qAlive())
			return false;
		if (atk.isDizzy())
			return false;

		CfgCorpsVO atkCorps = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, source.id);

		//是否触发攻方的技能
		boolean triggerSkill = atkCorps.hasSkill()
				&& (forceTriggerSki || CommonUtil.isCrit(antiAble ? atkCorps.getCcAtkSkillRate() : atkCorps.getCcDefSkillRate()));
		if (triggerSkill) {
			CfgSkillVO tiggerSkillVO = antiAble ? atkCorps.getCcAtkSkillVO() : atkCorps.getCcDefSkillVO();
			boolean isCrit = forceTriggerSki && atkBuff != null ? atkBuff.advanced : false;//高级BUFF
			if (antiAble && !forceTriggerSki && tiggerSkillVO.hasBuff()) {//触发攻方技能携带的BUFF
				isCrit = !CommonUtil.isCrit(tiggerSkillVO.getCsBuffRate());
				atkBuff = (!isCrit ? tiggerSkillVO.getCsBuffVO() : tiggerSkillVO.getCsBuffVO2()).getBuff(isCrit);
				if (!atkCorps.isSpecialCorps())//非特殊兵种只给自己加BUFF,且不展现
					atk.addBuff(atkBuff);
			}
			actBO.effectBO.setSkillEffect(tiggerSkillVO.getCsId(), tiggerSkillVO.getCsBattleBubble(), tiggerSkillVO.getCsBattleImg(), tiggerSkillVO
					.getCsBattleSide(), atkBuff != null && atkCorps.isSpecialCorps() ? atkBuff.bType : BenefitType.COMMON.getV());

			List<BoardPoint> targets = context.targetRule.getTarget(source, target, tiggerSkillVO.getCsRule().trim(), context.board);
			Player def2 = null;
			TargetBO targetBO2 = null;
			boolean defTriggerSkill = false;
			BuffProxy defBuff2Proxy = new BuffProxy();
			for (BoardPoint target2 : targets) {
				def2 = context.getPlayer(target2.pos);
				targetBO2 = new TargetBO(target2.pos);
				defTriggerSkill = skillAtk(targetBO2, atk, atkCorps, atkBuff, def2, antiAble, defBuff2Proxy);
				applySkillMf(actBO, targetBO2, def2, lastActDam, isCrit);
				if (antiAble && defTriggerSkill) {
					HalfRoundBO hr = makeHalfRound(target2, true);
					if (skillAtk(hr, target2, def2, source, atk, true, defBuff2Proxy.buff, false)) {
						targetBO2.anti = hr;
					}
				}
			}
		}
		return triggerSkill;
	}

	/**计算技能伤害等*/
	public boolean skillAtk(TargetBO targetBO, Player atk, CfgCorpsVO atkCorps, Buff atkBuff, Player def, boolean antiAble, BuffProxy defBuff2Proxy)
			throws Exception {
		boolean defTriggerSkill = false;
		defBuff2Proxy.buff = null;//重置
		CfgCorpsVO defCorps = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_CORPS, def.pId);
		if (atkCorps.isSpecialCorps()) {
			targetBO.addBuff(def.addBuff(atkBuff), def, context.board);
		} else {
			if (antiAble) {
				//是否触发守方的技能
				defTriggerSkill = !def.isDizzy() && defCorps.hasSkill() && CommonUtil.isCrit(defCorps.getCcDefSkillRate());
				CfgSkillVO defSkill = defCorps.getCcDefSkillVO();
				if (defTriggerSkill && defSkill.hasBuff()) {//触发守方技能携带的BUFF
					boolean isCrit = !CommonUtil.isCrit(defSkill.getCsBuffRate());
					defBuff2Proxy.buff = (!isCrit ? defSkill.getCsBuffVO() : defSkill.getCsBuffVO2()).getBuff(isCrit);
					if (!defCorps.isSpecialCorps())//非特殊兵种只给自己加BUFF,且不展现
						def.addBuff(defBuff2Proxy.buff);
				}
			}
			lastActDam = -1
					* Math.max(0,
							new Double(((!antiAble && atk.isAntiAble()) ? (-lastActDam * atk.addition.factor.damAntiAdd + atk.addition.digit.damAntiAdd)
									: atkCorps.getCcCorpsGradeVO().getCcgFactor() * atkCorps.getCcAtkFactor() * atk.getpMagicAtk()
											* getRestrainFactor(atkCorps.getCcCorpsTypeVO(), defCorps.getCcCorpsTypeVO()))
									* (1 + atk.addition.factor.damAdd - def.addition.factor.damImmuneAdd)
									+ atk.addition.digit.damAdd
									- def.addition.digit.damImmuneAdd).intValue());
		}
		return defTriggerSkill;
	}

	/**
	 * 物理攻击对HP的影响等*/
	private void applyShortMf(HalfRoundBO actBO, TargetBO targetBO, Player def, int lastActDam, boolean isCrit) throws Exception {
		applyHpMf(actBO, targetBO, def, lastActDam, isCrit, DamageType.PHY);
	}

	/**
	 * 技能对HP的影响等
	 **/
	private void applySkillMf(HalfRoundBO actBO, TargetBO targetBO, Player def, int lastActDam, boolean isCrit) throws Exception {
		applyHpMf(actBO, targetBO, def, lastActDam, isCrit, DamageType.SKI);
	}

	private void applyHpMf(HalfRoundBO actBO, TargetBO targetBO, Player def, int lastActDam, boolean isCrit, DamageType dType) throws Exception {
		actBO.targets.add(targetBO);
		if (!(dType == DamageType.SKI && lastActDam == 0))//技能伤害为0时不显示伤害
			targetBO.damage = new DamageBO(dType.getV(), lastActDam, isCrit);
		def.ajustHp(lastActDam, context.board);
	}

	/**刷新BUFF*/
	public void decBuff() throws Exception {
		decBuff(SideType.LEFT.getV());
		decBuff(SideType.RIGHT.getV());
	}

	/**刷新BUFF*/
	public void decBuff(int side) throws Exception {
		Iterator<Buff> it = null;
		Buff nextBuff = null;
		List<Player> players = side == SideType.LEFT.getV() ? context.atkers : context.defers;
		for (Player player : players) {
			if (player.pHp <= 0)
				continue;
			boolean removed = false;
			it = player.buffs.iterator();
			while (it.hasNext()) {
				nextBuff = it.next();
				if (nextBuff.bCalcType == BuffCalcType.NOW.getV()) {
					//System.out.println("now,rm->pos:" + player.pos + ",bid:" + nextBuff.bId + ",continues:" + nextBuff.bContinues);
					it.remove();
					removed = true;
					continue;
				}

				if (player.pos == lastActPos && !nextBuff.isNew) {
					//System.out.println("unNow,update->pos:" + player.pos + ",bid:" + nextBuff.bId + ",continues:" + nextBuff.bContinues);
					nextBuff.bContinues -= 1;
					//System.out.println("unNow,update->pos:" + player.pos + ",bid:" + nextBuff.bId + ",continues:" + nextBuff.bContinues);
					if (nextBuff.bContinues <= 0) {
						//System.out.println("unNow,rm->pos:" + player.pos + ",bid:" + nextBuff.bId + ",continues:" + nextBuff.bContinues);
						it.remove();
						removed = true;
						if (!isExistBattleImg(nextBuff, player)) {
							actBO.addAfterBuff(new BuffBO(nextBuff.bBattleImg, BuffStatusType.VANISH.getV()));
						}
					}
				} else {
					nextBuff.isNew = false;
					//	System.out.println("unNow,no update->pos:" + player.pos + ",bid:" + nextBuff.bId + ",continues:" + nextBuff.bContinues);
				}
			}
			if (removed) {//刷新加成
				player.refreshAddition();
			}
		}
	}

	/**
	 * 是否存在与buff相同的特效（不包括自身）
	 * */
	private boolean isExistBattleImg(Buff buff, Player player) {
		if (buff.bBattleImg.equals(Const.NONE))
			return false;
		for (Buff bf : player.buffs) {
			if (bf.bId != buff.bId && bf.bBattleImg.equals(buff.bBattleImg))
				return true;
		}
		return false;
	}

	/**获得克制系数*/
	public double getRestrainFactor(CfgCorpsTypeVO atkType, CfgCorpsTypeVO defType) {
		if (atkType.getCctRestrainLessId().equals(defType.getCctId())) {
			return bc.getLessAtkFactor();
		} else if (atkType.getCctRestrainMoreId().equals(defType.getCctId())) {
			return bc.getMoreAtkFactor();
		} else if (defType.getCctRestrainLessId().equals(atkType.getCctId())) {
			return bc.getLessDefFactor();
		} else if (defType.getCctRestrainMoreId().equals(atkType.getCctId())) {
			return bc.getMoreDefFactor();
		} else if (CorpsType.isSpecailCorps(defType.getCctId())) {
			return bc.getSpecialAtkFactor();
		} else {
			return 1.0;
		}
	}

	public boolean init(PlayerBO atker, List<Player> atkers, PlayerBO defer, List<Player> defers) throws Exception {
		if (atker == null || defer == null)
			throw new BattleException("player is null");
		if (atkers == null || atkers.size() == 0)//攻方出战阵容不能为空
			throw new NotifyException(ErCode.E301);
		rb.atker = atker;
		rb.defer = defer;
		List<CfgSceneVO> scenes = CfgDataMgr.getInstance().getList(CfgDataMgr.KEY_CFG_SCENE);
		rb.battleScene = scenes.get(Const.RAND.nextInt(scenes.size())).getCsImg();
		rb.maxRounds = bc.getMaxRounds();
		initPlayerBO(rb.atker, atkers);
		initPlayerBO(rb.defer, defers);
		context.setPlayers(atkers, defers);
		return !(defers == null || defers.size() == 0);
	}

	private void initPlayerBO(PlayerBO per, List<Player> pers) throws Exception {
		if (pers == null || pers.size() == 0)
			return;
		BoardPoint bp = null;
		for (Player p : pers) {
			bp = context.board.getBoardPoint(p.pos);
			bp.validIt(p.pId, p.pHp, p.pHpMax, p.pImg, p.bgImg, p.pCaptain);
			per.army.add(bp);
			per.hpMax += p.pHpMax;
		}
		per.hp = per.hpMax;
	}

	/**下一个行动者
	 * @return -1 表示未找到
	 * */
	public int nextActor() {
		int side = lastActPos == 0 || lastActPos / Battleboard.OFFSET_BASE == SideType.RIGHT.getV() ? SideType.LEFT.getV() : SideType.RIGHT.getV();
		lastActPos = context.board.findAlive(side, side == SideType.LEFT.getV() ? lastLeftActPos % Battleboard.OFFSET_BASE : lastRightActPos
				% Battleboard.OFFSET_BASE);
		if (side == SideType.LEFT.getV()) {
			lastLeftActPos = lastActPos;
		} else {
			lastRightActPos = lastActPos;
		}
		return lastActPos;
	}

}
