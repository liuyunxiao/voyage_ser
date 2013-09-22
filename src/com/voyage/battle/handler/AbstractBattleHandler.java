package com.voyage.battle.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyage.battle.bo.PlayerBO;
import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.enums.BattleType;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.player.Player;
import com.voyage.battle.processor.BattleProcessor;
import com.voyage.battle.processor.DefaultBattleProcessor;
import com.voyage.battle.thread.ReplayWriteThread;
import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.WrapBoolean;
import com.voyage.constant.Const;
import com.voyage.data.vo.CfgVipVO;
import com.voyage.service.IBattleService;
import com.voyage.service.ICorpsService;

/**
 * 战斗流程控制
 * */
public abstract class AbstractBattleHandler implements BattleHandler {
	private static int replayId;//生成战报ID的种子
	protected BattleProcessor processor;
	protected JSONObject param;
	protected PlayerBO atker;
	protected PlayerBO defer;
	protected List<Player> atkers;
	protected List<Player> defers;
	@Autowired
	private ICorpsService corpsService;
	@Autowired
	protected IBattleService battleService;

	public AbstractBattleHandler() {
		processor = new DefaultBattleProcessor();
		atkers = new ArrayList<Player>(1);
		defers = new ArrayList<Player>(1);
	}

	private void init0(JSONObject param) throws Exception {
		this.param = param;
		//保存阵型
		corpsService.moveAndCaptain(param);
		init();
	}

	/**
	 * 战斗初始化(必须在此设置atker、atkers、defer、defers)
	 * @return
	 */
	protected abstract void init() throws Exception;

	public final ReplayBO fight(JSONObject param) throws Exception {
		ReplayBO rb = null;
		try {
			//获得玩家锁
			WrapBoolean atkLock = CommonMgr.getInstance().getPkLock(param.getInt(Const.USERID));
			if (atkLock == null)
				throw new IllegalStateException("atk in battle");
			if (getBattleType().isPvp()) {
				WrapBoolean defLock = CommonMgr.getInstance().getPkLock(param.getInt(Const.DEFID));
				if (defLock == null)
					throw new IllegalStateException("def in battle");
			}
			//战斗相关
			init0(param);
			rb = processor.fight(atker, atkers, defer, defers);
			destory(rb);
		} finally {
			//释放玩家锁
			if (getBattleType().isPvp())
				CommonMgr.getInstance().releasePkLock(param.getInt(Const.DEFID));
			CommonMgr.getInstance().releasePkLock(param.getInt(Const.USERID));
		}
		return rb;
	}

	/**
	 * 战斗结束后设置奖励等
	 * @param br
	 */
	protected abstract void after(ReplayBO rb) throws Exception;

	/**
	 * 保存战报等
	 * @param br
	 */
	private void destory(ReplayBO rb) throws Exception {
		rb.battleType = getBattleType().getV();
		rb.winSide = getWinSide(rb);
		//设置跳过、加速功能
		CfgVipVO cvVO = CfgDataMgr.getInstance().getUnique(CfgDataMgr.KEY_CFG_VIP, atker.vipLevel);
		rb.cdSkip = cvVO.getCvCdSkip();
		rb.speedUp = (atker.level < cvVO.getCvSpeedUpLevel()) ? 0 : cvVO.getCvSpeedUp();

		after(rb);
		saveReplay(rb);
	}

	/**保存战报*/
	private void saveReplay(ReplayBO rb) {
		rb.battleId = genBattleId();
		ReplayWriteThread.getInstance().addWrite(rb);
	}

	/**判断胜者*/
	private int getWinSide(ReplayBO rb) {
		if (getBattleType().isPvp()) {
			return (rb.defer.hpMax == 0 || rb.atker.hp * 1.0 / rb.atker.hpMax > rb.defer.hp * 1.0 / rb.defer.hpMax) ? SideType.LEFT.getV() : SideType.RIGHT
					.getV();
		} else {
			return rb.defer.hp <= 0 ? SideType.LEFT.getV() : SideType.RIGHT.getV();
		}
	}

	/**生成战报ID*/
	private static String genBattleId() {
		int i = 0;
		synchronized (AbstractBattleHandler.class) {
			replayId = replayId % Integer.MAX_VALUE;
			i = replayId++;
		}

		return new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + i;
	}

	public abstract BattleType getBattleType();
}
