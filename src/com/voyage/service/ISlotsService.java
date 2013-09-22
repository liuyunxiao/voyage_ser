package com.voyage.service;

import java.util.List;

import org.json.JSONObject;

import com.voyage.data.bo.SlotsBO;
import com.voyage.data.bo.SlotsPlayBO;
import com.voyage.data.vo.SysUserSlotsVO;

public interface ISlotsService {
	/**
	 * 重置所有玩家的周得分
	 * */
	int resetAllScore() throws Exception;

	/**
	 * 返回根据周得分降序排列的前N条
	 * */
	List<SysUserSlotsVO> selectAllByScoreDesc(int n) throws Exception;

	/**组装老虎机界面数据*/
	SlotsBO enter(int userId) throws Exception;

	/**
	 * 猜大小
	 * @param big 是否猜大
	 * @return 是否猜中(0:否，1：是)
	 * */
	int roll(int userId, int nGold, int big) throws Exception;

	/**
	 * 游戏开始
	 * @param  格式: 所压的水果{... press:[{csId:1,n:1},{csId:2, n:3}]}；
	 * */
	SlotsPlayBO play(JSONObject jo) throws Exception;
}
