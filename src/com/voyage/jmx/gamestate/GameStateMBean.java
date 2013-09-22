package com.voyage.jmx.gamestate;

public interface GameStateMBean {
	/**统计在线人数*/
	int online() throws Exception;

	/**
	 * 刷新CFG_数据缓存
	 * */
	void reloadCache() throws Exception;

	/**
	 * 刷新排行榜
	 * */
	void reloadRank() throws Exception;
}
