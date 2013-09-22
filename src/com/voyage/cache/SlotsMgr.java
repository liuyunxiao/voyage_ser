package com.voyage.cache;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.common.socket.ResponseUtil;
import com.voyage.SpringContext;
import com.voyage.battle.enums.WhetherType;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.constant.EventDef;
import com.voyage.data.bo.PondBO;
import com.voyage.data.bo.SlotsMsgBO;
import com.voyage.data.bo.SlotsRankBO;
import com.voyage.data.vo.SysParamVO;
import com.voyage.data.vo.SysUserSlotsVO;
import com.voyage.enums.SysParamType;
import com.voyage.service.IParamService;
import com.voyage.service.ISlotsService;
import com.voyage.socket.SocketSessionManager;
import com.voyage.util.CloneUtil;
import com.voyage.util.CommonUtil;

/**
 * 老虎机管理器
 * */
@Component
public class SlotsMgr {
	private final Logger logger = LoggerFactory.getLogger(SlotsMgr.class);
	private PondBO pond;
	private final Object pondLock = new Object();
	private Set<Integer> slotsUsers = new HashSet<Integer>();//正在老虎机界面上的玩家
	private Map<Integer, Cashback> cashbacks = new HashMap<Integer, Cashback>();//返金区
	@Autowired
	private IParamService paramService;
	@Autowired
	private ISlotsService slotsService;

	private SlotsMgr() {
	}

	public static SlotsMgr getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(SlotsMgr.class);
	}

	public synchronized void reload() throws Exception {
		logger.info("reload slots rank...");
		//初始化奖池
		initPond();
	}

	/**
	 * 重置周得分及奖池中奖状态
	 * */
	public void resetAllScore() throws Exception {
		logger.info("slots: reset all week score");
		slotsService.resetAllScore();
		synchronized (pondLock) {
			PondBO pondBak = (PondBO) CloneUtil.deepClone(pond);
			pond.clearHit();
			SysParamVO spVO = new SysParamVO();
			spVO.setSpType(SysParamType.POND.getV());
			spVO.setSpC(pond.bigHit - pondBak.bigHit);
			spVO.setSpD(pond.smallHit - pondBak.smallHit);
			paramService.updateByOffset(spVO);
		}
	}

	/**
	 * 初始化奖池相关
	 * */
	private void initPond() throws Exception {
		synchronized (pondLock) {
			pond = new PondBO();
			List<SysParamVO> slotsParams = paramService.selectByType(SysParamType.POND);
			SysParamVO insertVO = new SysParamVO();
			if (slotsParams.size() == 0) {
				insertVO.setSpType(SysParamType.POND.getV());
				insertVO.setSpA(PingyangConfig.getInstance().getBigPondInit());
				insertVO.setSpB(PingyangConfig.getInstance().getSmallPondInit());
				paramService.insertSelective(insertVO);
			}
			if (slotsParams.size() == 0)
				pond.reset();
			else
				pond.updateByOffset(new PondBO(slotsParams.get(0).getSpA(), slotsParams.get(0).getSpB(), slotsParams.get(0).getSpC(), slotsParams.get(0)
						.getSpD()));
		}
	}

	/**
	 * 放入奖池
	 * */
	private void toPondByOffset(int userId, int n) throws Exception {
		PondBO pondOffset = new PondBO(new Double(n * PingyangConfig.getInstance().getBigPondFactor()).intValue(), new Double(n
				* PingyangConfig.getInstance().getSmallPondFactor()).intValue());
		pondOffset = safePondOffset(pondOffset);
		if (pondOffset.big + pondOffset.small <= 0)
			return;
		pond.updateByOffset(pondOffset);
	}

	/**
	 * 尝试中奖
	 * */
	public PondBO tryReapPond(int userId, int n) throws Exception {
		PondBO pb = new PondBO();
		Point rate = null;
		synchronized (pondLock) {
			PondBO pondBak = (PondBO) CloneUtil.deepClone(pond);
			//放入奖池
			toPondByOffset(userId, n);
			//是否中大奖
			if (pond.bigHit == WhetherType.NO.getV()) {
				rate = PingyangConfig.getInstance().getBigPondWin();
				pb.bigHit = (CommonUtil.isCrit(rate.y, rate.x) ? WhetherType.YES : WhetherType.NO).getV();
				if (pb.bigHit == WhetherType.YES.getV()) {
					pond.bigHit = WhetherType.YES.getV();
					pb.big = pond.big;
					pond.big = PingyangConfig.getInstance().getBigPondInit();
				}
			}

			//是否中小奖
			if (!(pb.bigHit == WhetherType.YES.getV() || pond.smallHit == WhetherType.YES.getV())) {
				rate = PingyangConfig.getInstance().getSmallPondWin();
				pb.smallHit = (CommonUtil.isCrit(rate.y, rate.x) ? WhetherType.YES : WhetherType.NO).getV();
				if (pb.smallHit == WhetherType.YES.getV()) {
					pond.smallHit = WhetherType.YES.getV();
					pb.small = pond.small;
					pond.small = PingyangConfig.getInstance().getSmallPondInit();
				}
			}

			SysParamVO spVO = new SysParamVO();
			spVO.setSpType(SysParamType.POND.getV());
			spVO.setSpA(pond.big - pondBak.big);
			spVO.setSpB(pond.small - pondBak.small);
			spVO.setSpC(pond.bigHit - pondBak.bigHit);
			spVO.setSpD(pond.smallHit - pondBak.smallHit);
			paramService.updateByOffset(spVO);
		}
		sendSlotsMsg(new SlotsMsgBO(pond, null));
		return pb;
	}

	/**
	 * 奖池最大可增长值
	 * */
	private PondBO safePondOffset(PondBO pb) {
		return new PondBO(Math.min(pb.big, PingyangConfig.getInstance().getPondMax() - pond.big), Math.min(pb.small, PingyangConfig.getInstance().getPondMax()
				- pond.small));
	}

	/**
	 * 进入老虎机界面
	 * */
	public void enter(int userId) {
		slotsUsers.add(userId);
		//设置返金币区
		Cashback cb = cashbacks.get(userId);
		if (cb == null)
			cashbacks.put(userId, new Cashback());
		else
			cb.udpate(0);
	}

	/**
	 * 返回玩家的返金区
	 * */
	public Cashback getCashback(int userId) {
		return cashbacks.get(userId);
	}

	/**
	 * 获得最新奖池
	 * */
	public PondBO getPond() {
		return pond;
	}

	/**
	 * 通知前台刷新奖池或消息
	 * */
	public void sendSlotsMsg(SlotsMsgBO smBO) {
		for (Integer userId : slotsUsers) {
			try {
				if (SocketSessionManager.INSTANCE.isOnLine(userId)) {
					//通知前台
					ResponseUtil.notifyJson(userId, EventDef.SLOTS, smBO);
				}
			} catch (Exception e) {
				logger.debug("slots msg: ignore send error", e);
			}
		}
	}

	/**
	 * 返回排行榜: 排行前N名
	 * */
	public List<SlotsRankBO> getRanks() throws Exception {
		List<SlotsRankBO> ranks = new ArrayList<SlotsRankBO>(Const.SLOTS_RANK_N);
		List<SysUserSlotsVO> slots = slotsService.selectAllByScoreDesc(Const.SLOTS_RANK_N);
		for (SysUserSlotsVO susVO : slots) {
			ranks.add(new SlotsRankBO(susVO, CommonMgr.getInstance().getUserInfo(susVO.getSusUserId()).getName()));
		}
		return ranks;
	}
}
