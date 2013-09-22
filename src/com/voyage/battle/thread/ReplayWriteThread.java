package com.voyage.battle.thread;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyage.SpringContext;
import com.voyage.battle.bo.ReplayBO;
import com.voyage.battle.enums.BattleType;
import com.voyage.battle.enums.SideType;
import com.voyage.battle.enums.WhetherType;
import com.voyage.cache.RelationMgr;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.ErCode;
import com.voyage.data.bo.MsgBO;
import com.voyage.data.vo.SysUserBattleRecordVO;
import com.voyage.enums.MsgFormatType;
import com.voyage.enums.MsgType;
import com.voyage.exception.NotifyException;
import com.voyage.service.IBattleService;
import com.voyage.service.IRelationService;
import com.voyage.util.CloneUtil;
import com.voyage.util.CommonUtil;

/**将战报保存到本地线程*/
@Component
public class ReplayWriteThread implements Runnable {
	private Logger logger = LoggerFactory.getLogger(ReplayWriteThread.class);
	private BlockingDeque<ReplayBO> list = new LinkedBlockingDeque<ReplayBO>(1);
	private boolean running;
	@Autowired
	private IBattleService battleService;
	@Autowired
	private IRelationService relationService;

	public static ReplayWriteThread getInstance() {
		return SpringContext.getInstance().getSpringContext().getBean(ReplayWriteThread.class);
	}

	public ReplayBO getReplay(String battleId) throws Exception {
		ReplayBO replay = null;
		try {
			replay = (ReplayBO) CloneUtil.deSerial(PingyangConfig.getInstance().getReplayDiskDir() + battleId);
			if (replay == null) {
				throw new IllegalStateException("replay is null");
			}
		} catch (Exception e) {
			throw new NotifyException(ErCode.E303);
		}
		return replay;
	}

	public void addWrite(ReplayBO rb) {
		if (running)
			list.add(rb);
	}

	public void startMe() {
		this.running = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		logger.info("write replay thread start...");
		String diskDir = PingyangConfig.getInstance().getReplayDiskDir();
		File file = null;
		while (running) {
			try {
				ReplayBO replay = list.take();
				BattleType bt = BattleType.parse(replay.battleType);
				if (bt.isPvp()) {
					if (PingyangConfig.getInstance().getReplayToDisk() == WhetherType.YES.getV()) {
						//保存到本地
						CloneUtil.serial(replay, diskDir + replay.battleId);
					}
					List<SysUserBattleRecordVO> outDateRecords = battleService.saveRecord(replay);//保存到数据库
					if (outDateRecords.size() > 0) {
						//删除本地过期记录
						for (SysUserBattleRecordVO subrVO : outDateRecords) {
							file = new File(diskDir + subrVO.getSubrReplayId());
							if (file.exists())
								file.delete();
						}
					}
					notifyDefer(replay);
				}
			} catch (Exception e) {
				logger.error("write replay error", e);
			}
		}
	}

	/**
	 * 通知防守者
	 * */
	private void notifyDefer(ReplayBO replay) {
		try {
			int atkId = replay.atker.id;
			int defId = replay.defer.id;
			//有新战报
			relationService.sendBrt(defId);
			//通知防守者的好友
			List<Integer> friends = RelationMgr.getInstance().getRelation(defId);
			String content = CommonUtil.getFormatMsg(replay.winSide == SideType.LEFT.getV() ? MsgFormatType.DEFEND_LOST : MsgFormatType.DEFEND_WIN,
					new Object[] { defId, replay.defer.name, atkId, replay.atker.name });

			MsgBO msgBO = new MsgBO(MsgType.SYSTEM.getV(), 0, 0, content, null);
			for (Integer friend : friends) {
				if (friend != atkId) {
					msgBO.toId = friend;
					relationService.sendMsg(msgBO);
				}
			}
		} catch (Exception e) {
			logger.debug("battle: ignore send error", e);
		}
	}
}
