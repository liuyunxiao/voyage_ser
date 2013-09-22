package com.voyage.service;

import java.util.List;

import com.voyage.battle.bo.ReplayBO;
import com.voyage.data.vo.SysUserBattleRecordVO;

public interface IBattleService {
	/**解除保护*/
	void belayOff(int userId) throws Exception;

	/**
	 * 结算对战双方的收益
	 * */
	void setPkSingleReward(ReplayBO rb) throws Exception;

	/**
	 * 结算挑战怪物的收益
	 * */
	void setMonsterReward(ReplayBO rb, int cmnId) throws Exception;

	/**
	 * 结算活动副本收益
	 * */
	void setActivityMapReward(ReplayBO rb, int camId) throws Exception;

	/**
	 * 判断玩家是否处于保护期
	 * */
	boolean isProtected(int defId) throws Exception;

	/**
	 * 保存战报到数据库并返回过期记录
	 * 
	 * */
	List<SysUserBattleRecordVO> saveRecord(ReplayBO replay) throws Exception;

}
