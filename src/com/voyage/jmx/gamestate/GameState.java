package com.voyage.jmx.gamestate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.RankMgr;
import com.voyage.socket.SocketSessionManager;
import com.voyage.util.CommonUtil;

public class GameState implements GameStateMBean {
	private final Logger logger = LoggerFactory.getLogger(GameState.class);

	@Override
	public int online() throws Exception {
		int n = SocketSessionManager.INSTANCE.getUserSocketSize();
		logger.warn(CommonUtil.getGmDo("online", null).toString());
		return n;
	}

	@Override
	public void reloadCache() throws Exception {
		CfgDataMgr.getInstance().reloadCache();
		logger.warn(CommonUtil.getGmDo("reloadCache", null).toString());
	}

	@Override
	public void reloadRank() throws Exception {
		RankMgr.getInstance().reload();
		logger.warn(CommonUtil.getGmDo("reloadRank", null).toString());
	}
}
